package de.scribble.lp.TASTools.freezeV2.networking;

import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freezeV2.FreezeHandlerClient;
import de.scribble.lp.TASTools.freezeV2.MotionSaver;
import de.scribble.lp.TASTools.freezeV2.RelMotionSaver;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class FreezePacketHandler implements IMessageHandler<FreezePacket, IMessage> {


	@Override
	public FreezePacket onMessage(FreezePacket msg, MessageContext ctx) {
		if(ctx.side==Side.CLIENT) {
			Minecraft.getMinecraft().addScheduledTask(()->{
				boolean enabled=msg.isEnabled();
				if(enabled) {
					FreezeHandlerClient.enable(enabled);
					if(msg.sendPacket()) {
						MotionSaver saver=FreezeHandlerClient.getSaverClient();
						double x=saver.getMotionSavedX();
						double y=saver.getMotionSavedY();
						double z=saver.getMotionSavedZ();
						
						RelMotionSaver relsaver=FreezeHandlerClient.getRelsaverClient();
						float rx=relsaver.getRelSavedX();
						float ry=relsaver.getRelSavedY();
						float rz=relsaver.getRelSavedZ();
						
						ModLoader.NETWORK.sendToServer(new MovementPacket(x,y,z,rx,ry,rz));
					}
				}else {
					FreezeHandlerClient.enable(enabled);
				}
			});
		}
		return null;
	}
}
