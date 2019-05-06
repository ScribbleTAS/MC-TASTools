package de.scribble.lp.TASTools.misc;

import de.scribble.lp.TASTools.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MiscPacketHandler implements IMessageHandler<MiscPacket, IMessage>{

	@Override
	public IMessage onMessage(MiscPacket message, MessageContext ctx) {
		if(message.getMode()==0) {
			new Util().reloadClientconfig();
			Minecraft.getMinecraft().player.sendMessage(new TextComponentTranslation("msg.misc.reload")); //Config reloaded!
		}else if(message.getMode()==1) {
			EntityPlayerSP sender=Minecraft.getMinecraft().player;
			if(GuiOverlayLogo.potionenabled) {
				sender.sendMessage(new TextComponentTranslation("msg.logo.disabled")); //�cDisabled Logo in HUD
				GuiOverlayLogo.potionenabled=false;
				ClientProxy.config.get("GuiPotion","Enabled",true,"Enables the MC-TAS-Logo in the Gui").set(false);
				ClientProxy.config.save();
			}else if(!GuiOverlayLogo.potionenabled) {
				sender.sendMessage(new TextComponentTranslation("msg.logo.enabled"));	//�aEnabled Logo in HUD
				GuiOverlayLogo.potionenabled=true;
				ClientProxy.config.get("GuiPotion","Enabled",true,"Enables the MC-TAS-Logo in the Gui").set(true);
				ClientProxy.config.save();
			}
		}
		return null;
	}

}