package de.scribble.lp.TASTools.freeze;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import de.scribble.lp.TASTools.ModLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class FreezePacketHandler implements IMessageHandler<FreezePacket, IMessage> {


	@Override
	public FreezePacket onMessage(final FreezePacket msg, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			final EntityPlayer player = ctx.getServerHandler().playerEntity;
			ctx.getServerHandler().playerEntity.getServerForPlayer().(new Runnable(){

				@Override
				public void run() {
					if(MinecraftServer.getServer().getConfigurationManager().canSendCommands(player.getGameProfile())) {
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
