package de.scribble.lp.TASTools.freeze;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FreezeEventsClient {
	
	@SubscribeEvent
	public void disableMouse(MouseEvent ev) {
		ev.setCanceled(true);
	}
}
