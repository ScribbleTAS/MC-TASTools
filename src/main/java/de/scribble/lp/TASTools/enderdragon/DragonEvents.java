package de.scribble.lp.TASTools.enderdragon;

import de.scribble.lp.TASTools.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class DragonEvents {
	@SubscribeEvent
	public void onKeyPress(InputEvent.KeyInputEvent ev) {
		if(ClientProxy.DragonKey.isPressed()) {
			Minecraft.getMinecraft().thePlayer.sendChatMessage("/dragon");
		}
	}
}
