package de.scribble.lp.TASTools.freeze;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FreezeEventsServer {

	public static List<EntityPlayerMP> playerMP;

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent ev) {
		if (playerMP.size() > 0) {
			for (int i = 0; i < playerMP.size(); i++) {
				playerMP.get(i).setPositionAndUpdate(FreezePacketHandler.entity.get(i).getPosX(),
				FreezePacketHandler.entity.get(i).getPosY(), FreezePacketHandler.entity.get(i).getPosZ());

				playerMP.get(i).rotationPitch = FreezePacketHandler.entity.get(i).getPitch();
				playerMP.get(i).prevRotationYaw = FreezePacketHandler.entity.get(i).getYaw();
			}
		}
	}
}
