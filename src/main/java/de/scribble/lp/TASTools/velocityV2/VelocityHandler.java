package de.scribble.lp.TASTools.velocityV2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freezeV2.FreezeHandlerServer;
import de.scribble.lp.TASTools.freezeV2.MotionSaverServer;
import de.scribble.lp.TASTools.freezeV2.networking.FreezePacket;
import de.scribble.lp.TASTools.freezeV2.networking.MovementPacket;
import de.scribble.lp.TASTools.misc.FileStuff;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class VelocityHandler {
	public static Map<String,PacketSender> playeracknowledge=Maps.<String,PacketSender>newHashMap();
	@SubscribeEvent
	public void onLogout(PlayerLoggedOutEvent ev) {
		playeracknowledge.remove(ev.player.getName());
		saveMotion((EntityPlayerMP) ev.player);
	}
	@SubscribeEvent
	public void onLogin(PlayerLoggedInEvent ev) {
		EntityPlayerMP player= (EntityPlayerMP) ev.player;
		
		MinecraftServer server=player.getServer();
		ModLoader.NETWORK.sendTo(new FreezePacket(FreezeHandlerServer.isEnabled(), false), player);
		
		MotionSaverServer saver;
		if(!server.isDedicatedServer()) {
			if(server.getPlayerList().getPlayers().get(0).getName().equalsIgnoreCase(player.getName())) {
				File file = new File(Minecraft.getMinecraft().mcDataDir,
						"saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName()
								+ File.separator + "singleplayer" + "_motion.txt");
				if(!file.exists()) {
					CommonProxy.logger.info("File for player"+player.getName()+" doesn't exist. Not applying motion to this player");
					return;
				}
				saver=readFile(file, "singleplayer");
			}else {
				File file = new File(Minecraft.getMinecraft().mcDataDir,
						"saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName()
								+ File.separator + player.getName() + "_motion.txt");
				if(!file.exists()) {
					CommonProxy.logger.info("File for player"+player.getName()+" doesn't exist. Not applying motion to this player");
					return;
				}
				saver=readFile(file, player.getName());
			}
		}else {
			File file = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath() + File.separator + ModLoader.getLevelname() +File.separator
					+ ev.player.getName() + "_motion.txt");
			if(!file.exists()) {
				return;
			}
			saver=readFile(file, player.getName());
		}
//		System.out.println(saver.getMotionSavedX()+" "+saver.getMotionSavedY() +" "+saver.getMotionSavedZ()+" "+ saver.getRelSavedX()+" "+ saver.getRelSavedY()+" "+ saver.getRelSavedZ());
		player.fallDistance=saver.getFalldistance();
		playeracknowledge.put(player.getName(), new PacketSender(player, saver.getMotionSavedX(), saver.getMotionSavedY(), saver.getMotionSavedZ(), saver.getRelSavedX(), saver.getRelSavedY(), saver.getRelSavedZ()));
		playeracknowledge.get(player.getName()).start();
	}
	public static void saveMotion(EntityPlayerMP player){
		MinecraftServer server=player.getServer();
		
		if(!server.isDedicatedServer()) {
			if(server.getPlayerList().getPlayers().get(0).getName().equalsIgnoreCase(player.getName())) {
				File file = new File(Minecraft.getMinecraft().mcDataDir,
						"saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName()
								+ File.separator + "singleplayer" + "_motion.txt");
				saveFile(FreezeHandlerServer.get("singleplayer"), file);
			}else {
				File file = new File(Minecraft.getMinecraft().mcDataDir,
						"saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName()
								+ File.separator + player.getName() + "_motion.txt");
				saveFile(FreezeHandlerServer.get(player.getName()), file);
			}
		}else {
			File file = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath() + File.separator + ModLoader.getLevelname() +File.separator
					+ player.getName() + "_motion.txt");
			saveFile(FreezeHandlerServer.get(player.getName()),file);
		}
	}
	private static void saveFile(MotionSaverServer saver, File file) {
		if(saver==null) {
			CommonProxy.logger.error("Couldn't save file: "+file);
			return;
		}
		StringBuilder output=new StringBuilder();
		String playername=saver.getPlayername();
		
		double x=saver.getMotionSavedX();
		double y=saver.getMotionSavedY();
		double z=saver.getMotionSavedZ();
		
		float rx=saver.getRelSavedX();
		float ry=saver.getRelSavedY();
		float rz=saver.getRelSavedZ();
		
		float fallDistance=saver.getFalldistance();
		
		output.append("#This file was generated by TASTools, the author is ScribbleLP. To prevent this file being generated, check the tastools.cfg\n"
				+ "\n"
				+ "MotionX:"+x+"\n"
				+ "MotionY:"+y+"\n"
				+ "MotionZ:"+z+"\n"
				+ "\n"
				+ "RelMotionX:"+rx+"\n"
				+ "RelMotionY:"+ry+"\n"
				+ "RelMotionZ:"+rz+"\n"
				+ "\n"
				+ "fallDistance:"+fallDistance);
		
		FileStuff.writeThings(output, file, "Saving motion of "+playername);
	}
	public MotionSaverServer readFile(File file, String playername) {
		
		List<String> lines= new ArrayList<String>();
		double x=0;
		double y=0;
		double z=0;
		
		float rx=0;
		float ry=0;
		float rz=0;
		
		float fallDistance=0;
		try {
			lines=FileStuff.readThings(file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		for (String line :lines) {
			if(line.startsWith("MotionX:")) {
				line=line.replace("MotionX:", "");
				x=Double.parseDouble(line);
			}else if(line.startsWith("MotionY:")) {
				line=line.replace("MotionY:", "");
				y=Double.parseDouble(line);
			}else if(line.startsWith("MotionZ:")) {
				line=line.replace("MotionZ:", "");
				z=Double.parseDouble(line);
			}else if(line.startsWith("RelMotionX:")){
				line=line.replace("RelMotionX:", "");
				rx=Float.parseFloat(line);
			}else if(line.startsWith("RelMotionY:")){
				line=line.replace("RelMotionY:", "");
				ry=Float.parseFloat(line);
			}else if(line.startsWith("RelMotionZ:")){
				line=line.replace("RelMotionZ:", "");
				rz=Float.parseFloat(line);
			}else if(line.startsWith("fallDistance:")){
				line=line.replace("fallDistance:", "");
				fallDistance=Float.parseFloat(line);
			}
		}
		return new MotionSaverServer(playername, x, y, z, rx, ry, rz, fallDistance);
	}
	public static void saveAllMotion(MinecraftServer server) {
		List<EntityPlayerMP> players= server.getPlayerList().getPlayers();
		for(EntityPlayerMP player : players) {
			saveMotion(player);
		}
	}
	
	public class PacketSender extends Thread{
		EntityPlayerMP playername;
		boolean breaks=false;
		private double moX;
		private double moY;
		private double moZ;
		private float relX;
		private float relY;
		private float relZ;
		
		public PacketSender(EntityPlayerMP player,double x, double y, double z, float rx, float ry, float rz) {
			this.playername=player;
			this.setName("MotionSenderThread-"+player.getName());
			moX=x;
			moY=y;
			moZ=z;
			
			relX=rx;
			relY=ry;
			relZ=rz;
		}
		@Override
		public void run() {
			while(!breaks) {
				System.out.println(playername.getName());
				ModLoader.NETWORK.sendTo(new MovementPacket(moX, moY, moZ, relX, relY, relZ), playername);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		public void setBreaks(boolean breaks) {
			this.breaks = breaks;
		}
	}
}
