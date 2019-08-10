package de.scribble.lp.TASTools.freeze;

import java.io.File;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.velocity.ReapplyingVelocity;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public class FreezeEvents {
	@SubscribeEvent
	public void onjoinServer(PlayerLoggedInEvent ev) {
		EntityPlayerMP playerev = (EntityPlayerMP) ev.player;
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) { // Multiplayer
			if (FreezeHandler.isServerFrozen()) {

				if (VelocityEvents.velocityenabledServer) { // If velocity in the server config is enabled
					File file = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath()
							+ File.separator + ev.player.getEntityWorld().getWorldInfo().getWorldName() + File.separator
							+ playerev.getDisplayName() + "_velocity.txt");

					if (file.exists()) {
						// Apply the motion to the player, instead of his current motion
						double[] bewegung = new ReapplyingVelocity().getVelocity(file); // German for motion lol
						float fallstrecke = new ReapplyingVelocity().getFalldistance(file); //falldistance
						FreezeHandler.entity.add(new EntityDataStuff(playerev.getDisplayName(), playerev.posX, playerev.posY,
								playerev.posZ, playerev.rotationPitch, playerev.rotationYaw, bewegung[0], bewegung[1],
								bewegung[2], fallstrecke));
					} else {
						FreezeHandler.entity.add(new EntityDataStuff(playerev.getDisplayName(), playerev.posX, playerev.posY,
								playerev.posZ, playerev.rotationPitch, playerev.rotationYaw, playerev.motionX,
								playerev.motionY, playerev.motionZ, playerev.fallDistance));
					}
					playerev.capabilities.disableDamage=true;
					playerev.capabilities.isFlying=true;
				} else { // if velocityserver is disabled
					FreezeHandler.entity.add(new EntityDataStuff(playerev.getDisplayName(), playerev.posX, playerev.posY,
							playerev.posZ, playerev.rotationPitch, playerev.rotationYaw, 0, 0, 0, 0));

					playerev.capabilities.disableDamage=true;
					playerev.capabilities.isFlying=true;
				}
				ModLoader.NETWORK.sendTo(new FreezePacket(true), playerev);

			}
		}else { // Open to LAN
				if (ModLoader.freezeenabledSP) {
					List<EntityPlayerMP> playerMP = FMLCommonHandler.instance().getMinecraftServerInstance()
							.getConfigurationManager().playerEntityList;
					if (playerMP.size() > 1) {
						if (FreezeHandler.isServerFrozen()) {
							if (VelocityEvents.velocityenabledClient) {
								File file = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath()
										+ File.separator + ev.player.getEntityWorld().getWorldInfo().getWorldName()
										+ File.separator + playerev.getDisplayName() + "_velocity.txt");

								if (file.exists()) {
									double[] bewegung = new ReapplyingVelocity().getVelocity(file); // German for motion lol
									float fallstrecke = new ReapplyingVelocity().getFalldistance(file); //falldistance
									FreezeHandler.entity.add(new EntityDataStuff(playerev.getDisplayName(), playerev.posX,
											playerev.posY, playerev.posZ, playerev.rotationPitch, playerev.rotationYaw,
											bewegung[0], bewegung[1], bewegung[2], fallstrecke));
								} else {
									FreezeHandler.entity.add(new EntityDataStuff(playerev.getDisplayName(), playerev.posX,
											playerev.posY, playerev.posZ, playerev.rotationPitch, playerev.rotationYaw,
											0, 0, 0, 0));
								}
								playerev.capabilities.disableDamage=true;
								playerev.capabilities.isFlying=true;
								ModLoader.NETWORK.sendTo(new FreezePacket(true), playerev);

							} else { // if velocityclient is disabled

								FreezeHandler.entity.add(new EntityDataStuff(playerev.getDisplayName(), playerev.posX,
										playerev.posY, playerev.posZ, playerev.rotationPitch, playerev.rotationYaw,
										playerev.motionX, playerev.motionY, playerev.motionZ, playerev.fallDistance));

								playerev.capabilities.disableDamage=true;
								playerev.capabilities.isFlying=true;
								ModLoader.NETWORK.sendTo(new FreezePacket(true), playerev);
							}
						}
					} else { // Singleplayer
						if (VelocityEvents.velocityenabledClient) {
							File file = new File(Minecraft.getMinecraft().mcDataDir,
									"saves" + File.separator
											+ Minecraft.getMinecraft().getIntegratedServer().getFolderName()
											+ File.separator + "latest_velocity.txt");
							if (file.exists()) {
								double[] motion = new ReapplyingVelocity().getVelocity(file);
								float fd = new ReapplyingVelocity().getFalldistance(file);
								FreezeHandler.startFreezeSetMotionServer(motion[0], motion[1], motion[2], fd);
							} else
								FreezeHandler.startFreezeServer();
						} else {
							FreezeHandler.startFreezeServer();
						}
						ModLoader.NETWORK.sendTo(new FreezePacket(true), playerev);
					}
				}
			}
		}

	@SubscribeEvent
	public void onLeaveServer(PlayerLoggedOutEvent ev) {
		EntityPlayerMP playerEV = (EntityPlayerMP) ev.player;
		
		List<EntityPlayerMP> playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList;
		
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			if (FreezeHandler.isServerFrozen()) {
				
				for (int o=0; o<FreezeHandler.entity.size();o++) {
					if(FreezeHandler.entity.get(o).getPlayername().equals(playerEV.getDisplayName())) {
						FreezeHandler.entity.remove(o);
					}
				}
				if(!playerEV.capabilities.isCreativeMode) {
					playerEV.capabilities.disableDamage=false;
				}
				playerEV.capabilities.isFlying=false;
				ModLoader.NETWORK.sendTo(new FreezePacket(false), playerEV);
			}
		}else{
			if(playerMP.size()>1) {
				if (FreezeHandler.isServerFrozen()) {
					
					for (int o=0; o<FreezeHandler.entity.size();o++) {
						if(FreezeHandler.entity.get(o).getPlayername().equals(playerEV.getDisplayName())) {
							FreezeHandler.entity.remove(o);
						}
					}
					if(!playerEV.capabilities.isCreativeMode) {
						playerEV.capabilities.disableDamage=false;
					}
					playerEV.capabilities.isFlying=false;
					ModLoader.NETWORK.sendTo(new FreezePacket(false), playerEV);
				}
			}else {
				FreezeHandler.stopFreezeServerNoUpdate();
				ModLoader.NETWORK.sendTo(new FreezePacket(false),playerEV);
			}

		}
	}
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent ev) {
		if (FreezeHandler.isServerFrozen()){
			if (FreezeHandler.playerMP.size() > 0) {
				for (int i = 0; i < FreezeHandler.playerMP.size(); i++) {
						if (FreezeHandler.playerMP.get(i).getDisplayName().equals(FreezeHandler.entity.get(i).getPlayername())) {
							FreezeHandler.playerMP.get(i).setPositionAndUpdate(FreezeHandler.entity.get(i).getPosX(), FreezeHandler.entity.get(i).getPosY(),
									FreezeHandler.entity.get(i).getPosZ());
	
							FreezeHandler.playerMP.get(i).rotationPitch = FreezeHandler.entity.get(i).getPitch();
							FreezeHandler.playerMP.get(i).rotationYaw = FreezeHandler.entity.get(i).getYaw();
					}
				}
			}
		}
	}
	@SubscribeEvent
	public void disableFalldamage(LivingFallEvent ev) {
		if (FreezeHandler.isClientFrozen()){
			if (ev.entityLiving instanceof EntityPlayerMP) {
				ev.setCanceled(true);
			}
		}
	}
}
