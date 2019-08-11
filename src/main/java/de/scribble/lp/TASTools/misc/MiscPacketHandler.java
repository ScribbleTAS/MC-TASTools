package de.scribble.lp.TASTools.misc;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import de.scribble.lp.TASTools.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;

public class MiscPacketHandler implements IMessageHandler<MiscPacket, IMessage>{

	@Override
	public IMessage onMessage(final MiscPacket message, MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			if (message.getMode() == 0) {
				new Util().reloadClientconfig();
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("msg.misc.reload")); // Config reloaded!
			}else if (message.getMode() == 1) {
				if (GuiOverlayLogo.potionenabled) {
					Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("msg.logo.disabled")); // §cDisabled Logo in HUD
					GuiOverlayLogo.potionenabled = false;
					ClientProxy.config.get("GuiPotion", "Enabled", true, "Enables the MC-TAS-Logo in the Gui")
							.set(false);
					ClientProxy.config.save();
				} else if (!GuiOverlayLogo.potionenabled) {
					Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("msg.logo.enabled")); // §aEnabled Logo in HUD
					GuiOverlayLogo.potionenabled = true;
					ClientProxy.config.get("GuiPotion", "Enabled", true, "Enables the MC-TAS-Logo in the Gui")
							.set(true);
					ClientProxy.config.save();
				}
			}
		}
		
		return null;
	}

}
