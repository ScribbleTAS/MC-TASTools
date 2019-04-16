package de.scribble.lp.TASTools.freeze;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.common.graph.Network;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.proxy.ClientProxy;
import de.scribble.lp.TASTools.velocity.ReapplyingVelocity;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class FreezeEvents {
	@SubscribeEvent
	public void onjoinServer(PlayerLoggedInEvent ev) {
		List<EntityPlayerMP> playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
				.getPlayers();
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			if (FreezeHandler.isServerFrozen()) {

	
				if (playerMP.size() > 0) {
					FreezeHandler.entity= new ArrayList<EntityDataStuff>();
					if(VelocityEvents.velocityenabledServer) {
						for (int i = 0; i < (playerMP.size()); i++) {
							
							File file= new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath() + File.separator + ev.player.getEntityWorld().getWorldInfo().getWorldName()+File.separator
									+ playerMP.get(i).getName() + "_velocity.txt");
							
							if (file.exists()) {
								double [] bewegung=new ReapplyingVelocity().getVelocity(file); 	//German for motion lol
								FreezeHandler.entity.add(new EntityDataStuff(playerMP.get(i).getName(), playerMP.get(i).posX, playerMP.get(i).posY, playerMP.get(i).posZ,
												playerMP.get(i).rotationPitch, playerMP.get(i).rotationYaw, bewegung[0],
												bewegung[1], bewegung[2]));
								file.delete();
							}else {
								FreezeHandler.entity.add(new EntityDataStuff(playerMP.get(i).getName(), playerMP.get(i).posX, playerMP.get(i).posY, playerMP.get(i).posZ,
												playerMP.get(i).rotationPitch, playerMP.get(i).rotationYaw, playerMP.get(i).motionX,
												playerMP.get(i).motionY, playerMP.get(i).motionZ));
							}
							playerMP.get(i).setEntityInvulnerable(true);
							playerMP.get(i).setNoGravity(true);
						}
					}else {
						for (int i = 0; i < (playerMP.size()); i++) {
							FreezeHandler.entity.add(new EntityDataStuff(playerMP.get(i).getName(), playerMP.get(i).posX, playerMP.get(i).posY, playerMP.get(i).posZ,
											playerMP.get(i).rotationPitch, playerMP.get(i).rotationYaw, playerMP.get(i).motionX,
											playerMP.get(i).motionY, playerMP.get(i).motionZ));
							
							playerMP.get(i).setEntityInvulnerable(true);
							playerMP.get(i).setNoGravity(true);
						}
					}
					FreezeHandler.playerMP = playerMP;
					ModLoader.NETWORK.sendToAll(new FreezePacket(true));
				}
			}
		}else {
			if(ModLoader.freezeenabledSP) {
				if(playerMP.size()>1) {
					FreezeHandler.entity= new ArrayList<EntityDataStuff>();
					if (FreezeHandler.isServerFrozen()) {
						for (int i = 0; i < (playerMP.size()); i++) {
							
							File file= new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath() + File.separator + ev.player.getEntityWorld().getWorldInfo().getWorldName()+File.separator
									+ playerMP.get(i).getName() + "_velocity.txt");
							
							if (file.exists()) {
								double [] bewegung=new ReapplyingVelocity().getVelocity(file); 	//German for motion lol
								FreezeHandler.entity.add(new EntityDataStuff(playerMP.get(i).getName(), playerMP.get(i).posX, playerMP.get(i).posY, playerMP.get(i).posZ,
												playerMP.get(i).rotationPitch, playerMP.get(i).rotationYaw, bewegung[0],
												bewegung[1], bewegung[2]));
								file.delete();
							}else {
								FreezeHandler.entity.add(new EntityDataStuff(playerMP.get(i).getName(), playerMP.get(i).posX, playerMP.get(i).posY, playerMP.get(i).posZ,
												playerMP.get(i).rotationPitch, playerMP.get(i).rotationYaw, playerMP.get(i).motionX,
												playerMP.get(i).motionY, playerMP.get(i).motionZ));
							}
							playerMP.get(i).setEntityInvulnerable(true);
							playerMP.get(i).setNoGravity(true);
							ModLoader.NETWORK.sendTo(new FreezePacket(true), (EntityPlayerMP) ev.player);
						}
					}
				}else {
					EntityPlayerMP playerEv=(EntityPlayerMP) ev.player;
					if (VelocityEvents.velocityenabledClient) {
						File file= new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator +Minecraft.getMinecraft().getIntegratedServer().getFolderName()+File.separator+"latest_velocity.txt");
						if (file.exists()){
							double [] motion = new ReapplyingVelocity().getVelocity(file);
							FreezeHandler.startFreezeSetMotionServer(motion[0], motion[1], motion[2]);
						}else FreezeHandler.startFreezeServer();
					}else {
						FreezeHandler.startFreezeServer();
					}
					
					ModLoader.NETWORK.sendToAll(new FreezePacket(true));
				}
			}
		}
	}

	@SubscribeEvent
	public void onLeaveServer(PlayerLoggedOutEvent ev) {
		List<EntityPlayerMP> playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			if (FreezeHandler.isServerFrozen()) {
	
				if (playerMP.size() > 0) {
					FreezeHandler.entity= new ArrayList<EntityDataStuff>();
					if (VelocityEvents.velocityenabledServer) {
						for (int i = 0; i < (playerMP.size()); i++) {

							File file = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath()
									+ File.separator + ev.player.getEntityWorld().getWorldInfo().getWorldName()
									+ File.separator + playerMP.get(i).getName() + "_velocity.txt");
							if (file.exists()) {
								double[] bewegung = new ReapplyingVelocity().getVelocity(file); // German for motion lol
								FreezeHandler.entity.add(new EntityDataStuff(playerMP.get(i).getName(), playerMP.get(i).posX, playerMP.get(i).posY,
												playerMP.get(i).posZ, playerMP.get(i).rotationPitch,
												playerMP.get(i).rotationYaw, bewegung[0], bewegung[1], bewegung[2]));
							} else {
								FreezeHandler.entity.add(new EntityDataStuff(playerMP.get(i).getName(), playerMP.get(i).posX, playerMP.get(i).posY,
												playerMP.get(i).posZ, playerMP.get(i).rotationPitch,
												playerMP.get(i).rotationYaw, playerMP.get(i).motionX,
												playerMP.get(i).motionY, playerMP.get(i).motionZ));
							}
							playerMP.get(i).setEntityInvulnerable(true);
							playerMP.get(i).setNoGravity(true);
						}
					} else {
						for (int i = 0; i < (playerMP.size()); i++) {
							FreezeHandler.entity.add(new EntityDataStuff(playerMP.get(i).getName(), playerMP.get(i).posX, playerMP.get(i).posY,
									playerMP.get(i).posZ, playerMP.get(i).rotationPitch, playerMP.get(i).rotationYaw,
									playerMP.get(i).motionX, playerMP.get(i).motionY, playerMP.get(i).motionZ));

							playerMP.get(i).setEntityInvulnerable(true);
							playerMP.get(i).setNoGravity(true);
						}
					}
					FreezeHandler.playerMP = playerMP;
					ModLoader.NETWORK.sendToAll(new FreezePacket(true));
					
				}
			}
		}else{
			if(playerMP.size()>1) {
				FreezeHandler.entity= new ArrayList<EntityDataStuff>();
				if (FreezeHandler.isServerFrozen()&&FreezeHandler.isClientFrozen()) {
					for (int i = 0; i < (playerMP.size()); i++) {
						
						File file= new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath() + File.separator + ev.player.getEntityWorld().getWorldInfo().getWorldName()+File.separator
								+ playerMP.get(i).getName() + "_velocity.txt");
						
						if (file.exists()) {
							double [] bewegung=new ReapplyingVelocity().getVelocity(file); 	//German for motion lol
							FreezeHandler.entity.add(new EntityDataStuff(playerMP.get(i).getName(), playerMP.get(i).posX, playerMP.get(i).posY, playerMP.get(i).posZ,
											playerMP.get(i).rotationPitch, playerMP.get(i).rotationYaw, bewegung[0],
											bewegung[1], bewegung[2]));
							file.delete();
						}else {
							FreezeHandler.entity.add(i,
									new EntityDataStuff(playerMP.get(i).getName(), playerMP.get(i).posX, playerMP.get(i).posY, playerMP.get(i).posZ,
											playerMP.get(i).rotationPitch, playerMP.get(i).rotationYaw, playerMP.get(i).motionX,
											playerMP.get(i).motionY, playerMP.get(i).motionZ));
						}
						playerMP.get(i).setEntityInvulnerable(true);
						playerMP.get(i).setNoGravity(true);
						
					}
					ModLoader.NETWORK.sendTo(new FreezePacket(false), (EntityPlayerMP) ev.player);
				}
			}else {
				FreezeHandler.entity= new ArrayList<EntityDataStuff>();
				FreezeHandler.stopFreezeServerNoUpdate();
				ModLoader.NETWORK.sendToAll(new FreezePacket(false));
			}

		}
	}
}
