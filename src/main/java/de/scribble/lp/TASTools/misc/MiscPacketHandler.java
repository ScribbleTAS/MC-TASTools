package de.scribble.lp.TASTools.misc;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.savestates.SavestateEvents;
import net.minecraft.client.Minecraft;
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
					Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("msg.gui.disabled")); // §cDisabled Logo in HUD
					GuiOverlayLogo.potionenabled = false;
					ClientProxy.config.get("GuiPotion", "Enabled", true, "Enables the MC-TAS-Logo in the Gui")
							.set(false);
					ClientProxy.config.save();
				} else if (!GuiOverlayLogo.potionenabled) {
					Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("msg.gui.enabled")); // §aEnabled Logo in HUD
					GuiOverlayLogo.potionenabled = true;
					ClientProxy.config.get("GuiPotion", "Enabled", true, "Enables the MC-TAS-Logo in the Gui")
							.set(true);
					ClientProxy.config.save();
				}
			}else if(message.getMode()==2) {
				File file = new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator + "savestates");
				try {
					if(!file.exists())file.mkdir();
					Desktop.getDesktop().open(file);
				} catch (IOException e) {
					CommonProxy.logger.fatal("Something went wrong while opening ", new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator + "savestates").getPath());
					e.printStackTrace();
				}
			}else if(message.getMode()==3) {
				if (SavestateEvents.savestatepauseenabled) {
					Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("msg.pausegui.disabled"));	//§cDisabled Velocity when joining the world
					SavestateEvents.savestatepauseenabled = false;
					ClientProxy.config.get("Savestate", "CustomGui", true, "Enables 'Make a Savestate' Button in the pause menu. Disable this if you use other mods that changes the pause menu")
							.set(false);
					ClientProxy.config.save();
				} else if (!SavestateEvents.savestatepauseenabled) {
					Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("msg.pausegui.enabled"));		//§aEnabled Velocity when joining the world
					SavestateEvents.savestatepauseenabled = true;
					ClientProxy.config.get("Savestate", "CustomGui", true, "Enables 'Make a Savestate' Button in the pause menu. Disable this if you use other mods that changes the pause menu")
							.set(true);
					ClientProxy.config.save();
				}
			}
		}
		
		return null;
	}

}
