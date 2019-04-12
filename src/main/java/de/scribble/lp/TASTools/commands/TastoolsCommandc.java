package de.scribble.lp.TASTools.commands;

import java.util.List;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.duping.DupeEvents;
import de.scribble.lp.TASTools.freeze.EntityDataStuff;
import de.scribble.lp.TASTools.freeze.FreezeEvents;
import de.scribble.lp.TASTools.freeze.FreezePacket;
import de.scribble.lp.TASTools.keystroke.GuiKeystrokes;
import de.scribble.lp.TASTools.proxy.ClientProxy;
import de.scribble.lp.TASTools.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class TastoolsCommandc extends CommandBase{
	public static boolean freeze=false;
	public static List<EntityDataStuff> entity;
	List<EntityPlayerMP> playerMP;
	private static FreezeEvents Freezer =new FreezeEvents();

	
	public List<String> emptyList(List<String> full){
		while(full.size()!=0){
			full.remove(0);
		}
		return full;
	}
	
	@Override
	public String getName() {
		return "tastools";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/tastools <keystrokes|duping|velocity>";
	}
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(sender instanceof EntityPlayer) {
			
			if (args.length==0) {
				sender.sendMessage(new TextComponentString(TextFormatting.GREEN+"TASTools v1.0"));
			}
			if(!CommonProxy.isTASModLoaded()) {
				//disable/enable keystrokes command
				if (args.length==1&&args[0].equalsIgnoreCase("keystrokes")) {
					Configuration config=ClientProxy.config;
					config.load();
					if (GuiKeystrokes.guienabled) {
						sender.sendMessage(new TextComponentTranslation("msg.keystrokes.disabled"));	//§cKeystrokes disabled
						GuiKeystrokes.guienabled=false;
						config.get("Keystrokes","Enabled", true, "Activates the keystrokes on startup").set(false);
						config.save();
					}else if (!GuiKeystrokes.guienabled) {
						sender.sendMessage(new TextComponentTranslation("msg.keystrokes.enabled"));		//§aKeystrokes enabled
						GuiKeystrokes.guienabled=true;
						config.get("Keystrokes","Enabled", true, "Activates the keystrokes on startup").set(true);
						config.save();
					}
				}
				//change corner
				
				if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&args[1].equalsIgnoreCase("downLeft")) {
					Configuration config=ClientProxy.config;
					GuiKeystrokes.changeCorner(0);
					config.load();
					config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("downLeft");
					config.save();
				}
				else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&args[1].equalsIgnoreCase("downRight")) {
					Configuration config=ClientProxy.config;
					GuiKeystrokes.changeCorner(1);
					config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("downRight");
					config.save();
				}
				else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&args[1].equalsIgnoreCase("upRight")) {
					Configuration config=ClientProxy.config;
					GuiKeystrokes.changeCorner(2);
					config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("upRight");
					config.save();
				}
				else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&args[1].equalsIgnoreCase("upLeft")) {
					Configuration config=ClientProxy.config;
					GuiKeystrokes.changeCorner(3);
					config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("upLeft");
					config.save();
				}
			}else {
				if (args[0].equalsIgnoreCase("keystrokes")) {
					sender.sendMessage(new TextComponentTranslation("msg.keystrokes.tasmoderr")); //Keystrokes are disabled due to the TASmod keystrokes. Please refer to /tasmod gui to change the settings
				}
			}
			
				//duping command
			if (args.length==1&&args[0].equalsIgnoreCase("duping")) {
				if(!CommonProxy.isDupeModLoaded()) {
					Configuration config=ClientProxy.config;
					config.load();
					if (DupeEvents.dupingenabled) {
						sender.sendMessage(new TextComponentTranslation("msg.duping.disabled")); //§cDuping disabled
						DupeEvents.dupingenabled=false;
						config.get("Duping","Enabled", true, "Activates the duping on startup").set(false);
						config.save();
					}else if (!DupeEvents.dupingenabled) {
						sender.sendMessage(new TextComponentTranslation("msg.duping.enabled")); //§aDuping enabled
						DupeEvents.dupingenabled=true;
						config.get("Duping","Enabled", true, "Activates the duping on startup").set(true);
						config.save();
					}
				}else {
					sender.sendMessage(new TextComponentTranslation("msg.duping.dupemoderr")); //§cDupeMod is loaded, so this command is disabled
				}
			}
		} if (args.length==1&&args[0].equalsIgnoreCase("freeze")) {
			if(!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
				if (!freeze) {
					freeze=true;
					ModLoader.NETWORK.sendToServer(new FreezePacket(true));
				}else if (freeze) {
					freeze=false;
					ModLoader.NETWORK.sendToServer(new FreezePacket(false));
				}
			}else {
				if (!freeze) {
					freeze=true;
					playerMP=FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
					if(playerMP.size()>0) {
						for(int i=0;i<=(playerMP.size());i++) {
							entity.set(i, new EntityDataStuff(playerMP.get(i).posX,
									playerMP.get(i).posY, playerMP.get(i).posZ, 
									playerMP.get(i).rotationPitch, playerMP.get(i).rotationYaw, playerMP.get(i).motionX, playerMP.get(i).motionY, playerMP.get(i).motionZ));
						}
						Freezer.playerMP=playerMP;
						MinecraftForge.EVENT_BUS.register(Freezer);
						CommonProxy.logger.info("Success!");
					}else {
						CommonProxy.logger.warn("No player is on the server");
					}
					
				}else if (freeze) {
					freeze=false;
					MinecraftForge.EVENT_BUS.unregister(Freezer);
				}
			}
		}
		
	}
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		if (args.length==1){
			return getListOfStringsMatchingLastWord(args, new String[] {"keystrokes","duping","freeze"});
		}
		else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&!CommonProxy.isTASModLoaded()) {
			return getListOfStringsMatchingLastWord(args, new String[] {"downLeft","downRight","upRight","upLeft"});
		}
		return super.getTabCompletions(server, sender, args, targetPos);
	}

}
