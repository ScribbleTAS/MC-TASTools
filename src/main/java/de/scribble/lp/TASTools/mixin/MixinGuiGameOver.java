package de.scribble.lp.TASTools.mixin;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.savestates.SavestateEvents;
import de.scribble.lp.TASTools.savestates.SavestatePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.resources.I18n;

@Mixin(GuiGameOver.class)
public class MixinGuiGameOver extends GuiScreen{
	private int enableButtonsTimer;
	
	@Inject(method="initGui", at=@At("HEAD"), cancellable=true)
	public void redoInitGui(CallbackInfo ci) {
		if(SavestateEvents.reloadgameoverenabled) {
			this.buttonList.clear();
	        this.enableButtonsTimer = 0;
	
	        if (this.mc.world.getWorldInfo().isHardcoreModeEnabled())
	        {
	            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.spectate")));
	            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("Break the game!")));
	            this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 120, I18n.format("Reload the savestate")));
	        }
	        else
	        {
	            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.respawn")));
	            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.titleScreen")));
	            this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 120, I18n.format("Reload the savestate")));
	
	            if (this.mc.getSession() == null)
	            {
	                (this.buttonList.get(1)).enabled = false;
	            }
	        }
	
	        for (GuiButton guibutton : this.buttonList)
	        {
	        	if(buttonList.get(2)==guibutton) {
	        		continue;
	        	}
	            guibutton.enabled = false;
	        }
		}else {
			this.buttonList.clear();
	        this.enableButtonsTimer = 0;

	        if (this.mc.world.getWorldInfo().isHardcoreModeEnabled())
	        {
	            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.spectate")));
	            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen." + (this.mc.isIntegratedServerRunning() ? "deleteWorld" : "leaveServer"))));
	        }
	        else
	        {
	            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.respawn")));
	            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.titleScreen")));

	            if (this.mc.getSession() == null)
	            {
	                (this.buttonList.get(1)).enabled = false;
	            }
	        }

	        for (GuiButton guibutton : this.buttonList)
	        {
	            guibutton.enabled = false;
	        }
		}
		ci.cancel();
	}
	@Inject(method="actionPerformed", at=@At("HEAD"), cancellable = true)
	public void redoActionPerformed(GuiButton button, CallbackInfo ci) throws IOException{
		if (SavestateEvents.reloadgameoverenabled) {
			switch (button.id)
	        {
	            case 0:
	                this.mc.player.respawnPlayer();
	                this.mc.displayGuiScreen((GuiScreen)null);
	                break;
	            case 1:

	                if (this.mc.world.getWorldInfo().isHardcoreModeEnabled())
	                {
	                	CommonProxy.logger.warn("Hello dear player, you just clicked the 'Delete World' option! And you may realise that this didn't work as planned... That is not TASTools fault tho, this is the actual Minecraft code... I could fix this, but then it's not vanilla anymore and maybe there is a use for this...");
	                    this.mc.displayGuiScreen(new GuiMainMenu());
	                }
	                else
	                {
	                    GuiYesNo guiyesno = new GuiYesNo(this, I18n.format("deathScreen.quit.confirm"), "", I18n.format("deathScreen.titleScreen"), I18n.format("deathScreen.respawn"), 0);
	                    this.mc.displayGuiScreen(guiyesno);
	                    guiyesno.setButtonDelay(20);
	                }
	                return;
	                //This is the new button!
	            case 2:
	            	if (mc.player.canUseCommand(2, "savestate")) {
	    				ModLoader.NETWORK.sendToServer(new SavestatePacket(false));
	    			}else {
	    				CommonProxy.logger.info("You don't have the required permissions to use the loadstate button!");
	    			}
	            	return;
	        }
		} else {
			switch (button.id)
	        {
	            case 0:
	                this.mc.player.respawnPlayer();
	                this.mc.displayGuiScreen((GuiScreen)null);
	                break;
	            case 1:

	                if (this.mc.world.getWorldInfo().isHardcoreModeEnabled())
	                {
	                    this.mc.displayGuiScreen(new GuiMainMenu());
	                }
	                else
	                {
	                    GuiYesNo guiyesno = new GuiYesNo(this, I18n.format("deathScreen.quit.confirm"), "", I18n.format("deathScreen.titleScreen"), I18n.format("deathScreen.respawn"), 0);
	                    this.mc.displayGuiScreen(guiyesno);
	                    guiyesno.setButtonDelay(20);
	                }
	        }
		}
		ci.cancel();
	}
}
