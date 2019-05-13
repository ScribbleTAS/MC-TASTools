package de.scribble.lp.TASTools.enderdragon;

import java.util.List;

import com.google.common.base.Predicate;

import de.scribble.lp.TASTools.CommonProxy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayerMP;

public class DragonCommandc extends CommandBase{
	@Override
	public String getCommandName() {
		return "dragon";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/dragon";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayerMP) {

			List<EntityDragon> dragons = sender.getEntityWorld().getEntities(EntityDragon.class, new Predicate<EntityDragon>() {
				@Override
				public boolean apply(EntityDragon e) {
					return !e.isDead;
				}
			});
			if (dragons.size() == 0) {
				CommonProxy.logger.error("Couldn't find a dragon :(");
				return;
			}else
				for (int i = 0; i < dragons.size(); i++) {
					dragons.get(i).forceNewTarget=true;
				}
			
		}
	}
}
