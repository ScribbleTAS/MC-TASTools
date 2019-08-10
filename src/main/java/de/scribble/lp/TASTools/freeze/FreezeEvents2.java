package de.scribble.lp.TASTools.freeze;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.MouseEvent;

public class FreezeEvents2 {
	@SubscribeEvent
	public void disableMouse(MouseEvent ev) {
		ev.setCanceled(FreezeHandler.isClientFrozen());
	}
}
