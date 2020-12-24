package de.scribble.lp.TASTools.freezeV2;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FreezeHandlerServer {
	private static Map<String, MotionSaverServer> playerMotion= Maps.<String, MotionSaverServer>newHashMap();
	private static boolean enabled;
	
	public static boolean isEnabled() {
		return enabled;
	}
	public static void activate(boolean enable) {
		enabled=enable;
	}
	public static void add(EntityPlayerMP player, double motionSavedX, double motionSavedY, double motionSavedZ, float relSavedX, float relSavedY, float relSavedZ) {
		MinecraftServer server = player.getServer();
		if(!server.isDedicatedServer()) {
			if(server.getPlayerList().getPlayers().get(0).getName().equalsIgnoreCase(player.getName())) {
				MotionSaverServer saver= new MotionSaverServer("singleplayer", motionSavedX, motionSavedY, motionSavedZ, relSavedX, relSavedY, relSavedZ, player.fallDistance);
				playerMotion.put("singleplayer", saver);
			}else {
				MotionSaverServer saver= new MotionSaverServer(player.getName(), motionSavedX, motionSavedY, motionSavedZ, relSavedX, relSavedY, relSavedZ, player.fallDistance);
				playerMotion.put(player.getName(), saver);
			}
		}else {
			MotionSaverServer saver= new MotionSaverServer(player.getName(), motionSavedX, motionSavedY, motionSavedZ, relSavedX, relSavedY, relSavedZ, player.fallDistance);
			playerMotion.put(player.getName(), saver);
		}
	}
	public static void clear(){
		playerMotion.clear();
	}
	public static MotionSaverServer get(String name) {
		return playerMotion.get(name);
	}
}
