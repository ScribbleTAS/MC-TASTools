package de.scribble.lp.TASTools.velocity;

import java.io.File;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.freeze.FreezeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraftforge.event.world.WorldEvent;

public class VelocityEvents2 {

	@SubscribeEvent
	public void WorldUnload(WorldEvent.Unload ev) {
		// Singleplayer
		VelocityEvents.stopit = false;
		if (ev.world.isRemote) {
			if(Minecraft.getMinecraft().getNetHandler().playerInfoList.size()==1) {
				if (VelocityEvents.velocityenabledClient) {
					File file = new File(Minecraft.getMinecraft().mcDataDir,
							"saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName()
									+ File.separator + "latest_velocity.txt");
					CommonProxy.logger.info("Start saving velocity...");
					if (FreezeHandler.isClientFrozen()) { // I don't even care anymore
						new SavingVelocity().saveVelocityCustom(FreezeHandler.entity.get(0).getMotionX(),
								FreezeHandler.entity.get(0).getMotionY(), FreezeHandler.entity.get(0).getMotionZ(),
								FreezeHandler.entity.get(0).getFalldistance(), file);
					} else {
						new SavingVelocity().saveVelocity(Minecraft.getMinecraft().thePlayer, file);
					}
				}
			}
		}
	}
}
