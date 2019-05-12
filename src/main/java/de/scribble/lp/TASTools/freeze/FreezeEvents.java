package de.scribble.lp.TASTools.freeze;

import java.io.File;
import java.util.List;

import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.velocity.ReapplyingVelocity;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class FreezeEvents {
	private boolean isServerFrozen;
	@SubscribeEvent
	public void onjoinServer(PlayerLoggedInEvent ev) {
		EntityPlayerMP playerev = (EntityPlayerMP) ev.player;
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) { // Multiplayer
			if (FreezeHandler.isServerFrozen()) {

				if (VelocityEvents.velocityenabledServer) { // If velocity in the server config is enabled
					File file = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath()
							+ File.separator + ev.player.getEntityWorld().getWorldInfo().getWorldName() + File.separator
							+ playerev.getName() + "_velocity.txt");

					if (file.exists()) {
						// Apply the motion to the player, instead of his current motion
						double[] bewegung = new ReapplyingVelocity().getVelocity(file); // German for motion lol
						FreezeHandler.entity.add(new EntityDataStuff(playerev.getName(), playerev.posX, playerev.posY,
								playerev.posZ, playerev.rotationPitch, playerev.rotationYaw, bewegung[0], bewegung[1],
								bewegung[2]));
					} else {
						FreezeHandler.entity.add(new EntityDataStuff(playerev.getName(), playerev.posX, playerev.posY,
								playerev.posZ, playerev.rotationPitch, playerev.rotationYaw, playerev.motionX,
								playerev.motionY, playerev.motionZ));
					}
					playerev.capabilities.disableDamage=true;
					playerev.capabilities.isFlying=true;
				} else { // if velocityserver is disabled
					FreezeHandler.entity.add(new EntityDataStuff(playerev.getName(), playerev.posX, playerev.posY,
							playerev.posZ, playerev.rotationPitch, playerev.rotationYaw, 0, 0, 0));

					playerev.capabilities.disableDamage=true;
					playerev.capabilities.isFlying=true;
				}
				ModLoader.NETWORK.sendTo(new FreezePacket(true), playerev);

			}
		}else { // Open to LAN
				if (ModLoader.freezeenabledSP) {
					List<EntityPlayerMP> playerMP = FMLCommonHandler.instance().getMinecraftServerInstance()
							.getConfigurationManager().getPlayerList();
					if (playerMP.size() > 1) {
						if (FreezeHandler.isServerFrozen()) {
							if (VelocityEvents.velocityenabledClient) {
								File file = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath()
										+ File.separator + ev.player.getEntityWorld().getWorldInfo().getWorldName()
										+ File.separator + playerev.getName() + "_velocity.txt");

								if (file.exists()) {
									double[] bewegung = new ReapplyingVelocity().getVelocity(file); // German for motion
																									// lol
									FreezeHandler.entity.add(new EntityDataStuff(playerev.getName(), playerev.posX,
											playerev.posY, playerev.posZ, playerev.rotationPitch, playerev.rotationYaw,
											bewegung[0], bewegung[1], bewegung[2]));
								} else {
									FreezeHandler.entity.add(new EntityDataStuff(playerev.getName(), playerev.posX,
											playerev.posY, playerev.posZ, playerev.rotationPitch, playerev.rotationYaw,
											0, 0, 0));
								}
								playerev.capabilities.disableDamage=true;
								playerev.capabilities.isFlying=true;
								ModLoader.NETWORK.sendTo(new FreezePacket(true), playerev);

							} else { // if velocityclient is disabled

								FreezeHandler.entity.add(new EntityDataStuff(playerev.getName(), playerev.posX,
										playerev.posY, playerev.posZ, playerev.rotationPitch, playerev.rotationYaw,
										playerev.motionX, playerev.motionY, playerev.motionZ));

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
								FreezeHandler.startFreezeSetMotionServer(motion[0], motion[1], motion[2]);
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
		
		List<EntityPlayerMP> playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().getPlayerList();
		
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			if (FreezeHandler.isServerFrozen()) {
				
				for (int o=0; o<FreezeHandler.entity.size();o++) {
					if(FreezeHandler.entity.get(o).getPlayername().equals(playerEV.getName())) {
						FreezeHandler.entity.remove(o);
					}
				}
				playerEV.capabilities.disableDamage=false;
				playerEV.capabilities.isFlying=false;
				ModLoader.NETWORK.sendTo(new FreezePacket(false), playerEV);
			}
		}else{
			if(playerMP.size()>1) {
				if (FreezeHandler.isServerFrozen()) {
					
					for (int o=0; o<FreezeHandler.entity.size();o++) {
						if(FreezeHandler.entity.get(o).getPlayername().equals(playerEV.getName())) {
							FreezeHandler.entity.remove(o);
						}
					}
					playerEV.capabilities.disableDamage=false;
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
	public void pressKeybinding(InputEvent.KeyInputEvent ev) {
		if (ClientProxy.FreezeKey.isPressed() && Minecraft.getMinecraft().thePlayer.canCommandSenderUseCommand(2, "dupe")) {
			isServerFrozen = true;
			ModLoader.NETWORK.sendToServer(new FreezePacket(true,1));
			if (!FreezeHandler.isClientFrozen()) {
				FreezeHandler.startFreezeClient();
			} else
				FreezeHandler.stopFreezeClient();

		}
	}
}
