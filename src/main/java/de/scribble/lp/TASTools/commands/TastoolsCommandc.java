package de.scribble.lp.TASTools.commands;

import java.util.List;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.duping.DupeEvents;
import de.scribble.lp.TASTools.keystroke.GuiKeystrokes;
import de.scribble.lp.TASTools.keystroke.KeystrokesPacket;
import de.scribble.lp.TASTools.proxy.ClientProxy;
import de.scribble.lp.TASTools.proxy.CommonProxy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.config.Configuration;

public class TastoolsCommandc extends CommandBase{

	
	@Override
	public String getName() {
		return "tastools";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/tastools <keystrokes|duping|velocity>";
	}
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		boolean isdedicated=server.isDedicatedServer();
		if(sender instanceof EntityPlayer) {
			if (args.length==0) {
				sender.sendMessage(new TextComponentString(TextFormatting.GREEN+"TASTools v1.0"));
			}
			if(!CommonProxy.isTASModLoaded()) {
				//disable/enable keystrokes command
				if (args.length==1&&args[0].equalsIgnoreCase("keystrokes")) {
					
					if(!isdedicated) {
						Configuration config=ClientProxy.config;
						if (GuiKeystrokes.guienabled) {
							sender.sendMessage(new TextComponentTranslation("msg.keystrokes.disabled"));	//§cKeystrokes disabled
							GuiKeystrokes.guienabled=false;
							config.get("Keystrokes","Enabled", true, "Activates the keystrokes on startup").set(false);
							config.save();
						}else if (!GuiKeystrokes.guienabled) {
							sender.sendMessage(new TextComponentTranslation("msg.keystrokes.enabled"));		//§aKeystrokes enabled
							GuiKeystrokes.guienabled=true;
							config.get("Keystrokes","Enabled", true, "Activates the keystrokes on startup").set(true);
							config.save();
						}
					}else {
						ModLoader.NETWORK.sendTo(new KeystrokesPacket(), (EntityPlayerMP) sender);
					}
				}
				
				//change corner
				
				if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&args[1].equalsIgnoreCase("downLeft")) {
					if (!isdedicated) {
						Configuration config=ClientProxy.config;
						GuiKeystrokes.changeCorner(0);
						config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("downLeft");
						config.save();
					}else {
						ModLoader.NETWORK.sendTo(new KeystrokesPacket(0), (EntityPlayerMP) sender);
					}
				}
				else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&args[1].equalsIgnoreCase("downRight")) {
					if (!isdedicated) {
						Configuration config=ClientProxy.config;
						GuiKeystrokes.changeCorner(1);
						config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("downRight");
						config.save();
					}else {
						ModLoader.NETWORK.sendTo(new KeystrokesPacket(1), (EntityPlayerMP) sender);
					}
				}
				else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&args[1].equalsIgnoreCase("upRight")) {
					if (!isdedicated) {
						Configuration config=ClientProxy.config;
						GuiKeystrokes.changeCorner(2);
						config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("upRight");
						config.save();
					}else {
						ModLoader.NETWORK.sendTo(new KeystrokesPacket(2), (EntityPlayerMP) sender);
					}
				}
				else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&args[1].equalsIgnoreCase("upLeft")) {
					if (!isdedicated) {
						Configuration config=ClientProxy.config;
						GuiKeystrokes.changeCorner(3);
						config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("upLeft");
						config.save();
					}else {
						ModLoader.NETWORK.sendTo(new KeystrokesPacket(3), (EntityPlayerMP) sender);
					}
				}
				else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&server.getPlayerList().getPlayers().contains(server.getPlayerList().getPlayerByUsername(args[1]))) {
					notifyCommandListener(sender, this, "msg.keystroke.multiplayerchange", new TextComponentString(args[1]));
					ModLoader.NETWORK.sendTo(new KeystrokesPacket(), server.getPlayerList().getPlayerByUsername(args[1]));
				}
			}else {
				if (args[0].equalsIgnoreCase("keystrokes")) {
					sender.sendMessage(new TextComponentTranslation("msg.keystrokes.tasmoderr")); //Keystrokes are disabled due to the TASmod keystrokes. Please refer to /tasmod gui to change the settings
				}
			}
			
				//duping command
			if (args.length==1&&args[0].equalsIgnoreCase("duping")) {
				if(!isdedicated) {
					if(!CommonProxy.isDupeModLoaded()) {
						Configuration config=ClientProxy.config;
						config.load();
						if (DupeEvents.dupingenabled) {
							sender.sendMessage(new TextComponentTranslation("msg.duping.disabled")); //§cDuping disabled
							DupeEvents.dupingenabled=false;
							config.get("Duping","Enabled", true, "Activates the duping on startup").set(false);
							config.save();
						}else if (!DupeEvents.dupingenabled) {
							sender.sendMessage(new TextComponentTranslation("msg.duping.enabled")); //§aDuping enabled
							DupeEvents.dupingenabled=true;
							config.get("Duping","Enabled", true, "Activates the duping on startup").set(true);
							config.save();
						}
					}else {
						sender.sendMessage(new TextComponentTranslation("msg.duping.dupemoderr")); //§cDupeMod is loaded, so this command is disabled
					}
				}
			}
		}else {
			if (args.length == 1 && args[0].equalsIgnoreCase("freeze")) {

			} else if (args.length == 1 && args[0].equalsIgnoreCase("keystrokes")) {
				CommonProxy.logger.warn("Cannot enable keystrokes");
			} else if (args.length == 2 && args[0].equalsIgnoreCase("keystrokes") && server.getPlayerList().getPlayers().contains(server.getPlayerList().getPlayerByUsername(args[1]))) {
				CommonProxy.logger.info("Changed Keystroke-Settings for "+args[1]);
				ModLoader.NETWORK.sendTo(new KeystrokesPacket(), server.getPlayerList().getPlayerByUsername(args[1]));
			}
		
	}
		
	}
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		if (args.length==1){
			return getListOfStringsMatchingLastWord(args, new String[] {"keystrokes","duping","freeze"});
		}
		else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&!CommonProxy.isTASModLoaded()) {
			List<String> tabs =getListOfStringsMatchingLastWord(args, new String[] {"downLeft","downRight","upRight","upLeft"});
			if(server.isDedicatedServer()) {
				tabs.addAll(getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()));
			}
			return tabs;
		}
		return super.getTabCompletions(server, sender, args, targetPos);
	}
}
