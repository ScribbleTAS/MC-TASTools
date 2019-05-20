package de.scribble.lp.TASTools.savestates;

import de.scribble.lp.TASTools.ModLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SavestatePacketHandler implements IMessageHandler<SavestatePacket, IMessage>{

	@Override
	public IMessage onMessage(SavestatePacket message, MessageContext ctx) {
		if (ctx.side== Side.SERVER) {
			EntityPlayerMP player=ctx.getServerHandler().player;
			MinecraftServer server=FMLCommonHandler.instance().getMinecraftServerInstance();
			if (!server.isDedicatedServer()) {
				ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
					if (!player.canUseCommand(2, "savestate")) {
						return;
					}
					if(message.isLoadSave()) {
						new SavestateHandlerClient().saveState();
					}
					else {
						if(server.getCurrentPlayerCount()==1) {
							ModLoader.NETWORK.sendTo(new SavestatePacket(false,1), (EntityPlayerMP) player);
						}
						else {
							ModLoader.NETWORK.sendTo(new SavestatePacket(false,1), server.getPlayerList().getPlayers().get(0));
						}
					}
				});
			}else {
				ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
					if(message.isLoadSave())new SavestateHandlerServer().saveState();
					else new SavestateHandlerServer().setFlagandShutdown();
				});
			}
		} else if (ctx.side == Side.CLIENT) {
			Minecraft.getMinecraft().addScheduledTask(()->{
				if (message.getMode()==0) {
					if(!message.isLoadSave()) {
						new SavestateHandlerClient().displayLoadingScreen();
					}else {
						new SavestateHandlerClient().displayIngameMenu();
					}
				}else if (message.getMode()==1) {
					if (!message.isLoadSave()) {
						new SavestateHandlerClient().loadLastSavestate();
					}
				}
			});
		}

		return null;
	}

}
