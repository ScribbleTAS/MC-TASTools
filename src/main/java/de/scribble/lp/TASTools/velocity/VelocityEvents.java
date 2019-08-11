package de.scribble.lp.TASTools.velocity;

import java.io.File;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freeze.FreezeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;

public class VelocityEvents {
	public static boolean velocityenabledClient;
	public static boolean velocityenabledServer;

	@SubscribeEvent
	public void onCloseServer(PlayerEvent.PlayerLoggedOutEvent ev) {
		CommonProxy.logger.debug("PLO Event triggered in VelocityEvents");
		if (!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			List<EntityPlayerMP> players= FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList;
			// Singleplayer
			if (players.size() == 1) {
				if (velocityenabledClient) {
					File file = new File(Minecraft.getMinecraft().mcDataDir,
							"saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName()
									+ File.separator + "latest_velocity.txt");
					CommonProxy.logger.info("Start saving velocity...");
					if (FreezeHandler.isServerFrozen()) {
						new SavingVelocity().saveVelocityCustom(FreezeHandler.entity.get(0).getMotionX(),
								FreezeHandler.entity.get(0).getMotionY(), FreezeHandler.entity.get(0).getMotionZ(), FreezeHandler.entity.get(0).getFalldistance(),
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
									+ File.separator + ev.player.getDisplayName() + "_velocity.txt");
					CommonProxy.logger.info("Start saving velocity for " + ev.player.getDisplayName() + " (Singleplayer)");
					if (FreezeHandler.isServerFrozen()) {
						for (int i = 0; i < players.size(); i++) {
							if (FreezeHandler.entity.get(i).getPlayername().equals(ev.player.getDisplayName())) {
								new SavingVelocity().saveVelocityCustom(FreezeHandler.entity.get(i).getMotionX(),
										FreezeHandler.entity.get(i).getMotionY(),
										FreezeHandler.entity.get(i).getMotionZ(), FreezeHandler.entity.get(i).getFalldistance(), file);
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
		}
	}
}
