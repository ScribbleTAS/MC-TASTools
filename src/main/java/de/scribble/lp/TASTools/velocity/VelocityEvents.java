package de.scribble.lp.TASTools.velocity;

import java.io.File;

import de.scribble.lp.TASTools.duping.RecordingDupe;
import de.scribble.lp.TASTools.duping.RefillingDupe;
import de.scribble.lp.TASTools.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
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
		if (!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			if (velocityenabledClient) {
				File file = new File(Minecraft.getMinecraft().mcDataDir,
						"saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName()
								+ File.separator + "latest_velocity.txt");
				CommonProxy.logger.info("Start saving velocity...");
				new SavingVelocity().saveVelocity(ev.player, file);
			}
		}else {
			if(velocityenabledServer) {
				File file = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath() + File.separator + ev.player.getEntityWorld().getWorldInfo().getWorldName()+File.separator
						+ ev.player.getName() + "_velocity.txt");
				CommonProxy.logger.info("Saving velocity of "+ev.player.getName());
				new SavingVelocity().saveVelocity(ev.player, file);
			}
		}

	}

	@SubscribeEvent
	public void onOpenServer(PlayerEvent.PlayerLoggedInEvent ev) {

	}
}
