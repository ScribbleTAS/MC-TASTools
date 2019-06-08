package de.scribble.lp.TASTools.enderdragon;

import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class DragonEvents {
	@SubscribeEvent
	public void onKeyPress(InputEvent.KeyInputEvent ev) {
		if(ClientProxy.DragonKey.isPressed()) {
			Minecraft.getMinecraft().thePlayer.sendChatMessage("/dragon");
		}
	}
	@SubscribeEvent
	public void displayDamageinConsole(LivingHurtEvent ev) {
		if (ev.entity instanceof EntityDragon) {
			CommonProxy.logger.info("DragonDamage: "+ev.ammount);
		}
	}
}
