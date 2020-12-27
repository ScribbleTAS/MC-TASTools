package de.scribble.lp.TASTools.freezeV2;

import java.util.ArrayList;
import java.util.List;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freezeV2.networking.FreezePacket;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

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
		boolean freezeStatus=FreezeHandlerServer.isEnabled();
		if(args.length==0) {
			FreezeHandlerServer.activate(!freezeStatus);
			ModLoader.NETWORK.sendToAll(new FreezePacket(FreezeHandlerServer.isEnabled(), true));
		}else if(args.length==1) {
			if(server.getPlayerList().getPlayers().contains(server.getPlayerList().getPlayerByUsername(args[0]))){
				ModLoader.NETWORK.sendTo(new FreezePacket(!freezeStatus, true), server.getPlayerList().getPlayerByUsername(args[0]));
			}else {
				throw new CommandException("Couldn't find that player", new Object());
			}
		}else {
			throw new CommandException("Too many arguments", new Object());
		}
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		List<String> tabs=new ArrayList<String>();
		if(args.length==1) {
			tabs.addAll(getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()));
		}else {
			tabs.clear();
		}
		return tabs;
	}

}
