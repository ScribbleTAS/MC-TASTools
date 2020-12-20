package de.scribble.lp.TASTools.velocity;

import de.scribble.lp.TASTools.freeze.FreezeHandlerVer2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class VelocityEvents {
	public void onCloseServer(PlayerLoggedOutEvent ev) {
		EntityPlayer player=ev.player;
		MinecraftServer server=player.getServer();
		
		if(!server.isDedicatedServer()) {
			IntegratedServer iserver=(IntegratedServer) server;
			if(!iserver.getPublic()) { //Singleplayer
				//player.
			}
		}
	}
}
