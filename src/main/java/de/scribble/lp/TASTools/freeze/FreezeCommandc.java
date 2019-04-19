package de.scribble.lp.TASTools.freeze;

import de.scribble.lp.TASTools.ModLoader;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class FreezeCommandc extends CommandBase {
	
	@Override
	public String getName() {
		return "freeze";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/freeze";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (!FreezeHandler.isServerFrozen()) {
			FreezeHandler.startFreezeServer();
			ModLoader.NETWORK.sendToAll(new FreezePacket(true));
		} else if (FreezeHandler.isServerFrozen()) {
			FreezeHandler.stopFreezeServer();
			ModLoader.NETWORK.sendToAll(new FreezePacket(false));
		}

	}

}
