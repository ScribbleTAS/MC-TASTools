package de.scribble.lp.TASTools.freeze;

import java.util.ArrayList;
import java.util.List;

import de.scribble.lp.TASTools.proxy.CommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class FreezePacketHandler implements IMessageHandler<FreezePacket,FreezePacket>{
	
	static FreezeEvents Freezer =new FreezeEvents();
	public static EntityDataStuff entity;

	@Override
	public FreezePacket onMessage(FreezePacket message, MessageContext ctx) {
		EntityPlayerMP player=ctx.getServerHandler().player;
		if(message.startstop()) {
			
			World world = player.getEntityWorld();
			FreezeEvents.playerSp=player;
			entity=new EntityDataStuff(player.posX, player.posY, player.posZ, player.rotationPitch, player.rotationYaw, player.motionX, player.motionY, player.motionZ);
			MinecraftForge.EVENT_BUS.register(Freezer);
		}
			
		
		if(!message.startstop()) {
			player.setPositionAndUpdate(FreezePacketHandler.entity.getPosX(), FreezePacketHandler.entity.getPosY(), FreezePacketHandler.entity.getPosZ());
			player.rotationPitch=FreezePacketHandler.entity.getPitch();
			player.rotationYaw=FreezePacketHandler.entity.getYaw();
			player.motionX=entity.getMotionX();
			player.motionY=entity.getMotionY();
			player.motionZ=entity.getMotionZ();
			player.velocityChanged=true;
			MinecraftForge.EVENT_BUS.unregister(Freezer);
		}
		
		return null;
	}
}
