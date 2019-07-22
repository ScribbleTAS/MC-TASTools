package de.scribble.lp.TASTools.duping;

import java.io.File;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;

public class DupePacketHandler implements IMessageHandler<DupePacket, IMessage>{


	@Override
	public IMessage onMessage(DupePacket message, MessageContext ctx) {
		if (ctx.side==Side.SERVER) {
			if(!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
				File file= new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator +Minecraft.getMinecraft().getIntegratedServer().getFolderName()+File.separator+"latest_dupe.txt");
				EntityPlayerMP player =ctx.getServerHandler().playerEntity;
				new RefillingDupe().refill(file, player);
			}
		}
		return null;
	}

}
