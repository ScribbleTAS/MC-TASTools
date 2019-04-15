package de.scribble.lp.TASTools.freeze;

import java.io.File;
import java.util.ArrayList;
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

public class FreezeEvents {
	@SubscribeEvent
	public void onjoinServer(PlayerLoggedInEvent ev) {
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()&&ModLoader.freezeenabledSP) {
			if (ModLoader.freeze) {
				List<EntityPlayerMP> playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
						.getPlayers();
	
				if (playerMP.size() > 0) {
					FreezeHandler.entity= new ArrayList<EntityDataStuff>();
					for (int i = 0; i < (playerMP.size()); i++) {
						FreezeHandler.entity.add(i,
								new EntityDataStuff(playerMP.get(i).posX, playerMP.get(i).posY, playerMP.get(i).posZ,
										playerMP.get(i).rotationPitch, playerMP.get(i).rotationYaw, playerMP.get(i).motionX,
										playerMP.get(i).motionY, playerMP.get(i).motionZ));
						playerMP.get(i).setEntityInvulnerable(true);
						playerMP.get(i).setNoGravity(true);
					}
					ApplyFreezeServer.playerMP = playerMP;
				}
			}
		}else {
			if(ModLoader.freezeenabledSP) {
				ModLoader.freeze = true;
				EntityPlayerMP playerEv=(EntityPlayerMP) ev.player;
				if (VelocityEvents.velocityenabled) {
					File file= new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator +Minecraft.getMinecraft().getIntegratedServer().getFolderName()+File.separator+"latest_velocity.txt");
					if (file.exists()){
						double [] motion = new ReapplyingVelocity().getVelocity(playerEv, file);
						FreezeHandler.startFreezeSetMotionServer(motion[0], motion[1], motion[2]);
					}else FreezeHandler.startFreezeServer();
				}else {
					FreezeHandler.startFreezeServer();
				}
				
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
					FreezeHandler.entity= new ArrayList<EntityDataStuff>();
					for (int i = 0; i < (playerMP.size()); i++) {
						FreezeHandler.entity.add(i,
								new EntityDataStuff(playerMP.get(i).posX, playerMP.get(i).posY, playerMP.get(i).posZ,
										playerMP.get(i).rotationPitch, playerMP.get(i).rotationYaw, playerMP.get(i).motionX,
										playerMP.get(i).motionY, playerMP.get(i).motionZ));
						playerMP.get(i).setEntityInvulnerable(true);
						playerMP.get(i).setNoGravity(true);
					}
					ApplyFreezeServer.playerMP = playerMP;
					
				}
			}
		}else{
			FreezeHandler.entity= new ArrayList<EntityDataStuff>();
			ModLoader.freeze = false;
			FreezeHandler.stopFreezeServer();
			ModLoader.NETWORK.sendToAll(new FreezePacket(false));

		}
	}
}
