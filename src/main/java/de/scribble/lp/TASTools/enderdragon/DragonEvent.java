package de.scribble.lp.TASTools.enderdragon;



import java.util.List;

import de.scribble.lp.TASTools.CommonProxy;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;

public class DragonEvent {
	private static List<EntityDragon> dragonlist;
	private static boolean dragonloaded=false;
	@SubscribeEvent
	public void onDimensionChange(PlayerChangedDimensionEvent ev) {
		if(ev.toDim==-1) {
			try {
				
			}catch(NullPointerException e) {
				CommonProxy.logger.error("Couldn't find a dragon :(");
			}
			dragonloaded=true;
		}
	}
	public static List<EntityDragon> getDragonlist() {
		return dragonlist;
	}
	
	public static boolean isDragonloaded() {
		return dragonloaded;
	}
}
