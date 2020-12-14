package de.scribble.lp.TASTools.flintrig;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ZombieDrops {
	public static boolean enabled=false;
	@SubscribeEvent
	public void dropIron(LivingDeathEvent ev) {
		if(enabled==false) return;
		if((ev.getEntity() instanceof EntityZombie)||(ev.getEntity() instanceof EntityZombieVillager)) {
			if(ev.getSource().getTrueSource()instanceof EntityPlayer) {
				if(!ev.getEntity().getEntityWorld().isRemote) {
					ev.getEntity().dropItem(Items.IRON_INGOT, 1);
				}
			}
		}
	}
}
