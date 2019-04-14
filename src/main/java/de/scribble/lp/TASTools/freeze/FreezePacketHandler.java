package de.scribble.lp.TASTools.freeze;

import java.util.ArrayList;
import java.util.List;

import de.scribble.lp.TASTools.proxy.CommonProxy;
import ibxm.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class FreezePacketHandler implements IMessageHandler<FreezePacket, IMessage> {

	static FreezeEventsServer FreezerSE = new FreezeEventsServer();
	static FreezeEventsClient FreezerCL = new FreezeEventsClient();
	public static ArrayList<EntityDataStuff> entity = new ArrayList<EntityDataStuff>();

	@Override
	public FreezePacket onMessage(FreezePacket msg, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			if (msg.startstop()) {
				startFreeze();
			}

			else if (!msg.startstop()) {
				stopFreeze();
			}
			
			
		} else if (ctx.side == Side.CLIENT) {
			if (msg.startstop()) {
				MinecraftForge.EVENT_BUS.register(FreezerCL);
			}

			else if (!msg.startstop()) {
				MinecraftForge.EVENT_BUS.unregister(FreezerCL);
			}
		}
		return null;
	}
	
	
	public void startFreeze() {
		List<EntityPlayerMP> playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
				.getPlayers();

		if (playerMP.size() > 0) {
			entity.removeAll(entity);
			for (int i = 0; i < (playerMP.size()); i++) {
				entity.add(i, new EntityDataStuff(playerMP.get(i).posX, playerMP.get(i).posY, playerMP.get(i).posZ,
								playerMP.get(i).rotationPitch, playerMP.get(i).rotationYaw,
								playerMP.get(i).motionX, playerMP.get(i).motionY, playerMP.get(i).motionZ));
				playerMP.get(i).setEntityInvulnerable(true);
				playerMP.get(i).setNoGravity(true);
			}
			FreezerSE.playerMP = playerMP;
			MinecraftForge.EVENT_BUS.register(FreezerSE);
		}
	}
	
	
	public void stopFreeze() {
		List<EntityPlayerMP> playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
				.getPlayers();
		for (int i = 0; i < (playerMP.size()); i++) {
			playerMP.get(i).setPositionAndUpdate(FreezePacketHandler.entity.get(i).getPosX(),
					FreezePacketHandler.entity.get(i).getPosY(), FreezePacketHandler.entity.get(i).getPosZ());
			playerMP.get(i).rotationPitch = FreezePacketHandler.entity.get(i).getPitch();
			playerMP.get(i).rotationYaw = FreezePacketHandler.entity.get(i).getYaw();
			playerMP.get(i).setEntityInvulnerable(false);
			playerMP.get(i).setNoGravity(false);
			playerMP.get(i).motionX = entity.get(i).getMotionX();
			playerMP.get(i).motionY = entity.get(i).getMotionY();
			playerMP.get(i).motionZ = entity.get(i).getMotionZ();
			playerMP.get(i).velocityChanged = true;
		}
		MinecraftForge.EVENT_BUS.unregister(FreezerSE);
	}
}
