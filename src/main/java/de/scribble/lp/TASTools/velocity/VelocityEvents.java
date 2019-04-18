package de.scribble.lp.TASTools.velocity;

import java.io.File;
import java.util.List;

import javax.swing.text.html.parser.Entity;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.duping.RecordingDupe;
import de.scribble.lp.TASTools.duping.RefillingDupe;
import de.scribble.lp.TASTools.freeze.FreezeHandler;
import de.scribble.lp.TASTools.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class VelocityEvents {
	public static boolean velocityenabledClient;
	public static boolean velocityenabledServer;

	@SubscribeEvent
	public void onCloseServer(PlayerEvent.PlayerLoggedOutEvent ev) {
		//Singleplayer
		if (!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			List<EntityPlayerMP> players= FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
			if (players.size()>1) {
				
				if (velocityenabledClient) {
					File file = new File(Minecraft.getMinecraft().mcDataDir,
							"saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName()
									+ File.separator + ev.player.getName()+"_velocity.txt");
					CommonProxy.logger.info("Start saving velocity for "+ev.player.getName()+" (Singleplayer)");
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
			//LAN-Server
			}else {	
				if (velocityenabledClient) {
					File file = new File(Minecraft.getMinecraft().mcDataDir,
							"saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName()
									+ File.separator + "latest_velocity.txt");
					CommonProxy.logger.info("Start saving velocity...");
					if(FreezeHandler.isServerFrozen()) {
						new SavingVelocity().saveVelocityCustom(FreezeHandler.entity.get(0).getMotionX(), FreezeHandler.entity.get(0).getMotionY(), FreezeHandler.entity.get(0).getMotionZ(), file);
					}
					else {
						new SavingVelocity().saveVelocity(ev.player, file);
					}
				}
			}
		//Dedicated Multiplayer
		}else {
			if(velocityenabledServer) {
				File file = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath() + File.separator + ModLoader.levelname +File.separator
						+ ev.player.getName() + "_velocity.txt");
				List<EntityPlayerMP> players= FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
				CommonProxy.logger.info("Saving velocity of "+ev.player.getName());
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
