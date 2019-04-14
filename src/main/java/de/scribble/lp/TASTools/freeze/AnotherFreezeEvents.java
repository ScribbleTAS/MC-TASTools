package de.scribble.lp.TASTools.freeze;

import java.io.File;
import java.util.List;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.proxy.ClientProxy;
import de.scribble.lp.TASTools.velocity.ReapplyingVelocity;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class AnotherFreezeEvents {
	@SubscribeEvent
	public void onjoinServer(PlayerLoggedInEvent ev) {
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()&&ModLoader.freezeenabledSP) {
			if (ModLoader.freeze) {
				List<EntityPlayerMP> playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
						.getPlayers();
	
				if (playerMP.size() > 0) {
					FreezePacketHandler.entity.removeAll(FreezePacketHandler.entity);
					for (int i = 0; i < (playerMP.size()); i++) {
						FreezePacketHandler.entity.add(i,
								new EntityDataStuff(playerMP.get(i).posX, playerMP.get(i).posY, playerMP.get(i).posZ,
										playerMP.get(i).rotationPitch, playerMP.get(i).rotationYaw, playerMP.get(i).motionX,
										playerMP.get(i).motionY, playerMP.get(i).motionZ));
						playerMP.get(i).setEntityInvulnerable(true);
						playerMP.get(i).setNoGravity(true);
					}
					FreezeEventsServer.playerMP = playerMP;
				}
			}
		}else {
			if(ModLoader.freezeenabledSP) {
				ModLoader.freeze = true;
				EntityPlayerMP playerMP=(EntityPlayerMP) ev.player;
				FreezePacketHandler Freezer = new FreezePacketHandler();
				/*if (VelocityEvents.velocityenabled) {
					File file= new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator +Minecraft.getMinecraft().getIntegratedServer().getFolderName()+File.separator+"latest_velocity.txt");
					if (file.exists()){
						double [] pos = new ReapplyingVelocity().getVelocity(playerMP, file);
						Freezer.startFreezeSetMotion(pos[0], pos[1], pos[2]);*/
					//}else Freezer.startFreeze();
				//}else {
					Freezer.startFreeze();
				//}
				
				ModLoader.NETWORK.sendToAll(new FreezePacket(true));
			}
		}
	}

	@SubscribeEvent
	public void onLeaveServer(PlayerLoggedOutEvent ev) {
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()&&ModLoader.freezeenabledSP) {
			if (ModLoader.freeze) {
				List<EntityPlayerMP> playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
						.getPlayers();
	
				if (playerMP.size() > 0) {
					FreezePacketHandler.entity.removeAll(FreezePacketHandler.entity);
					for (int i = 0; i < (playerMP.size()); i++) {
						FreezePacketHandler.entity.add(i,
								new EntityDataStuff(playerMP.get(i).posX, playerMP.get(i).posY, playerMP.get(i).posZ,
										playerMP.get(i).rotationPitch, playerMP.get(i).rotationYaw, playerMP.get(i).motionX,
										playerMP.get(i).motionY, playerMP.get(i).motionZ));
						playerMP.get(i).setEntityInvulnerable(true);
						playerMP.get(i).setNoGravity(true);
					}
					FreezeEventsServer.playerMP = playerMP;
					
				}
			}
		}else{
			ModLoader.freeze = false;
			FreezePacketHandler Freezer = new FreezePacketHandler();
			Freezer.stopFreeze();
			ModLoader.NETWORK.sendToAll(new FreezePacket(false));

		}
	}
}
