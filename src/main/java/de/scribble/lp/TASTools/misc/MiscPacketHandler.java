package de.scribble.lp.TASTools.misc;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MiscPacketHandler implements IMessageHandler<MiscPacket, IMessage>{

	@Override
	public IMessage onMessage(MiscPacket message, MessageContext ctx) {
		if(message.getMode()==0) {
			new Util().reloadClientconfig();
		}else if(message.getMode()==1) {
			EntityPlayerSP sender=Minecraft.getMinecraft().player;
			if(GuiOverlayLogo.potionenabled) {
				sender.sendMessage(new TextComponentTranslation("msg.logo.disabled")); //§cDisabled Logo in HUD
				GuiOverlayLogo.potionenabled=false;
				ClientProxy.config.get("GuiPotion","Enabled",true,"Enables the MC-TAS-Logo in the Gui").set(false);
				ClientProxy.config.save();
			}else if(!GuiOverlayLogo.potionenabled) {
				sender.sendMessage(new TextComponentTranslation("msg.logo.enabled"));	//§aEnabled Logo in HUD
				GuiOverlayLogo.potionenabled=true;
				ClientProxy.config.get("GuiPotion","Enabled",true,"Enables the MC-TAS-Logo in the Gui").set(true);
				ClientProxy.config.save();
			}
		}else if(message.getMode()==2) {
			try {
				Desktop.getDesktop().open(new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator + "savestates"));
			} catch (IOException e) {
				CommonProxy.logger.fatal("Something went wrong while opening ", new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator + "savestates").getPath());
				e.printStackTrace();
			}
		}
		return null;
	}

}
