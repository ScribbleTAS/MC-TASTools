package de.scribble.lp.TASTools.commands;

import java.util.ArrayList;
import java.util.List;

import de.scribble.lp.TASTools.duping.DupeEvents;
import de.scribble.lp.TASTools.keystroke.GuiKeystrokes;
import de.scribble.lp.TASTools.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Configuration;

public class TastoolsCommandc extends CommandBase{
	
	public static void sendMessage(String msg){
		try{
			Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(msg));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public List<String> emptyList(List<String> full){
		while(full.size()!=0){
			full.remove(0);
		}
		return full;
	}
	
	@Override
	public String getName() {
		return "tastools";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "tastools <keystrokes|duping|velocity>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(sender instanceof EntityPlayer) {
			if (args.length==0) {
				sendMessage(TextFormatting.GREEN+"TAStools enabled!");
			}
			//keystrokes command
			else if (args.length==1&&args[0].equalsIgnoreCase("keystrokes")) {
				Configuration config=ClientProxy.config;
				config.load();
				if (GuiKeystrokes.guienabled) {
					sendMessage(TextFormatting.RED+"Keystrokes disabled");
					GuiKeystrokes.guienabled=false;
					config.get("Keystrokes","Enabled", true, "Activates the keystrokes on startup").set(false);
					config.save();
				}else if (!GuiKeystrokes.guienabled) {
					sendMessage(TextFormatting.GREEN+"Keystrokes disabled");
					GuiKeystrokes.guienabled=true;
					config.get("Keystrokes","Enabled", true, "Activates the keystrokes on startup").set(true);
					config.save();
				}
			}
			//duping command
			else if (args.length==1&&args[0].equalsIgnoreCase("duping")) {
				Configuration config=ClientProxy.config;
				config.load();
				if (DupeEvents.dupingenabled) {
					sendMessage(TextFormatting.RED+"Duping disabled");
					DupeEvents.dupingenabled=false;
					config.get("Duping","Enabled", true, "Activates the duping on startup").set(false);
					config.save();
				}else if (!DupeEvents.dupingenabled) {
					sendMessage(TextFormatting.GREEN+"Duping enabled");
					DupeEvents.dupingenabled=true;
					config.get("Duping","Enabled", true, "Activates the duping on startup").set(true);
					config.save();
				}
			}
		}
		
	}
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		if (args.length==1){
			return getListOfStringsMatchingLastWord(args, new String[] {"keystrokes","duping"});
		}
		return super.getTabCompletions(server, sender, args, targetPos);
	}

}
