package de.scribble.lp.TASTools.freeze;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FreezeHandler {
	
	static ApplyFreezeServer FreezerSE = new ApplyFreezeServer();
	static ApplyFreezeClient FreezerCL = new ApplyFreezeClient();
	public static ArrayList<EntityDataStuff> entity = new ArrayList<EntityDataStuff>();
	
	public static List<EntityPlayerMP> playerMP;

	/**
	 * Indicates Server side freezing. Mouse Input freezing is indicated by "clientfrozen"
	 */
	private static boolean serverfrozen;
	/**
	 * Client isn't actually frozen, the server handles freezing... this is just to show that the mouse is diabled
	 */
	private static boolean clientfrozen;
	
	public static void startFreezeServer() {
		serverfrozen=true;
		List<EntityPlayerMP> playerTemp = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
				.getPlayers();

		if (playerTemp.size() > 0) {
			entity=new ArrayList<EntityDataStuff>();
			for (int i = 0; i < (playerTemp.size()); i++) {
				entity.add(i, new EntityDataStuff(playerTemp.get(i).getName(), playerTemp.get(i).posX, playerTemp.get(i).posY, playerTemp.get(i).posZ,
								playerTemp.get(i).rotationPitch, playerTemp.get(i).rotationYaw,
								playerTemp.get(i).motionX, playerTemp.get(i).motionY, playerTemp.get(i).motionZ));
				playerTemp.get(i).setEntityInvulnerable(true);
				playerTemp.get(i).setNoGravity(true);
			}
		}
		playerMP = playerTemp;
		MinecraftForge.EVENT_BUS.register(FreezerSE);
	}
	public static void startFreezeSetMotionServer(double X, double Y, double Z) {
		serverfrozen=true;
		List<EntityPlayerMP> playerTemp2 = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
				.getPlayers();

		if (playerTemp2.size() > 0) {
			entity=new ArrayList<EntityDataStuff>();
			for (int i = 0; i < (playerTemp2.size()); i++) {
				entity.add(i, new EntityDataStuff(playerTemp2.get(i).getName(), playerTemp2.get(i).posX, playerTemp2.get(i).posY, playerTemp2.get(i).posZ,
								playerTemp2.get(i).rotationPitch, playerTemp2.get(i).rotationYaw,
								X, Y, Z));
				playerTemp2.get(i).setEntityInvulnerable(true);
				playerTemp2.get(i).setNoGravity(true);
			}
		}
		playerMP = playerTemp2;
		MinecraftForge.EVENT_BUS.register(FreezerSE);
	}
	
	
	public static void stopFreezeServer() {
		serverfrozen=false;
		List<EntityPlayerMP> playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
				.getPlayers();
		if (playerMP.size() > 0) {
			for (int i = 0; i < (playerMP.size()); i++) {
				playerMP.get(i).setPositionAndUpdate(FreezeHandler.entity.get(i).getPosX(),
						FreezeHandler.entity.get(i).getPosY(), FreezeHandler.entity.get(i).getPosZ());
				playerMP.get(i).rotationPitch = FreezeHandler.entity.get(i).getPitch();
				playerMP.get(i).rotationYaw = FreezeHandler.entity.get(i).getYaw();
				playerMP.get(i).setEntityInvulnerable(false);
				playerMP.get(i).setNoGravity(false);
				playerMP.get(i).motionX = entity.get(i).getMotionX();
				playerMP.get(i).motionY = entity.get(i).getMotionY();
				playerMP.get(i).motionZ = entity.get(i).getMotionZ();
				playerMP.get(i).velocityChanged = true;
			}
		}
		MinecraftForge.EVENT_BUS.unregister(FreezerSE);
	}
	public static void stopFreezeServerNoUpdate() {
		serverfrozen=false;
		MinecraftForge.EVENT_BUS.unregister(FreezerSE);
	}
	
	public static void startFreezeClient() {
		clientfrozen=true;
		MinecraftForge.EVENT_BUS.register(FreezerCL);
	}
	public static void stopFreezeClient() {
		clientfrozen=false;
		MinecraftForge.EVENT_BUS.unregister(FreezerCL);
	}
	public static boolean isServerFrozen() {
		return serverfrozen;
	}
	public static boolean isClientFrozen() {
		return clientfrozen;
	}
}



class ApplyFreezeServer extends FreezeHandler{

	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent ev) {
		if (playerMP.size() > 0) {
			for (int i = 0; i < playerMP.size(); i++) {
				playerMP.get(i).setPositionAndUpdate(entity.get(i).getPosX(), entity.get(i).getPosY(),
						entity.get(i).getPosZ());

				playerMP.get(i).rotationPitch = FreezeHandler.entity.get(i).getPitch();
				playerMP.get(i).prevRotationYaw = FreezeHandler.entity.get(i).getYaw();
			}
		}
	}
}

class ApplyFreezeClient extends FreezeHandler{
	
	@SubscribeEvent
	public void disableMouse(MouseEvent ev) {
		ev.setCanceled(true);
	}
}