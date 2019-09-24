package de.scribble.lp.TASTools.enderdragon;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.scribble.lp.TASTools.CommonProxy;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class DragonEvents {

	@SubscribeEvent
	public void displayDamageinConsole(LivingHurtEvent ev) {
		if (ev.entity instanceof EntityDragon) {
			CommonProxy.logger.info("DragonDamage: "+ev.ammount);
		}
	}
}
