package de.scribble.lp.TASTools.savestates;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.savestates.gui.GuiSavestateIngameMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.client.event.GuiOpenEvent;

public class SavestateEvents {
	public static boolean savestatepauseenabled;
	@SubscribeEvent
	public void GuiOpen(GuiOpenEvent ev) {
		if(ev.gui instanceof GuiIngameMenu) {
			if(savestatepauseenabled) {
				ev.setCanceled(true);
				Minecraft.getMinecraft().displayGuiScreen(new GuiSavestateIngameMenu());
			}
		}
	}
	@SubscribeEvent
	public void pressKeybinding(InputEvent.KeyInputEvent ev){
		if (ClientProxy.SavestateSaveKey.isPressed()) {
			ModLoader.NETWORK.sendToServer(new SavestatePacket(true));
		}
		if (ClientProxy.SavestateLoadKey.isPressed()) {
			ModLoader.NETWORK.sendToServer(new SavestatePacket(false));
		}
	}
}
