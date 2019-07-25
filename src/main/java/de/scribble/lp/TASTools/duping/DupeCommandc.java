package de.scribble.lp.TASTools.duping;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class DupeCommandc extends CommandBase{
	
	@Override
	public String getCommandName() {
		
		return "dupe";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/dupe";
	}
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		World world =sender.getEntityWorld();
		if (!server.isDedicatedServer()&&server.getCurrentPlayerCount()==1) {
			if(sender instanceof EntityPlayer &&DupeEvents.dupingenabled){
				if(args.length==0||(args[0].equalsIgnoreCase("chest")&&args.length==1)){
					File file= new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator +Minecraft.getMinecraft().getIntegratedServer().getFolderName()+File.separator+"latest_dupe.txt");
					if (file.exists()) {
						new DupeEvents().startStopping((EntityPlayer)sender);
						new RefillingDupe().refill(file, (EntityPlayer)sender);
					}
				}
			}
		}
	}
}
