package de.scribble.lp.TASTools.freezeV2.networking;

import de.scribble.lp.TASTools.velocityV2.VelocityHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class AcknowledgePacketHandler implements IMessageHandler<AcknowledgePacket, IMessage>{

	@Override
	public IMessage onMessage(AcknowledgePacket message, MessageContext ctx) {
		if(ctx.side==Side.SERVER) {
			VelocityHandler.playeracknowledge.get(ctx.getServerHandler().player.getName()).setBreaks(true);
		}
		return null;
	}

}
