package de.scribble.lp.TASTools.savestates;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SavestatePacketHandler implements IMessageHandler<SavestatePacket, IMessage>{

	@Override
	public IMessage onMessage(final SavestatePacket message, MessageContext ctx) {

		if (ctx.side== Side.SERVER) {
			final EntityPlayer player =ctx.getServerHandler().playerEntity;
				if (!ctx.getServerHandler().playerEntity.mcServer.isDedicatedServer()) {
					ctx.getServerHandler().playerEntity.getServerForPlayer().addScheduledTask(new Runnable() {
	
						@Override
						public void run() {
							if(MinecraftServer.getServer().getConfigurationManager().canSendCommands(player.getGameProfile())){
								if (message.isLoadSave()) {
									new SavestateHandlerClient().saveState();
								} else {
									new SavestateHandlerClient().loadLastSavestate();
								}
							}
						}
						
					});
				}else {
					ctx.getServerHandler().playerEntity.getServerForPlayer().addScheduledTask(new Runnable(){
	
						@Override
						public void run() {
							if(MinecraftServer.getServer().getConfigurationManager().canSendCommands(player.getGameProfile())){
								if(message.isLoadSave())new SavestateHandlerServer().saveState();
								else new SavestateHandlerServer().setFlagandShutdown();
							}
						}
						
					});
				}
			
		} else if (ctx.side == Side.CLIENT) {
			Minecraft.getMinecraft().addScheduledTask(new Runnable() {

				@Override
				public void run() {
					if(!message.isLoadSave()) {
						new SavestateHandlerClient().displayLoadingScreen();
					}else {
						new SavestateHandlerClient().displayIngameMenu();
					}
					
				}
				
			});
		}

		return null;
	}

}
