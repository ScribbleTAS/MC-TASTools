package de.scribble.lp.TASTools.endderdragon;

import java.util.List;

import com.google.common.base.Predicate;

import de.scribble.lp.TASTools.CommonProxy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.server.MinecraftServer;

public class DragonCommandc extends CommandBase{
	@Override
	public String getName() {
		return "dragon";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/dragon <phase>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		List<Entity> list = server.getWorld(-1).getLoadedEntityList();
		List<EntityDragon> dragons=null;
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getDisplayName().equals("EnderDragon")) {
				dragons.add((EntityDragon) list.get(i));
			}
		}
		CommonProxy.logger.info("Found a dragon!");
			if(args.length==1&&args[0].equalsIgnoreCase("dying")) {
				
				for (int i = 0; i ==dragons.size(); i++) {
					new PhaseManager(dragons.get(i)).setPhase(PhaseList.DYING);
				}
				
		}
	}

}
