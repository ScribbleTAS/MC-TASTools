package de.scribble.lp.TASTools.savestates;

import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

public class SavestateCommandc extends CommandBase{

	@Override
	public String getCommandName() {
		return "savestate";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/savestate <save|load>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		MinecraftServer server=FMLCommonHandler.instance().getMinecraftServerInstance();
		if (sender instanceof EntityPlayer) {
			if (!server.isDedicatedServer()) {
				if (args.length == 1 && args[0].equalsIgnoreCase("save")) {
					new SavestateHandlerClient().saveState();
				} else if (args.length == 1 && args[0].equalsIgnoreCase("load")) {
					ModLoader.NETWORK.sendTo(new SavestatePacket(false,1), (EntityPlayerMP) sender);
				}
			}else {
				if (args.length == 1 && args[0].equalsIgnoreCase("save")) {
					new SavestateHandlerServer().saveState();
					
				}else if(args.length == 1 && args[0].equalsIgnoreCase("load")) {
					new SavestateHandlerServer().setFlagandShutdown();
				}
			}
		}else {
			if (args.length == 1 && args[0].equalsIgnoreCase("save")&&server.isDedicatedServer()) {
				CommonProxy.logger.info("Making a Savestate! Hold on...");
				new SavestateHandlerServer().saveState();
				CommonProxy.logger.info("Done!");
			}else if(args.length == 1 && args[0].equalsIgnoreCase("load")) {
				new SavestateHandlerServer().setFlagandShutdown();
				CommonProxy.logger.info("Loading a savestate");
			}
		}
		
	}
	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		MinecraftServer server=FMLCommonHandler.instance().getMinecraftServerInstance();
		if(args.length==1) {
			return getListOfStringsMatchingLastWord(args, new String[] {"save","load"});
		}else {
			return super.addTabCompletionOptions(sender, args, pos);
		}
	}
}
