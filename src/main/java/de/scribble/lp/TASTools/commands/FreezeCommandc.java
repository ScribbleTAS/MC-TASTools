package de.scribble.lp.TASTools.commands;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freeze.FreezeHandler;
import de.scribble.lp.TASTools.freeze.FreezePacket;
import de.scribble.lp.TASTools.freeze.FreezePacketHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

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
		World world = sender.getEntityWorld();
		if (!server.isDedicatedServer()) {
			if (!ModLoader.freeze) {
				ModLoader.freeze = true;
				ModLoader.NETWORK.sendToServer(new FreezePacket(true));
				ModLoader.NETWORK.sendToAll(new FreezePacket(true));
			} else if (ModLoader.freeze) {
				ModLoader.freeze = false;
				ModLoader.NETWORK.sendToServer(new FreezePacket(false));
				ModLoader.NETWORK.sendToAll(new FreezePacket(false));
			}

		} else {
			if (!ModLoader.freeze) {
				ModLoader.freeze = true;
				FreezeHandler.startFreezeServer();
				ModLoader.NETWORK.sendToAll(new FreezePacket(true));


			} else if (ModLoader.freeze) {
				ModLoader.freeze = false;
				FreezeHandler.stopFreezeServer();
				ModLoader.NETWORK.sendToAll(new FreezePacket(false));
			}
		}

	}

}
