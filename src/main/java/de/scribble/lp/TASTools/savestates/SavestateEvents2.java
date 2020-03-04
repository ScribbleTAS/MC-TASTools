package de.scribble.lp.TASTools.savestates;

import java.io.IOException;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.misc.Util;
import de.scribble.lp.TASTools.savestates.gui.GuiSavestateIngameMenu;
import de.scribble.lp.TASTools.savestates.gui.GuiSavestateLoadingScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.common.MinecraftForge;

public class SavestateEvents2 {
	Minecraft mc= Minecraft.getMinecraft();
	int endtimer=20;
	public static int tickspassed=0;
	public static boolean clientsaving;
	public static boolean clientloading;
	
	/**
	 * Used when making a savestate. Wait's 20 ticks before it does
	 * 
	 */
	@SubscribeEvent
	public void makeSavestate(TickEvent.ClientTickEvent ev) {
		if (ev.phase==Phase.START) {
			if (clientsaving) {
				if (tickspassed>=20) {
					try {
						SavestateHandlerClient.copyDirectory(SavestateHandlerClient.currentworldfolder, SavestateHandlerClient.targetsavefolder, new String[] {" "});
						
					} catch (IOException e) {
						CommonProxy.logger.error("Could not copy the directory "+SavestateHandlerClient.currentworldfolder.getPath()+" to "+SavestateHandlerClient.targetsavefolder.getPath()+" for some reason (Savestate save)");
						e.printStackTrace();
						clientsaving=false;
						SavestateHandlerClient.isSaving=false;
						return;
					}
					if(Util.enableSavestateScreenshotting) {
						new Util().saveScreenshotAt(SavestateHandlerClient.targetsavefolder, SavestateHandlerClient.screenshotname, SavestateHandlerClient.screenshot);
					}
					clientsaving=false;
					Minecraft.getMinecraft().displayGuiScreen(new GuiSavestateIngameMenu());
					SavestateHandlerClient.isSaving=false;
					return;
				}
				tickspassed++;
			}
		}
	}
	
	@SubscribeEvent
	public void loadSavestate(TickEvent.ClientTickEvent ev) {
		if (ev.phase == Phase.START) {
			if (!mc.isIntegratedServerRunning()) {
				if (tickspassed >= 20) {
					if (!(mc.currentScreen instanceof GuiSavestateLoadingScreen)) {
						SavestateHandlerClient.isLoading=false;
						return;
					}
					MinecraftForge.EVENT_BUS.unregister(this);
					SavestateHandlerClient.deleteDirContents(SavestateHandlerClient.currentworldfolder, new String[] { " " });
					try {
						SavestateHandlerClient.copyDirectory(SavestateHandlerClient.targetsavefolder, SavestateHandlerClient.currentworldfolder, new String[] { " " });
					} catch (IOException e) {
						CommonProxy.logger.error("Could not copy the directory " + SavestateHandlerClient.currentworldfolder.getPath() + " to "
								+ SavestateHandlerClient.targetsavefolder.getPath() + " for some reason (Savestate load)");
						e.printStackTrace();
						mc.displayGuiScreen(new GuiMainMenu());
						SavestateHandlerClient.isLoading=false;
						return;
					}
					FMLClientHandler.instance().getClient().launchIntegratedServer(SavestateHandlerClient.foldername, SavestateHandlerClient.worldname, null);
					SavestateHandlerClient.isLoading=false;
				}
				tickspassed++;
			}
		}
	}
}
