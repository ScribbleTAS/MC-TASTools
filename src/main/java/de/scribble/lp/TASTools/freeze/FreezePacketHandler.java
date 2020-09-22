package de.scribble.lp.TASTools.freeze;

import de.scribble.lp.TASTools.ModLoader;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class FreezePacketHandler implements IMessageHandler<FreezePacket, IMessage> {


	@Override
	public FreezePacket onMessage(final FreezePacket msg, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
			ctx.getServerHandler().playerEntity.getServer().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					if (!player.canCommandSenderUseCommand(2, "freeze")) {
							return;
						}
					if (msg.getMode() == 0) {
						if (msg.startstop()) {
							FreezeHandler.startFreezeServer();
						}

						else if (!msg.startstop()) {
							FreezeHandler.stopFreezeServer();
						}
					}
					else if(msg.getMode()==1) {
						if(!FreezeHandler.isServerFrozen()) {
							FreezeHandler.startFreezeServer();
							ModLoader.NETWORK.sendToAll(new FreezePacket(true));
						}
						else {
							FreezeHandler.stopFreezeServer();
							ModLoader.NETWORK.sendToAll(new FreezePacket(false));
						}
					}
				}
			});
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
