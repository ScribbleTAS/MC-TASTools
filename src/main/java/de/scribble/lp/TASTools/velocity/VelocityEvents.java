package de.scribble.lp.TASTools.velocity;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerDisconnectionFromClientEvent;

public class VelocityEvents {
	public static boolean velocityenabledClient;
	public static boolean velocityenabledServer;
	public static boolean stopit=true;


		
	//Dedicated Multiplayer (Moved to FreezeEvents

	@SubscribeEvent
	public void onOpenServer(PlayerEvent.PlayerLoggedInEvent ev) {
		VelocityEvents.stopit=true;
	}
}
