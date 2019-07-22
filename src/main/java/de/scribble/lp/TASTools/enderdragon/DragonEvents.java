package de.scribble.lp.TASTools.enderdragon;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class DragonEvents {
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
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
