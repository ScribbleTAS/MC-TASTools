package de.scribble.lp.TASTools.misc;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class MiscEvents {
	@SubscribeEvent
	public void onMainMenu(GuiOpenEvent ev) {
		if(ev.gui instanceof GuiMainMenu) {
			((GuiMainMenu) ev.gui).updateCounter=0;
			((GuiMainMenu) ev.gui).splashText="Well, someone is using TASTools!";
		}
	}
	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerLoggedOutEvent ev) {
		if (!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()&&!Minecraft.getMinecraft().getIntegratedServer().getPublic()) {
			File file = new File(Minecraft.getMinecraft().mcDataDir,
					"saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName()
							+ File.separator + "miscthings.txt");
			new MiscSaving().saveThings(ev.player, file);
		}
	}
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent ev) {
		if (!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()&&!Minecraft.getMinecraft().getIntegratedServer().getPublic()) {
			File file = new File(Minecraft.getMinecraft().mcDataDir,
					"saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName()
							+ File.separator + "miscthings.txt");
			if (file.exists()) {
				ev.player.portalCounter=new MiscReapplying().getPortalTime(file);
			}
		}
	}
}
