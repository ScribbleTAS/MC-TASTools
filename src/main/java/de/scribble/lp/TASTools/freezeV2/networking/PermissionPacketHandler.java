package de.scribble.lp.TASTools.freezeV2.networking;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freezeV2.FreezeHandlerServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class PermissionPacketHandler implements IMessageHandler<PermissionPacket, IMessage>{

	@Override
	public IMessage onMessage(PermissionPacket message, MessageContext ctx) {
		if(ctx.side==Side.SERVER) {
			EntityPlayerMP player=ctx.getServerHandler().player;
			player.getServerWorld().addScheduledTask(() ->{
				if(player.canUseCommand(2, "freeze")) {
					boolean freezeStatus=FreezeHandlerServer.isEnabled();
					FreezeHandlerServer.activate(!freezeStatus);
					ModLoader.NETWORK.sendToAll(new FreezePacket(FreezeHandlerServer.isEnabled(), true));
				}
			});
		}
		return null;
	}

}
