package de.scribble.lp.TASTools.freeze;

import java.util.List;

import de.scribble.lp.TASTools.commands.FreezeCommandc;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class AnotherFreezeEvents {
	@SubscribeEvent
	public void onjoinServer(PlayerLoggedInEvent ev) {
		if (FreezeCommandc.freeze) {
			List<EntityPlayerMP> playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
					.getPlayers();

			if (playerMP.size() > 0) {
				FreezePacketHandler.entity.removeAll(FreezePacketHandler.entity);
				for (int i = 0; i < (playerMP.size()); i++) {
					FreezePacketHandler.entity.add(i,
							new EntityDataStuff(playerMP.get(i).posX, playerMP.get(i).posY, playerMP.get(i).posZ,
									playerMP.get(i).rotationPitch, playerMP.get(i).rotationYaw, playerMP.get(i).motionX,
									playerMP.get(i).motionY, playerMP.get(i).motionZ));
					playerMP.get(i).setEntityInvulnerable(true);
					playerMP.get(i).setNoGravity(true);
				}
				FreezeEventsServer.playerMP = playerMP;
			}
		}
	}

	@SubscribeEvent
	public void onLeaveServer(PlayerLoggedOutEvent ev) {
		if (FreezeCommandc.freeze) {
			List<EntityPlayerMP> playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
					.getPlayers();

			if (playerMP.size() > 0) {
				FreezePacketHandler.entity.removeAll(FreezePacketHandler.entity);
				for (int i = 0; i < (playerMP.size()); i++) {
					FreezePacketHandler.entity.add(i,
							new EntityDataStuff(playerMP.get(i).posX, playerMP.get(i).posY, playerMP.get(i).posZ,
									playerMP.get(i).rotationPitch, playerMP.get(i).rotationYaw, playerMP.get(i).motionX,
									playerMP.get(i).motionY, playerMP.get(i).motionZ));
					playerMP.get(i).setEntityInvulnerable(true);
					playerMP.get(i).setNoGravity(true);
				}
				FreezeEventsServer.playerMP = playerMP;
			}
		}
	}
}
