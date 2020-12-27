package de.scribble.lp.TASTools.freezeV2;

import java.util.Map;

import com.google.common.collect.Maps;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.velocityV2.RequestVelocityPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class FreezeHandlerServer {
	private static Map<String, MotionSaverServer> playerMotion= Maps.<String, MotionSaverServer>newHashMap();
	private static boolean enabled;
	public static boolean once1=false;
	public static boolean once2=false;
	
	private static int retrycooldown=60;
	
	public static boolean isEnabled() {
		return enabled;
	}
	public static void activate(boolean enable) {
		if(enable) {
			FreezeHandlerServer.clear();
			once1=once2=false;
		}
		enabled=enable;
	}
	public static void add(EntityPlayerMP player, double motionSavedX, double motionSavedY, double motionSavedZ, float relSavedX, float relSavedY, float relSavedZ, float pitch, float yaw) {
		MinecraftServer server = player.getServer();
		if(!server.isDedicatedServer()) {
			if(server.getPlayerList().getPlayers().get(0).getName().equalsIgnoreCase(player.getName())) {
				MotionSaverServer saver= new MotionSaverServer("singleplayer", motionSavedX, motionSavedY, motionSavedZ, relSavedX, relSavedY, relSavedZ, player.fallDistance, pitch, yaw);
				playerMotion.put("singleplayer", saver);
			}else {
				MotionSaverServer saver= new MotionSaverServer(player.getName(), motionSavedX, motionSavedY, motionSavedZ, relSavedX, relSavedY, relSavedZ, player.fallDistance, pitch, yaw);
				playerMotion.put(player.getName(), saver);
			}
		}else {
			MotionSaverServer saver= new MotionSaverServer(player.getName(), motionSavedX, motionSavedY, motionSavedZ, relSavedX, relSavedY, relSavedZ, player.fallDistance, pitch, yaw);
			playerMotion.put(player.getName(), saver);
		}
	}
	public static void clear(){
		playerMotion.clear();
	}
	public static MotionSaverServer get(String name) {
		return playerMotion.get(name);
	}
	public static boolean isEveryMotionSaved() {
		boolean out =FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().size()==playerMotion.size();
		if(!out) {
			if(retrycooldown==0) {
				ModLoader.NETWORK.sendToAll(new RequestVelocityPacket());
				retrycooldown=60;
			}
			retrycooldown--;
		}
		return out;
	}
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent ev) {
		MinecraftServer server=FMLCommonHandler.instance().getMinecraftServerInstance();
		if(server.isDedicatedServer()||server.getPlayerList().getPlayers().size()>1) {
			if(enabled) {
				if(!isEveryMotionSaved()) {
					
					if(!once1) {
						server.getPlayerList().sendMessage(new TextComponentString(TextFormatting.RED+"Saving motion, don't disconnect!"));
						once1=true;
					}
					
				}else {
					if(!once2) {
						server.getPlayerList().sendMessage(new TextComponentString(TextFormatting.GREEN+"Motion is now saved, it's safe to disconnect!"));
						once2=true;
					}
				}
			}
		}
	}
}
