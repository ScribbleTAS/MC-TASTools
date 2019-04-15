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
		public static boolean velocityenabled;
		
		@SubscribeEvent
		public void onCloseServer(PlayerEvent.PlayerLoggedOutEvent ev){
			if(velocityenabled) {
				if (!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
					File file= new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator +Minecraft.getMinecraft().getIntegratedServer().getFolderName()+File.separator+"latest_velocity.txt");
					CommonProxy.logger.info("Start saving velocity...");
					new SavingVelocity().saveVelocity(ev.player, file);
				}
			}
		}
		
		@SubscribeEvent
		public void onOpenServer(PlayerEvent.PlayerLoggedInEvent ev){

		}
}
