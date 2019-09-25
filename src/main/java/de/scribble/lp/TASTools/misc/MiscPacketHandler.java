package de.scribble.lp.TASTools.misc;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.savestates.SavestateEvents;
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
			EntityPlayerSP sender=Minecraft.getMinecraft().thePlayer;
			if(GuiOverlayLogo.potionenabled) {
				sender.addChatMessage(new TextComponentTranslation("msg.gui.disabled")); //§cDisabled Logo in HUD
				GuiOverlayLogo.potionenabled=false;
				ClientProxy.config.get("GuiPotion","Enabled",true,"Enables the MC-TAS-Logo in the Gui").set(false);
				ClientProxy.config.save();
			}else if(!GuiOverlayLogo.potionenabled) {
				sender.addChatMessage(new TextComponentTranslation("msg.gui.enabled"));	//§aEnabled Logo in HUD
				GuiOverlayLogo.potionenabled=true;
				ClientProxy.config.get("GuiPotion","Enabled",true,"Enables the MC-TAS-Logo in the Gui").set(true);
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
			EntityPlayerSP sender=Minecraft.getMinecraft().thePlayer;
			if (SavestateEvents.savestatepauseenabled) {
				sender.addChatMessage(new TextComponentTranslation("msg.pausegui.disabled"));	//§cDisabled Velocity when joining the world
				SavestateEvents.savestatepauseenabled = false;
				ClientProxy.config.get("Savestate", "CustomGui", true, "Enables 'Make a Savestate' Button in the pause menu. Disable this if you use other mods that changes the pause menu")
						.set(false);
				ClientProxy.config.save();
			} else if (!SavestateEvents.savestatepauseenabled) {
				sender.addChatMessage(new TextComponentTranslation("msg.pausegui.enabled"));		//§aEnabled Velocity when joining the world
				SavestateEvents.savestatepauseenabled = true;
				ClientProxy.config.get("Savestate", "CustomGui", true, "Enables 'Make a Savestate' Button in the pause menu. Disable this if you use other mods that changes the pause menu")
						.set(true);
				ClientProxy.config.save();
			}
		}
		return null;
	}
}
