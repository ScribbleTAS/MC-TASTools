package de.scribble.lp.TASTools.flintrig;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class ZombieDropCommand extends CommandBase{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "zombiedrop";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "Meh";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(sender instanceof EntityPlayer) {
			ZombieDrops.enabled=!ZombieDrops.enabled;
			if(ZombieDrops.enabled) {
				sender.sendMessage(new TextComponentString("Zombies now drop only iron"));
			}else {
				sender.sendMessage(new TextComponentString("Zombies drops are normal again"));
			}
		}
	}
}
