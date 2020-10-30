package de.scribble.lp.TASTools.freeze;

import java.io.File;
import java.util.List;

import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.velocity.ReapplyingVelocity;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.NameFormat;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FreezeEvents {
	@SubscribeEvent
	public void onjoinServer(PlayerLoggedInEvent ev) {
		EntityPlayerMP playerev = (EntityPlayerMP) ev.player;
		ReapplyingVelocity velo = new ReapplyingVelocity();
		
		/*======================================= Multiplayer =======================================*/
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) { 
			if (FreezeHandler.isServerFrozen()) {
				if (VelocityEvents.velocityenabledServer) { // If velocity in the server config is enabled
					File file = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath()
							+ File.separator + playerev.getEntityWorld().getWorldInfo().getWorldName() + File.separator
							+ playerev.getName() + "_velocity.txt");
					
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
				if(!playerev.isSpectator()&&!playerev.capabilities.isCreativeMode) {
					playerev.capabilities.disableDamage=false;
				}
			}
		/*======================================= Open to LAN =======================================*/
		}else {
			List<EntityPlayerMP> playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerList();
			if (playerMP.size() > 1) {
				if (FreezeHandler.isServerFrozen()) {
					if (VelocityEvents.velocityenabledClient) {
						File file = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath()
								+ File.separator + ev.player.getEntityWorld().getWorldInfo().getWorldName()
								+ File.separator + playerev.getName() + "_velocity.txt");

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
		
		List<EntityPlayerMP> playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerList();
		//Multiplayer
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			if (FreezeHandler.isServerFrozen()) {
				
				removePlayerFromFreezeClock(playerEV);
				ModLoader.NETWORK.sendTo(new FreezePacket(false), playerEV);
			}
		//LAN
		}else{
			if(playerMP.size()>1) {
				if (FreezeHandler.isServerFrozen()) {
					removePlayerFromFreezeClock(playerEV);
					ModLoader.NETWORK.sendTo(new FreezePacket(false), playerEV);
				}
			}else {
				FreezeHandler.stopFreezeServerNoUpdate();
				ModLoader.NETWORK.sendTo(new FreezePacket(false),playerEV);
			}

		}
	}

	@SubscribeEvent
	public void pressKeybinding(InputEvent.KeyInputEvent ev) {
		Minecraft mc = Minecraft.getMinecraft();
		if (ClientProxy.FreezeKey.isPressed()) {
			ModLoader.NETWORK.sendToServer(new FreezePacket(true, 1));
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
		FreezeHandler.entity.add(new EntityDataStuff(player.getName(), player.posX, player.posY,
				player.posZ, player.rotationPitch, player.rotationYaw, 0, 0, 0, 0));

		player.capabilities.disableDamage=true;
	}
	/**
	 * Add a player + motion to the freeze clock
	 * @param player
	 * @param customMotion [0]=X, [1]=Y, [2]=Z
	 */
	public void addPlayerToFreezeClock(EntityPlayerMP player, double[] customMotion, float falldistance) {
		FreezeHandler.entity.add(new EntityDataStuff(player.getName(), player.posX, player.posY,
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
			if(FreezeHandler.entity.get(o).getPlayername().equals(player.getName())) {
				FreezeHandler.entity.remove(o);
			}
		}
		if(!player.capabilities.isCreativeMode||!player.isSpectator()) {
			player.capabilities.disableDamage=false;
		}
	}
	@SubscribeEvent
	public void onPlayerCreated(NameFormat ev) {
		if(!ev.username.equals("TASBot")) {
			ev.displayname="[TAS]"+" "+ev.username;
		}
	}
}
