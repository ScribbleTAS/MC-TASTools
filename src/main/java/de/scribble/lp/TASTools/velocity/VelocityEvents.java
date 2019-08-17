package de.scribble.lp.TASTools.velocity;


import java.io.File;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerDisconnectionFromClientEvent;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freeze.FreezeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;

public class VelocityEvents {
	public static boolean velocityenabledClient;
	public static boolean velocityenabledServer;
	public static boolean stopit=true;
	public static List<EntityPlayer> playerbefore;


	//Dedicated Multiplayer (Moved to FreezeEvents)

	@SubscribeEvent
	public void onOpenServer(PlayerEvent.PlayerLoggedInEvent ev) {
		VelocityEvents.stopit=true;
		playerbefore=FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList;
	}
}
