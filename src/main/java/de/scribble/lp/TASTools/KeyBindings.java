package de.scribble.lp.TASTools;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import de.scribble.lp.TASTools.freeze.FreezePacket;
import de.scribble.lp.TASTools.savestates.SavestatePacket;
import net.minecraft.client.Minecraft;

public class KeyBindings {
	@SubscribeEvent
	public void pressKeybinding(InputEvent.KeyInputEvent ev) {
		Minecraft mc = Minecraft.getMinecraft();
		if (ClientProxy.FreezeKey.isPressed()) {
			ModLoader.NETWORK.sendToServer(new FreezePacket(true, 1));
		}
		if (ClientProxy.SavestateSaveKey.isPressed()) {
			ModLoader.NETWORK.sendToServer(new SavestatePacket(true));
		}
		if (ClientProxy.SavestateLoadKey.isPressed()) {
			ModLoader.NETWORK.sendToServer(new SavestatePacket(false));
		}
		if(ClientProxy.DragonKey.isPressed()) {
			Minecraft.getMinecraft().thePlayer.sendChatMessage("/dragon");
		}
	}
}
