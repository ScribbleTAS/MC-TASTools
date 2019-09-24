package de.scribble.lp.TASTools.freeze;

import java.io.File;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.velocity.ReapplyingVelocity;
import de.scribble.lp.TASTools.velocity.SavingVelocity;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public class FreezeEvents {

	@SubscribeEvent
	public void onjoinServer(PlayerLoggedInEvent ev) {
		FreezeEvents3.unloadstop=true;
		EntityPlayerMP playerev = (EntityPlayerMP) ev.player;
		ReapplyingVelocity velo = new ReapplyingVelocity();
		/*======================================= Multiplayer =======================================*/
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) { 
			if (FreezeHandler.isServerFrozen()) {
				if (VelocityEvents.velocityenabledServer) { // If velocity in the server config is enabled
					File file = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath()
							+ File.separator + playerev.getEntityWorld().getWorldInfo().getWorldName() + File.separator
							+ playerev.getDisplayName() + "_velocity.txt");
					
					if (file.exists()) {
						// Apply the motion to the player, instead of his current motion
						double[] bewegung = velo.getVelocity(file); // German for motion lol
						float falldistance = velo.getFalldistance(file);
						addPlayerToFreezeClock(playerev, bewegung, falldistance);
					} else {
						addPlayerToFreezeClock(playerev);
					}
				} else { // if velocityserver is disabled
					addPlayerToFreezeClock(playerev);
				}
				//This will disable the mouse on the client
				ModLoader.NETWORK.sendTo(new FreezePacket(true), playerev);

			}else { //Due to a strange bug, invulnerability and no gravity will get carried over even tho the server is shut down...
				if(!playerev.capabilities.isCreativeMode) {
					playerev.capabilities.disableDamage=false;
				}
			}
		/*======================================= Open to LAN =======================================*/
		}else {
			List<EntityPlayerMP> playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList;
			if (playerMP.size() > 1) {
				if (FreezeHandler.isServerFrozen()) {
					if (VelocityEvents.velocityenabledClient) {
						File file = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath()
								+ File.separator + ev.player.getEntityWorld().getWorldInfo().getWorldName()
								+ File.separator + playerev.getDisplayName() + "_velocity.txt");

						if (file.exists()) {
							double[] bewegung = velo.getVelocity(file); // German for motion lol
							float falldistance = velo.getFalldistance(file);
							addPlayerToFreezeClock(playerev, bewegung, falldistance);
						} else {
							addPlayerToFreezeClock(playerev);
						}
						//This will disable the mouse on the client
						ModLoader.NETWORK.sendTo(new FreezePacket(true), playerev);

					} else { // if velocityclient is disabled

						addPlayerToFreezeClock(playerev);
						//This will disable the mouse on the client
						ModLoader.NETWORK.sendTo(new FreezePacket(true), playerev);
					}
				}
		/*====================================== Singleplayer =======================================*/
			} else {
				if(ModLoader.freezeenabledSP) {
					if (VelocityEvents.velocityenabledClient) {
						File file = new File(Minecraft.getMinecraft().mcDataDir,
								"saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName()
										+ File.separator + "latest_velocity.txt");
						if (file.exists()) {
							double[] motion = velo.getVelocity(file);
							float falldistance = velo.getFalldistance(file);
							FreezeHandler.startFreezeSetMotionServer(motion[0], motion[1], motion[2], falldistance);
						} else
							FreezeHandler.startFreezeServer();
					} else {
						FreezeHandler.startFreezeServer();
					}
					ModLoader.NETWORK.sendTo(new FreezePacket(true), playerev);
				}
			}
		/*===========================================================================================*/
		}
		}

	@SubscribeEvent
	public void onLeaveServer(PlayerLoggedOutEvent ev) {
		EntityPlayerMP playerEV = (EntityPlayerMP) ev.player;
		CommonProxy.logger.debug("PLO Event triggered in Freeze Events");
		List<EntityPlayerMP> playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList;
			
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			
			//moved here from VelocityEvents to here to avoid race conditions
			if(VelocityEvents.velocityenabledServer) {
				File file = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath() + File.separator + ModLoader.getLevelname() +File.separator
						+ ev.player.getDisplayName() + "_velocity.txt");
				List<EntityPlayerMP> players= FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList;
				CommonProxy.logger.info("Saving velocity of "+ev.player.getDisplayName());
				if(FreezeHandler.isServerFrozen()) {
					for(int i=0;i<players.size();i++) {
						if(FreezeHandler.entity.get(i).getPlayername().equals(ev.player.getDisplayName())) {
							new SavingVelocity().saveVelocityCustom(FreezeHandler.entity.get(i).getMotionX(), FreezeHandler.entity.get(i).getMotionY(), FreezeHandler.entity.get(i).getMotionZ(), FreezeHandler.entity.get(0).getFalldistance(), file);
						}
					}
				}
				else {
					new SavingVelocity().saveVelocity(ev.player, file);
				}
			}
		
			if (FreezeHandler.isServerFrozen()) {
				
				for (int o=0; o<FreezeHandler.entity.size();o++) {
					if(FreezeHandler.entity.get(o).getPlayername().equals(playerEV.getDisplayName())) {
						FreezeHandler.entity.remove(o);
					}
				}
				if(!playerEV.capabilities.isCreativeMode) {
					playerEV.capabilities.disableDamage=false;
				}
				ModLoader.NETWORK.sendTo(new FreezePacket(false), playerEV);
			}
		}else{
			if(playerMP.size()>1) {
				if (FreezeHandler.isServerFrozen()) {
					
					removePlayerFromFreezeClock(playerEV);
					if(!playerEV.capabilities.isCreativeMode) {
						playerEV.capabilities.disableDamage=false;
					}
					ModLoader.NETWORK.sendTo(new FreezePacket(false), playerEV);
				}
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
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onMenus(GuiOpenEvent ev) {
		if(ev.gui instanceof GuiMainMenu||ev.gui instanceof GuiMultiplayer) {
			if(FreezeHandler.isClientFrozen()) {
				CommonProxy.logger.info("Unfreezing the mouse");
				FreezeHandler.stopFreezeClient();
			}
		}
	}
	/**
	 * Add a player to the freezeclock
	 * @param player
	 */
	public void addPlayerToFreezeClock(EntityPlayerMP player) {
		FreezeHandler.entity.add(new EntityDataStuff(player.getDisplayName(), player.posX, player.posY,
				player.posZ, player.rotationPitch, player.rotationYaw, 0, 0, 0, 0));

		player.capabilities.disableDamage=true;
	}
	/**
	 * Add a player + motion to the freeze clock
	 * @param player
	 * @param customMotion [0]=X, [1]=Y, [2]=Z
	 */
	public void addPlayerToFreezeClock(EntityPlayerMP player, double[] customMotion, float falldistance) {
		FreezeHandler.entity.add(new EntityDataStuff(player.getDisplayName(), player.posX, player.posY,
				player.posZ, player.rotationPitch, player.rotationYaw, customMotion[0], customMotion[1],
				customMotion[2], falldistance));
		player.capabilities.disableDamage=true;
	}
	/**
	 * Remove a player from the freeze clock
	 * @param player
	 */
	public void removePlayerFromFreezeClock(EntityPlayerMP player) {
		for (int o=0; o<FreezeHandler.entity.size();o++) {
			if(FreezeHandler.entity.get(o).getPlayername().equals(player.getDisplayName())) {
				FreezeHandler.entity.remove(o);
			}
		}
		if(!player.capabilities.isCreativeMode) {
			player.capabilities.disableDamage=false;
		}
	}
}
