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

public class DupeEvents2 {
	private Minecraft mc= Minecraft.getMinecraft();
	
	@SubscribeEvent
	public void onCloseServer(WorldEvent.Unload ev){
		if((mc.currentScreen instanceof GuiIngameMenu||mc.currentScreen instanceof GuiSavestateIngameMenu)&&DupeEvents.dupingenabled){
				if(ev.world.playerEntities.size()!=0){
				CommonProxy.logger.info("Start saving items/chests...");
				new RecordingDupe().saveFile((EntityPlayer) ev.world.playerEntities.get(0));
			}
		}
	}
}
