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
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Configuration;

public class TastoolsCommandc extends CommandBase{
	
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
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(sender instanceof EntityPlayer) {
			if (args.length==0) {
				sender.sendMessage(new TextComponentString(TextFormatting.GREEN+"TASTools v1.0"));
			}
			//disable/enable keystrokes command
			else if (args.length==1&&args[0].equalsIgnoreCase("keystrokes")) {
				Configuration config=ClientProxy.config;
				config.load();
				if (GuiKeystrokes.guienabled) {
					sender.sendMessage(new TextComponentString(TextFormatting.RED+"Keystrokes disabled"));
					GuiKeystrokes.guienabled=false;
					config.get("Keystrokes","Enabled", true, "Activates the keystrokes on startup").set(false);
					config.save();
				}else if (!GuiKeystrokes.guienabled) {
					sender.sendMessage(new TextComponentString(TextFormatting.GREEN+"Keystrokes disabled"));
					GuiKeystrokes.guienabled=true;
					config.get("Keystrokes","Enabled", true, "Activates the keystrokes on startup").set(true);
					config.save();
				}
			}
			//change corner
			else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&args[1].equalsIgnoreCase("downLeft")) {
				Configuration config=ClientProxy.config;
				GuiKeystrokes.changeCorner(0);
				config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("downLeft");
				config.save();
			}
			else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&args[1].equalsIgnoreCase("downRight")) {
				Configuration config=ClientProxy.config;
				GuiKeystrokes.changeCorner(1);
				config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("downRight");
				config.save();
			}
			else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&args[1].equalsIgnoreCase("upRight")) {
				Configuration config=ClientProxy.config;
				GuiKeystrokes.changeCorner(2);
				config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("upRight");
				config.save();
			}
			else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&args[1].equalsIgnoreCase("upLeft")) {
				Configuration config=ClientProxy.config;
				GuiKeystrokes.changeCorner(3);
				config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("upLeft");
				config.save();
			}
			
			//duping command
			else if (args.length==1&&args[0].equalsIgnoreCase("duping")) {
				Configuration config=ClientProxy.config;
				config.load();
				if (DupeEvents.dupingenabled) {
					sender.sendMessage(new TextComponentString(TextFormatting.RED+"Duping disabled"));
					DupeEvents.dupingenabled=false;
					config.get("Duping","Enabled", true, "Activates the duping on startup").set(false);
					config.save();
				}else if (!DupeEvents.dupingenabled) {
					sender.sendMessage(new TextComponentString(TextFormatting.GREEN+"Duping enabled"));
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
		else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")) {
			return getListOfStringsMatchingLastWord(args, new String[] {"downLeft","downRight","upRight","upLeft"});
		}
		return super.getTabCompletions(server, sender, args, targetPos);
	}

}
