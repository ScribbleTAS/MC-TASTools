package de.scribble.lp.TASTools.freeze;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class FreezePacketHandler implements IMessageHandler<FreezePacket, IMessage> {


	@Override
	public FreezePacket onMessage(FreezePacket msg, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			if (msg.startstop()) {
				FreezeHandler.startFreezeServer();
			}

			else if (!msg.startstop()) {
				FreezeHandler.stopFreezeServer();
			}
			
			
		} else if (ctx.side == Side.CLIENT) {
			if (msg.startstop()) {
				FreezeHandler.startFreezeClient();
			}

			else if (!msg.startstop()) {
				FreezeHandler.stopFreezeClient();
			}
		}
		return null;
	}
}
