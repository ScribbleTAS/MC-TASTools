package de.scribble.lp.TASTools.keystroke;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import de.scribble.lp.TASTools.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.common.config.Configuration;

public class KeystrokesPacketHandler implements IMessageHandler<KeystrokesPacket, IMessage> {

	@Override
	public IMessage onMessage(KeystrokesPacket msg, MessageContext ctx) {
		if (ctx.side == Side.CLIENT) {
			if (!GuiKeystrokes.guienabled && msg.getCorner() == 4) {
				GuiKeystrokes.guienabled = true;
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("msg.keystrokes.enabled"));
				ClientProxy.config.get("Keystrokes", "Enabled", true, "Activates the keystrokes on startup").set(true);
				ClientProxy.config.save();
			} else if (GuiKeystrokes.guienabled && msg.getCorner() == 4) {
				GuiKeystrokes.guienabled = false;
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("msg.keystrokes.disabled"));
				ClientProxy.config.get("Keystrokes", "Enabled", true, "Activates the keystrokes on startup").set(false);
				ClientProxy.config.save();
			} else if (msg.getCorner() != 4) {
				GuiKeystrokes.changeCorner(msg.getCorner());
				Configuration config=ClientProxy.config;
				if(msg.getCorner()==0) {
					config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("downLeft");
					config.save();
				}
				if(msg.getCorner()==1) {
					config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("downRight");
					config.save();
				}
				if(msg.getCorner()==2) {
					config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("upRight");
					config.save();
				}
				if(msg.getCorner()==3) {
					config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("upLeft");
					config.save();
				}
			}
		}
		return null;
	}

}
