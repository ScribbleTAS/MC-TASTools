package de.scribble.lp.TASTools.velocityV2;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freezeV2.FreezeHandlerClient;
import de.scribble.lp.TASTools.freezeV2.MotionSaver;
import de.scribble.lp.TASTools.freezeV2.RelMotionSaver;
import de.scribble.lp.TASTools.freezeV2.networking.MovementPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class RequestVelocityPacketHandler implements IMessageHandler<RequestVelocityPacket, IMessage> {
	@Override
	public IMessage onMessage(RequestVelocityPacket message, MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				double x = FreezeHandlerClient.getMoX();
				double y = FreezeHandlerClient.getMoY();
				double z = FreezeHandlerClient.getMoZ();

				float rx = FreezeHandlerClient.getRelX();
				float ry = FreezeHandlerClient.getRelY();
				float rz = FreezeHandlerClient.getRelZ();
				if(FreezeHandlerClient.isEnabled()) {
					MotionSaver saver=FreezeHandlerClient.getSaverClient();
					x=saver.getMotionSavedX();
					y=saver.getMotionSavedY();
					z=saver.getMotionSavedZ();
					RelMotionSaver relsaver=FreezeHandlerClient.getRelsaverClient();
					rx=relsaver.getRelSavedX();
					ry=relsaver.getRelSavedY();
					rz=relsaver.getRelSavedZ();
				}
				ModLoader.NETWORK.sendToServer(new MovementPacket(x, y, z, rx, ry, rz));
			});
		}
		return null;
	}

}
