package de.scribble.lp.TASTools.freezeV2.networking;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freezeV2.FreezeHandlerClient;
import de.scribble.lp.TASTools.freezeV2.FreezeHandlerServer;
import de.scribble.lp.TASTools.freezeV2.MotionSaver;
import de.scribble.lp.TASTools.freezeV2.RelMotionSaver;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MovementPacketHandler implements IMessageHandler<MovementPacket, IMessage>{

	@Override
	public IMessage onMessage(MovementPacket message, MessageContext ctx) {
		if(ctx.side==Side.SERVER) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			player.getServerWorld().addScheduledTask(()->{
				double x=message.getMoX();
				double y=message.getMoY();
				double z=message.getMoZ();
				
				float rx=message.getRelX();
				float ry=message.getRelY();
				float rz=message.getRelZ();
				
				float pitch=message.getPitch();
				float yaw=message.getYaw();
				
				FreezeHandlerServer.add(player, x, y, z, rx, ry, rz, pitch, yaw);
			});
		}else {
			Minecraft.getMinecraft().addScheduledTask(()->{
				String playername=Minecraft.getMinecraft().player.getName();
				if(FreezeHandlerClient.isEnabled()) {
					FreezeHandlerClient.saverClient=new MotionSaver(playername, FreezeHandlerClient.isEnabled(), message.getMoX(), message.getMoY(), message.getMoZ());
						
					FreezeHandlerClient.relsaverClient=new RelMotionSaver(playername, FreezeHandlerClient.isEnabled(), message.getRelX(), message.getRelY(), message.getRelZ());
				}else {
					FreezeHandlerClient.moX=message.getMoX();
					FreezeHandlerClient.moY=message.getMoY();
					FreezeHandlerClient.moZ=message.getMoZ();
					
					FreezeHandlerClient.relX=message.getRelX();
					FreezeHandlerClient.relY=message.getRelY();
					FreezeHandlerClient.relZ=message.getRelZ();
				}
				FreezeHandlerClient.pitch=message.getPitch();
				FreezeHandlerClient.yaw=message.getYaw();
				
				ModLoader.NETWORK.sendToServer(new AcknowledgePacket());
				FreezeHandlerClient.motionapplied=true;
			});
		}
		return null;
	}
}
