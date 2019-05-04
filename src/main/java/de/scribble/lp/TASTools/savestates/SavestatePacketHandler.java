package de.scribble.lp.TASTools.savestates;

import de.scribble.lp.TASTools.savestates.gui.GuiSavestateSavingScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SavestatePacketHandler implements IMessageHandler<SavestatePacket, IMessage>{

	@Override
	public IMessage onMessage(SavestatePacket message, MessageContext ctx) {
		if (ctx.side.isServer()) {
			if (!ctx.getServerHandler().player.getServer().isDedicatedServer()) {
				ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
					new SavestateHandlerClient().loadLastSavestate();
				});
			}else {
				ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
					new SavestateHandlerServer().saveState();
				});
			}
		} else if (ctx.side.isClient()) {
			new SavestateHandlerClient().displayLoadingScreen();
		}

		return null;
	}

}
