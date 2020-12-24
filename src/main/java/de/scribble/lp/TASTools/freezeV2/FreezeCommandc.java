package de.scribble.lp.TASTools.freezeV2;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freezeV2.networking.FreezePacket;
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
		return "command.freeze.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
			
	}

}
