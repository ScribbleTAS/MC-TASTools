package de.scribble.lp.TASTools.savestates;

import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.savestates.gui.GuiSavestateDeathReloadScreen;
import de.scribble.lp.TASTools.savestates.gui.GuiSavestateIngameMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class SavestateEvents {
	public static boolean savestatepauseenabled;
	@SubscribeEvent
	public void GuiOpen(GuiOpenEvent ev) {
		if(ev.getGui() instanceof GuiIngameMenu) {
			if(savestatepauseenabled) {
				ev.setCanceled(true);
				Minecraft.getMinecraft().displayGuiScreen(new GuiSavestateIngameMenu());
			}
		}else if(ev.getGui() instanceof GuiGameOver) {
			if (savestatepauseenabled) {
				ev.setCanceled(true);
				Minecraft.getMinecraft().displayGuiScreen(new GuiSavestateDeathReloadScreen(null));
			}
		}
	}
	@SubscribeEvent
	public void pressKeybinding(InputEvent.KeyInputEvent ev){
		if (ClientProxy.SavestateSaveKey.isPressed()) {
			if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
				ModLoader.NETWORK.sendToServer(new SavestatePacket(true));
			}else {
				ModLoader.NETWORK.sendToServer(new SavestatePacket(true));
			}
		}
		if (ClientProxy.SavestateLoadKey.isPressed()) {
			if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
				ModLoader.NETWORK.sendToServer(new SavestatePacket(false));
			}else {
				ModLoader.NETWORK.sendToServer(new SavestatePacket(false));
			}
		}
		if (ClientProxy.TestingKey.isPressed()) {
			//Minecraft.getMinecraft().displayGuiScreen(new GuiSavestateDeathReloadScreen(null));
		}
	}
}
