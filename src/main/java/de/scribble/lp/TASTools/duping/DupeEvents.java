package de.scribble.lp.TASTools.duping;

import java.io.File;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.savestates.gui.GuiSavestateIngameMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.WorldEvent;

public class DupeEvents {
	private Minecraft mc= Minecraft.getMinecraft();
	public static boolean dupingenabled;
	
	@SubscribeEvent
	public void onCloseServer(WorldEvent.Unload ev){
		if((mc.currentScreen instanceof GuiIngameMenu||mc.currentScreen instanceof GuiSavestateIngameMenu)&&dupingenabled){
			CommonProxy.logger.info("Start saving items/chests...");
			new RecordingDupe().saveFile((EntityPlayer) ev.world.playerEntities.get(0));
		}
	}
	
	@SubscribeEvent
	public void onOpenServer(PlayerLoggedInEvent ev){
		if (dupingenabled&&!ev.player.worldObj.isRemote) {
			File file= new File(mc.mcDataDir, "saves" + File.separator +mc.getIntegratedServer().getFolderName()+File.separator+"latest_dupe.txt");
			if (file.exists()){
				CommonProxy.logger.info("Start refilling...");
				new RefillingDupe().refill(file, ev.player);
			}
		}
	}
	
	@SubscribeEvent
	public void pressKeybinding(InputEvent.KeyInputEvent ev){
		if(ClientProxy.DupeKey.isPressed()&&dupingenabled){
			ModLoader.NETWORK.sendToServer(new DupePacket());
		}
	}
}
