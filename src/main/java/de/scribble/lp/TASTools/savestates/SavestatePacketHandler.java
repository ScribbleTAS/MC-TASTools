package de.scribble.lp.TASTools.savestates;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SavestatePacketHandler implements IMessageHandler<SavestatePacket, IMessage>{

	@Override
	public IMessage onMessage(SavestatePacket message, MessageContext ctx) {
		ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
			new SavestateHandlerClient().loadLastSavestate();
		});
		return null;
	}

}
