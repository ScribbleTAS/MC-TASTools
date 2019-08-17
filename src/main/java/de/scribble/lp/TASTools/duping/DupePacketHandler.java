package de.scribble.lp.TASTools.duping;

import java.io.File;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import de.scribble.lp.TASTools.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;

public class DupePacketHandler implements IMessageHandler<DupePacket, IMessage>{


	@Override
	public IMessage onMessage(DupePacket message, MessageContext ctx) {
		if (ctx.side==Side.SERVER) {
			if(!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()&&FMLCommonHandler.instance().getMinecraftServerInstance().getCurrentPlayerCount()==1) {
				File file= new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator +Minecraft.getMinecraft().getIntegratedServer().getFolderName()+File.separator+"latest_dupe.txt");
				EntityPlayerMP player =ctx.getServerHandler().playerEntity;
				new DupeEvents().startStopping(player);
				new RefillingDupe().refill(file, player);
			}else {
				CommonProxy.logger.warn("Can't duplicate items on a server!");
			}
		}
		return null;
	}

}
