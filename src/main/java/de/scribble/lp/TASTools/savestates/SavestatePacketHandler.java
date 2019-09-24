package de.scribble.lp.TASTools.savestates;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import de.scribble.lp.TASTools.ModLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class SavestatePacketHandler implements IMessageHandler<SavestatePacket, IMessage>{

	@Override
	public IMessage onMessage(final SavestatePacket message, MessageContext ctx) {

		if (ctx.side== Side.SERVER) {
			final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			final EntityPlayer player =ctx.getServerHandler().playerEntity;
				if (!ctx.getServerHandler().playerEntity.mcServer.isDedicatedServer()) {

							if(MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile())){
								if (message.isLoadSave()) {
									if(server.getCurrentPlayerCount()==1) {
										ModLoader.NETWORK.sendTo(new SavestatePacket(true,1), (EntityPlayerMP) player);
									}
									else if(server.getCurrentPlayerCount()>1){
										ModLoader.NETWORK.sendTo(new SavestatePacket(true,1), (EntityPlayerMP) server.getConfigurationManager().playerEntityList.get(0));
									}
								} else {
									if(server.getConfigurationManager().getCurrentPlayerCount()==1) {
										ModLoader.NETWORK.sendTo(new SavestatePacket(false,1), (EntityPlayerMP) player);
									}
									else {
										ModLoader.NETWORK.sendTo(new SavestatePacket(false,1), (EntityPlayerMP) server.getConfigurationManager().playerEntityList.get(0));
									}
								}
							}

				}else {

							if(MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile())){
								if (message.getMode()==0) {
									if(message.isLoadSave())new SavestateHandlerServer().saveState();
									else new SavestateHandlerServer().setFlagandShutdown();
								}
							}

				}
			
		} else if (ctx.side == Side.CLIENT) {

					if (message.getMode()==0) {
						if(!message.isLoadSave()) {
							new SavestateHandlerClient().displayLoadingScreen();
						}else {
							new SavestateHandlerClient().displayIngameMenu();
						}
					}
					else if (message.getMode() == 1) {
						if (!message.isLoadSave()) {
							new SavestateHandlerClient().loadLastSavestate();
						} else {
							new SavestateHandlerClient().saveState();
						}
					}
				}
		return null;
	}

}
