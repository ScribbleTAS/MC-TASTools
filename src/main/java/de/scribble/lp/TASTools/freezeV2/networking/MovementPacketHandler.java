package de.scribble.lp.TASTools.freezeV2.networking;

import de.scribble.lp.TASTools.freezeV2.FreezeHandlerClient;
import de.scribble.lp.TASTools.freezeV2.FreezeHandlerServer;
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
				
				FreezeHandlerServer.add(player, x, y, z, rx, ry, rz);
			});
		}else {
			Minecraft.getMinecraft().addScheduledTask(()->{
				if(FreezeHandlerClient.getSaverClient()!=null||FreezeHandlerClient.getRelsaverClient()!=null) {
					FreezeHandlerClient.getSaverClient().setMotionSavedX(message.getMoX());
					FreezeHandlerClient.getSaverClient().setMotionSavedY(message.getMoY());
					FreezeHandlerClient.getSaverClient().setMotionSavedZ(message.getMoZ());
					
					float[] in={message.getRelX(),message.getRelY(), message.getRelZ()};
					
					FreezeHandlerClient.getRelsaverClient().setRelativeMotionSaved(in);
				}
			});
		}
		return null;
	}

}
