package de.scribble.lp.TASTools.freeze;

import java.util.ArrayList;
import java.util.List;

import de.scribble.lp.TASTools.proxy.CommonProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class FreezePacketHandler implements IMessageHandler<FreezePacket,IMessage>{
	
	static FreezeEvents Freezer =new FreezeEvents();
	public static ArrayList<Object> EntityStuff = new ArrayList<>();
	@Override
	public FreezePacket onMessage(FreezePacket message, MessageContext ctx) {
		EntityPlayer player=ctx.getServerHandler().player;
		World world = player.getEntityWorld();
		CommonProxy.logger.info("Hui");
		EntityPlayerMP playerMP=(EntityPlayerMP) player;
		
		if(message.startstop()) {
			List<Entity> ultimate=world.loadedEntityList;
			List<EntityDataStuff> EntityDataStuff = null;
			for(int i=0;i<=ultimate.size();i++) {
				
				EntityStuff.add(new EntityDataStuff(ultimate.get(i).posX, ultimate.get(i).posY, ultimate.get(i).posZ,
						ultimate.get(i).rotationPitch, ultimate.get(i).rotationYaw, ultimate.get(i).motionX, ultimate.get(i).motionY, ultimate.get(i).motionZ));
			}

		}
		
		if(!message.startstop()) {
			MinecraftForge.EVENT_BUS.unregister(Freezer);
		}
		
		return null;
	}
}
