package de.scribble.lp.TASTools.commands;

import java.util.ArrayList;
import java.util.List;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freeze.EntityDataStuff;
import de.scribble.lp.TASTools.freeze.FreezeEventsServer;
import de.scribble.lp.TASTools.freeze.FreezePacket;
import de.scribble.lp.TASTools.freeze.FreezePacketHandler;
import de.scribble.lp.TASTools.proxy.CommonProxy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class FreezeCommandc extends CommandBase {
	public static boolean freeze = false;

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
		if (!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			if (!freeze) {
				freeze = true;
				ModLoader.NETWORK.sendToServer(new FreezePacket(true));
				ModLoader.NETWORK.sendToAll(new FreezePacket(true));
			} else if (freeze) {
				freeze = false;
				ModLoader.NETWORK.sendToServer(new FreezePacket(false));
				ModLoader.NETWORK.sendToAll(new FreezePacket(false));
			}

		} else {
			FreezePacketHandler Freezer = new FreezePacketHandler();
			if (!freeze) {
				freeze = true;
					Freezer.startFreeze();
					ModLoader.NETWORK.sendToAll(new FreezePacket(true));


			} else if (freeze) {
				freeze = false;
				Freezer.stopFreeze();
				ModLoader.NETWORK.sendToAll(new FreezePacket(false));
			}
		}

	}

}
