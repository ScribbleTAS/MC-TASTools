package de.scribble.lp.TASTools.savestates;

import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freezeV2.FreezeHandlerServer;
import de.scribble.lp.TASTools.velocityV2.RequestVelocityPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SavestatePacketHandler implements IMessageHandler<SavestatePacket, IMessage>{
	private boolean stop=false;
	@Override
	public IMessage onMessage(SavestatePacket message, MessageContext ctx) {
		if (ctx.side == Side.SERVER) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			if (message.getMode() == 0) {
				if (!server.isDedicatedServer()) {
					ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
						if (!player.canUseCommand(2, "savestate")) {
							return;
						}
						if (message.isLoadSave()) {
							FreezeHandlerServer.clear();
							ModLoader.NETWORK.sendToAll(new RequestVelocityPacket());
							if (server.getCurrentPlayerCount() == 1) {
								ModLoader.NETWORK.sendTo(new SavestatePacket(true, 1), (EntityPlayerMP) player);
							} else if (server.getCurrentPlayerCount() > 1) {
								ModLoader.NETWORK.sendTo(new SavestatePacket(true, 1),server.getPlayerList().getPlayers().get(0));
							}
						} else {
							if (server.getCurrentPlayerCount() == 1) {
								ModLoader.NETWORK.sendTo(new SavestatePacket(false, 1), (EntityPlayerMP) player);
							} else {
								ModLoader.NETWORK.sendTo(new SavestatePacket(false, 1),server.getPlayerList().getPlayers().get(0));
							}
						}
					});
				} else {
					ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
						if (!player.canUseCommand(2, "savestate")) {
							return;
						}
						if (message.isLoadSave()) {
							FreezeHandlerServer.clear();
							ModLoader.NETWORK.sendToAll(new RequestVelocityPacket());
							new SavestateHandlerServer().saveState();
						}else {
							new SavestateHandlerServer().setFlagandShutdown();
						}
					});
				}
			} else if (message.getMode() == 1) {
				ctx.getServerHandler().player.getServerWorld().addScheduledTask(() -> {
					CommonProxy.logger.debug("Saving worlds and playerdata on the integrated Server");
					for (int i = 0; i < server.worlds.length; ++i) {
						if (server.worlds[i] != null) {
							WorldServer worldserver = server.worlds[i];
							boolean flag = worldserver.disableLevelSaving;
							worldserver.disableLevelSaving = false;
							try {
								worldserver.saveAllChunks(true, (IProgressUpdate) null);
							} catch (MinecraftException e) {
								CommonProxy.logger
										.debug("Something went wrong while Saving Chunks (SavestatePacketHandler)");
								CommonProxy.logger.catching(e);
							}
							worldserver.disableLevelSaving = flag;
						}
					}
					server.getPlayerList().saveAllPlayerData();
				});
			}
		} else if (ctx.side == Side.CLIENT) {
			Minecraft.getMinecraft().addScheduledTask(()->{
				if (message.getMode()==0) {
					if(!message.isLoadSave()) {
						ClientProxy.getSaveHandlerClient().displayLoadingScreen();
					}else {
						ClientProxy.getSaveHandlerClient().displayIngameMenu();
					}
				}else if (message.getMode()==1) {
					if (!message.isLoadSave()) {
						ClientProxy.getSaveHandlerClient().loadLastSavestate();
					}
					else {
						ClientProxy.getSaveHandlerClient().saveState();
					}
				}
			});
		}
		return null;
	}

}
