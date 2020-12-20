package de.scribble.lp.TASTools.velocity;

import java.io.File;
import java.util.List;

import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freeze.FreezeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Deprecated
public class VelocityEventsOld {
	public static boolean velocityenabledClient;
	public static boolean velocityenabledServer;

	@SubscribeEvent
	public void onCloseServer(PlayerEvent.PlayerLoggedOutEvent ev) {
		if (!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			List<EntityPlayerMP> players= FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
			// Singleplayer
			if (players.size() == 1) {
				if (velocityenabledClient) {
					File file = new File(Minecraft.getMinecraft().mcDataDir,
							"saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName()
									+ File.separator + "latest_velocity.txt");
					CommonProxy.logger.info("Velocity: Start saving velocity");
					if (FreezeHandler.isServerFrozen()) {
						new SavingVelocity().saveVelocityCustom(FreezeHandler.entity.get(0).getMotionX(),
								FreezeHandler.entity.get(0).getMotionY(), FreezeHandler.entity.get(0).getMotionZ(),
								file);
					} else {
						new SavingVelocity().saveVelocity(ev.player, file);
					}
				}
			//LAN-Server
			} else if (players.size() > 1) {
				if (velocityenabledClient) {
					File file = new File(Minecraft.getMinecraft().mcDataDir,
							"saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName()
									+ File.separator + ev.player.getName() + "_velocity.txt");
					CommonProxy.logger.info("Velocity: Start saving velocity for " + ev.player.getName() + " (Singleplayer)");
					if (FreezeHandler.isServerFrozen()) {
						for (int i = 0; i < players.size(); i++) {
							if (FreezeHandler.entity.get(i).getPlayername().equals(ev.player.getName())) {
								new SavingVelocity().saveVelocityCustom(FreezeHandler.entity.get(i).getMotionX(),
										FreezeHandler.entity.get(i).getMotionY(),
										FreezeHandler.entity.get(i).getMotionZ(), file);
							}
						}
					} else {
						new SavingVelocity().saveVelocity(ev.player, file);
					}
				}
			}
		//Dedicated Multiplayer
		}else {
			if(velocityenabledServer) {
				File file = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath() + File.separator + ModLoader.getLevelname() +File.separator
						+ ev.player.getName() + "_velocity.txt");
				List<EntityPlayerMP> players= FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
				CommonProxy.logger.info("Velocity: Saving velocity of "+ev.player.getName());
				if(FreezeHandler.isServerFrozen()) {
					for(int i=0;i<players.size();i++) {
						if(FreezeHandler.entity.get(i).getPlayername().equals(ev.player.getName())) {
							new SavingVelocity().saveVelocityCustom(FreezeHandler.entity.get(i).getMotionX(), FreezeHandler.entity.get(i).getMotionY(), FreezeHandler.entity.get(i).getMotionZ(), file);
						}
					}
				}
				else {
					new SavingVelocity().saveVelocity(ev.player, file);
				}
			}
		}

	}


}
