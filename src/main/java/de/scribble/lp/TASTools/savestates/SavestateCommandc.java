package de.scribble.lp.TASTools.savestates;

import java.util.List;

import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.savestates.gui.GuiSavestateIngameMenu;
import de.scribble.lp.TASTools.savestates.gui.GuiSavestateSavingScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class SavestateCommandc extends CommandBase{

	@Override
	public String getName() {
		return "savestate";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/savestate <save|load>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayer) {
			if (!server.isDedicatedServer()) {
				if (args.length == 1 && args[0].equalsIgnoreCase("save")) {
					new SavestateHandlerClient().saveState();
				} else if (args.length == 1 && args[0].equalsIgnoreCase("load")) {
					new SavestateHandlerClient().loadLastSavestate();
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
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		if(args.length==1) {
			if(!server.isDedicatedServer()) {
				return getListOfStringsMatchingLastWord(args, new String[] {"save","load"});
			}else return getListOfStringsMatchingLastWord(args, new String[] {"save"});
		}else {
			return super.getTabCompletions(server, sender, args, targetPos);
		}
	}
}
