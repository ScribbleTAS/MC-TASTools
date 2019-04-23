package de.scribble.lp.TASTools.savestates;

import java.util.List;

import de.scribble.lp.TASTools.savestates.gui.GuiSavestateLoadingScreen;
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
			if(args.length==1&&args[0].equalsIgnoreCase("save")) {
				new SavestateHandlerClient().saveState();
			}
			else if(args.length==1&&args[0].equalsIgnoreCase("load")) {
				new SavestateHandlerClient().loadLastSavestate();
			}
			else if(args.length==1&&args[0].equalsIgnoreCase("gui")) {
				Minecraft.getMinecraft().displayGuiScreen(new GuiSavestateLoadingScreen());
			}
		}
		
	}
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		if(args.length==1) {
			return getListOfStringsMatchingLastWord(args, new String[] {"save","load","gui"});
		}else {
			return super.getTabCompletions(server, sender, args, targetPos);
		}
	}
}
