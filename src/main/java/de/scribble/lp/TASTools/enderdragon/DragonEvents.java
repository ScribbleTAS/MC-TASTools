package de.scribble.lp.TASTools.enderdragon;

import de.scribble.lp.TASTools.CommonProxy;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DragonEvents {
	@SubscribeEvent
	public void displayDamageinConsole(LivingHurtEvent ev) {
		if (ev.getEntity() instanceof EntityDragon) {
			CommonProxy.logger.info("DragonDamage: "+ev.getAmount());
		}
	}
}
