package de.scribble.lp.TASTools.enderdragon;

import java.util.List;

import com.google.common.base.Predicate;

import de.scribble.lp.TASTools.CommonProxy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class DragonCommandc extends CommandBase{
	@Override
	public String getCommandName() {
		return "dragon";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/dragon <phase>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayerMP) {
			if (args.length < 1) {
				throw new WrongUsageException("/dragon <phase>", new Object[0]);
			}
			List<EntityDragon> dragons = sender.getEntityWorld().getEntities(EntityDragon.class, new Predicate<EntityDragon>() {
				@Override
				public boolean apply(EntityDragon e) {
					return !e.isDead;
				}
			});
			if (dragons.size() == 0) {
				CommonProxy.logger.error("Couldn't find a dragon :(");
				return;
			}
			if (args.length == 1 && args[0].equalsIgnoreCase("dying")) {
				for (int i = 0; i < dragons.size(); i++) {
					dragons.get(i).getPhaseManager().setPhase(PhaseList.DYING);
				}
			} else if (args.length == 1 && args[0].equalsIgnoreCase("holding_pattern")) {
				for (int i = 0; i < dragons.size(); i++) {
					dragons.get(i).getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
				}
			} else if (args.length == 1 && args[0].equalsIgnoreCase("hover")) {
				for (int i = 0; i < dragons.size(); i++) {
					dragons.get(i).getPhaseManager().setPhase(PhaseList.HOVER);
				}
			} else if (args.length == 1 && args[0].equalsIgnoreCase("landing")) {
				for (int i = 0; i < dragons.size(); i++) {
					dragons.get(i).getPhaseManager().setPhase(PhaseList.LANDING);
				}
			} else if (args.length == 1 && args[0].equalsIgnoreCase("landing_approach")) {
				for (int i = 0; i < dragons.size(); i++) {
					dragons.get(i).getPhaseManager().setPhase(PhaseList.LANDING_APPROACH);
				}
			} else if (args.length == 1 && args[0].equalsIgnoreCase("sitting_attacking")) {
				for (int i = 0; i < dragons.size(); i++) {
					dragons.get(i).getPhaseManager().setPhase(PhaseList.SITTING_ATTACKING);
				}
			} else if (args.length == 1 && args[0].equalsIgnoreCase("sitting_flaming")) {
				for (int i = 0; i < dragons.size(); i++) {
					dragons.get(i).getPhaseManager().setPhase(PhaseList.SITTING_FLAMING);
				}
			} else if (args.length == 1 && args[0].equalsIgnoreCase("sitting_scanning")) {
				for (int i = 0; i < dragons.size(); i++) {
					dragons.get(i).getPhaseManager().setPhase(PhaseList.SITTING_SCANNING);
				}
			} else if (args.length == 1 && args[0].equalsIgnoreCase("takeoff")) {
				for (int i = 0; i < dragons.size(); i++) {
					dragons.get(i).getPhaseManager().setPhase(PhaseList.TAKEOFF);
				}
			}
		}
	}
	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		if (args.length==1) {
			return getListOfStringsMatchingLastWord(args, new String[] {"dying","holding_pattern","hover","landing","landing_approach","sitting_attacking","sitting_flaming","sitting_scanning","takeoff"});
		}
		return super.getTabCompletionOptions(server, sender, args, targetPos);
	}
}
