package de.scribble.lp.TASTools;

import java.util.List;

import de.scribble.lp.TASTools.duping.DupeEvents;
import de.scribble.lp.TASTools.flintrig.FlintRig;
import de.scribble.lp.TASTools.keystroke.GuiKeystrokes;
import de.scribble.lp.TASTools.keystroke.KeystrokesPacket;
import de.scribble.lp.TASTools.misc.GuiOverlayLogo;
import de.scribble.lp.TASTools.misc.MiscPacket;
import de.scribble.lp.TASTools.misc.Util;
import de.scribble.lp.TASTools.savestates.SavestateEvents;
import de.scribble.lp.TASTools.savestates.SavestateHandlerClient;
import de.scribble.lp.TASTools.savestates.SavestateHandlerServer;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.config.Configuration;

public class TastoolsCommandc extends CommandBase{

	
	@Override
	public String getName() {
		return "tastools";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "command.tastools.usage";
	}
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		boolean isdedicated=server.isDedicatedServer();
		if(sender instanceof EntityPlayer) {
			if (args.length==0) {
				sender.sendMessage(new TextComponentTranslation("msg.misc.info", ModLoader.VERSION.toString(), ModLoader.MCVERSION.toString()));
			}
			//Checks if the TASMod is loaded. This mod has a keystroke system on it's own so TASTools will disable it's keypresses...
			if(!CommonProxy.isTASModLoaded()) {
				//disable/enable keystrokes command
				if (args.length==1&&args[0].equalsIgnoreCase("keystrokes")) {
					enableKeyBinds(isdedicated, sender, server);
				}
				
				//change corner
				
				if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&args[1].equalsIgnoreCase("downLeft")) {
					if (!isdedicated) {
						Configuration config=ClientProxy.config;
						GuiKeystrokes.changeCorner(0);
						config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("downLeft");
						config.save();
					}else {
						ModLoader.NETWORK.sendTo(new KeystrokesPacket(0), (EntityPlayerMP) sender);
					}
				}
				else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&args[1].equalsIgnoreCase("downRight")) {
					if (!isdedicated) {
						Configuration config=ClientProxy.config;
						GuiKeystrokes.changeCorner(1);
						config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("downRight");
						config.save();
					}else {
						ModLoader.NETWORK.sendTo(new KeystrokesPacket(1), (EntityPlayerMP) sender);
					}
				}
				else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&args[1].equalsIgnoreCase("upRight")) {
					if (!isdedicated) {
						Configuration config=ClientProxy.config;
						GuiKeystrokes.changeCorner(2);
						config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("upRight");
						config.save();
					}else {
						ModLoader.NETWORK.sendTo(new KeystrokesPacket(2), (EntityPlayerMP) sender);
					}
				}
				else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&args[1].equalsIgnoreCase("upLeft")) {
					if (!isdedicated) {
						Configuration config=ClientProxy.config;
						GuiKeystrokes.changeCorner(3);
						config.get("Keystrokes","CornerPos", "downLeft", "Sets the keystrokes to that specific corner. Options: downLeft,downRight,upRight,upLeft").set("upLeft");
						config.save();
					}else {
						ModLoader.NETWORK.sendTo(new KeystrokesPacket(3), (EntityPlayerMP) sender);
					}
				}
				//Change other peoples keystroke settings
				else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&server.getPlayerList().getPlayers().contains(server.getPlayerList().getPlayerByUsername(args[1]))) {
					notifyCommandListener(sender, this, "msg.keystroke.multiplayerchange", new TextComponentString(args[1]));
					ModLoader.NETWORK.sendTo(new KeystrokesPacket(), server.getPlayerList().getPlayerByUsername(args[1]));
				}
			//If the TASMod is loaded
			}else {
				if (args[0].equalsIgnoreCase("keystrokes")) {
					sender.sendMessage(new TextComponentTranslation("msg.keystrokes.tasmoderr")); //Keystrokes are disabled due to the TASmod keystrokes. Please refer to /tasmod gui to change the settings
				}
			}
			//freeze singleplayer
			if (args.length == 1 && args[0].equalsIgnoreCase("freeze")&&!isdedicated) {
				freezeSP(sender);
			}
			//freeze multiplayer
			else if (args.length == 1 && args[0].equalsIgnoreCase("freeze")) {
				freezeMP(sender);
			}
			//duping command
			if (args.length==1&&args[0].equalsIgnoreCase("duping")) {
				duping(isdedicated, sender);
			}
			//velocity singleplayer
			if (args.length == 1 && args[0].equalsIgnoreCase("velocity")&&!isdedicated) {
				velocitySP(sender);
			//velocity multiplayer
			} else if (args.length == 1 && args[0].equalsIgnoreCase("velocity")) {
				velocityMP(sender);
			}
			//reload config
			if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				reloadConfig(server, sender);
			//gui logo singleplayer
			} 
			if(args.length == 1 && args[0].equalsIgnoreCase("gui")) {
				guilogoSP(isdedicated, sender);
			} 
			if (args.length==2&&args[0].equalsIgnoreCase("gui")&&server.getPlayerList().getPlayers().contains(server.getPlayerList().getPlayerByUsername(args[1]))) {
				guilogoMP(args, server, sender);
				//Opens the savestate folder
			} 
			if(args.length==1&&args[0].equalsIgnoreCase("folder")){
				ModLoader.NETWORK.sendTo(new MiscPacket(2),(EntityPlayerMP)sender);
				//Changes the pause menu
			} 
			if(args.length==1&&args[0].equalsIgnoreCase("pausemenu")){
				pausemenu(sender);
			} 
			if(args.length==1&&args[0].equalsIgnoreCase("gameover")) {
				gameover(sender);
			} 
			if(args.length!=0&&args[0].equalsIgnoreCase("savestatetime")) {
				savestatetime(args, sender, server);
			} 
			if(args.length!=0&&args[0].equalsIgnoreCase("flintrig")) {
				flintRig(sender);
			}
			// Other than sender=Player starts here
		} else {

			if (args.length == 1 && args[0].equalsIgnoreCase("freeze")) {
				freezeCB();
				// velocity multiplayer

			} else if (args.length == 1 && args[0].equalsIgnoreCase("velocity")) {
				velocityCB();

			} else if (args.length == 1 && args[0].equalsIgnoreCase("keystrokes")) {
				CommonProxy.logger.warn("Cannot enable keystrokes");
			} else if (args.length == 2 && args[0].equalsIgnoreCase("keystrokes") && server.getPlayerList().getPlayers()
					.contains(server.getPlayerList().getPlayerByUsername(args[1]))) {
				CommonProxy.logger.info("Changed Keystroke-Settings for " + args[1]);
				ModLoader.NETWORK.sendTo(new KeystrokesPacket(), server.getPlayerList().getPlayerByUsername(args[1]));
				
			} else if (args.length == 1 && args[0].equalsIgnoreCase("gui")) {
				CommonProxy.logger.warn("Cannot enable the gui. Use /tastools gui <Playername>");
			}else if(args.length == 2 && args[0].equalsIgnoreCase("gui")&& server.getPlayerList().getPlayers()
					.contains(server.getPlayerList().getPlayerByUsername(args[1]))) {
				ModLoader.NETWORK.sendTo(new MiscPacket(1), server.getPlayerList().getPlayerByUsername(args[1]));
			}
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
			BlockPos targetPos) {
		if (args.length==1) {
			return getListOfStringsMatchingLastWord(args, new String[] {"keystrokes","duping","freeze","velocity","gui","reload","folder","pausemenu","gameover","savestatetime", "flintrig"});
		}
		else if (args.length==2&&args[0].equalsIgnoreCase("keystrokes")&&!CommonProxy.isTASModLoaded()) {
			List<String> tabs =getListOfStringsMatchingLastWord(args, new String[] {"downLeft","downRight","upRight","upLeft"});
			if(server.getPlayerList().getPlayers().size()>1) {
				tabs.addAll(getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()));
			}
			return tabs;
		}
		else if(args.length==2&&args[0].equalsIgnoreCase("gui")) {
			return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
		}
		return super.getTabCompletions(server, sender, args, targetPos);
	}
	
	
	private void enableKeyBinds(boolean isdedicated, ICommandSender sender, MinecraftServer server) {
		if(!isdedicated&&server.getCurrentPlayerCount()==1) {
			Configuration config=ClientProxy.config;
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
		}else {
			ModLoader.NETWORK.sendTo(new KeystrokesPacket(), (EntityPlayerMP) sender);
		}
	}
	private void freezeSP(ICommandSender sender) {
		if (ModLoader.freezeenabledSP) {
			sender.sendMessage(new TextComponentTranslation("msg.freezeClient.disabled")); // §cDisabled
			ModLoader.freezeenabledSP = false;
			ClientProxy.config.get("Freeze", "Enabled", false, "Freezes the game when joining singleplayer").set(false);
			ClientProxy.config.save();
		} else if (!ModLoader.freezeenabledSP) {
			sender.sendMessage(new TextComponentTranslation("msg.freezeClient.enabled")); // §aEnabled
			ModLoader.freezeenabledSP = true;
			ClientProxy.config.get("Freeze", "Enabled", false, "Freezes the game when joining singleplayer").set(true);
			ClientProxy.config.save();
		}
	}
	private void freezeMP(ICommandSender sender) {
		if (sender instanceof EntityPlayer) {
			if(ModLoader.freezeenabledMP) {
				sender.sendMessage(new TextComponentTranslation("msg.freezeServer.disabled")); //§cDisabled Freezing when starting the server
				ModLoader.freezeenabledMP=false;
				CommonProxy.serverconfig.get("Freeze","Enabled", false, "Freezes the game when starting the Server").set(false);
				CommonProxy.serverconfig.save();
			}else if (!ModLoader.freezeenabledMP) {
				sender.sendMessage(new TextComponentTranslation("msg.freezeServer.enabled")); //§aEnabled Freezing when starting the server
				ModLoader.freezeenabledMP=true;
				CommonProxy.serverconfig.get("Freeze","Enabled", false, "Freezes the game when starting the Server").set(true);
				CommonProxy.serverconfig.save();
			}
		}
	}
	private void duping(boolean isdedicated, ICommandSender sender) {
		if(!isdedicated) {
			if(!CommonProxy.isDupeModLoaded()) {
				Configuration config=ClientProxy.config;
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
	}
	private void velocitySP(ICommandSender sender) {
		if (VelocityEvents.velocityenabledClient) {
			sender.sendMessage(new TextComponentTranslation("msg.velocityClient.disabled"));	//§cDisabled Velocity when joining the world
			VelocityEvents.velocityenabledClient = false;
			ClientProxy.config.get("Velocity", "Enabled", true, "Activates velocity saving on startup")
					.set(false);
			ClientProxy.config.save();
		} else if (!VelocityEvents.velocityenabledClient) {
			sender.sendMessage(new TextComponentTranslation("msg.velocityClient.enabled"));		//§aEnabled Velocity when joining the world
			VelocityEvents.velocityenabledClient = true;
			ClientProxy.config.get("Velocity", "Enabled", true, "Activates velocity saving on startup")
					.set(true);
			ClientProxy.config.save();
		}
	}
	private void velocityMP(ICommandSender sender) {
		if (VelocityEvents.velocityenabledServer) {
			sender.sendMessage(new TextComponentTranslation("msg.velocityServer.disabled"));	//§cDisabled Velocity when logging into the server
			VelocityEvents.velocityenabledServer = false;
			CommonProxy.serverconfig.get("Velocity", "Enabled", true,
					"Saves and applies Velocity when joining/leaving the server").set(false);
			CommonProxy.serverconfig.save();
		} else if (!VelocityEvents.velocityenabledServer) {
			sender.sendMessage(new TextComponentTranslation("msg.velocityServer.enabled"));		//§aEnabled Velocity when logging into the server
			VelocityEvents.velocityenabledServer = true;
			CommonProxy.serverconfig.get("Velocity", "Enabled", true,
					"Saves and applies Velocity when joining/leaving the server").set(true);
			CommonProxy.serverconfig.save();
		}
	}
	private void reloadConfig(MinecraftServer server, ICommandSender sender) {
		if (!server.isDedicatedServer() && server.getCurrentPlayerCount() == 1) {
			ClientProxy.config.load();
			Util.reloadClientconfig(ClientProxy.config);
			notifyCommandListener(sender, this, "msg.misc.reload", new Object()); // Config reloaded!
		} else if(!server.isDedicatedServer() && server.getCurrentPlayerCount() > 1) {
			ModLoader.NETWORK.sendTo(new MiscPacket(0),(EntityPlayerMP) sender);
			notifyCommandListener(sender, this, "msg.misc.reload", new Object());
		} else {
			Util.reloadServerconfig(CommonProxy.serverconfig);
			ModLoader.NETWORK.sendToAll(new MiscPacket(0));
			notifyCommandListener(sender, this, "msg.misc.reload", new Object()); // Config reloaded!
		}
	}
	private void guilogoSP(boolean isdedicated, ICommandSender sender){
		if(!isdedicated){
			if(GuiOverlayLogo.potionenabled) {
				sender.sendMessage(new TextComponentTranslation("msg.gui.disabled")); //§cDisabled Logo in HUD
				GuiOverlayLogo.potionenabled=false;
				ClientProxy.config.get("GuiPotion","Enabled",true,"Enables the MC-TAS-Logo in the Gui").set(false);
				ClientProxy.config.save();
			}else if(!GuiOverlayLogo.potionenabled) {
				sender.sendMessage(new TextComponentTranslation("msg.gui.enabled"));	//§aEnabled Logo in HUD
				GuiOverlayLogo.potionenabled=true;
				ClientProxy.config.get("GuiPotion","Enabled",true,"Enables the MC-TAS-Logo in the Gui").set(true);
				ClientProxy.config.save();
			}
		}else {
			ModLoader.NETWORK.sendTo(new MiscPacket(1), (EntityPlayerMP)sender);
		}
	}
	private void guilogoMP(String[] args, MinecraftServer server, ICommandSender sender) {
		notifyCommandListener(sender, this, "msg.gui.multiplayerchange", new TextComponentString(args[1]));
		ModLoader.NETWORK.sendTo(new MiscPacket(1), server.getPlayerList().getPlayerByUsername(args[1]));
	}
	private void pausemenu(ICommandSender sender) {
		if (SavestateEvents.savestatepauseenabled) {
			sender.sendMessage(new TextComponentTranslation("msg.pausegui.disabled"));	//§cDisabled Velocity when joining the world
			SavestateEvents.savestatepauseenabled = false;
			ClientProxy.config.get("Savestate", "CustomGui", true, "Enables 'Make a Savestate' Button in the pause menu. Disable this if you use other mods that changes the pause menu")
					.set(false);
			ClientProxy.config.save();
		} else if (!SavestateEvents.savestatepauseenabled) {
			sender.sendMessage(new TextComponentTranslation("msg.pausegui.enabled"));		//§aEnabled Velocity when joining the world
			SavestateEvents.savestatepauseenabled = true;
			ClientProxy.config.get("Savestate", "CustomGui", true, "Enables 'Make a Savestate' Button in the pause menu. Disable this if you use other mods that changes the pause menu")
					.set(true);
			ClientProxy.config.save();
		}
	}
	private void gameover(ICommandSender sender) {
		SavestateEvents.reloadgameoverenabled=!SavestateEvents.reloadgameoverenabled;
		ClientProxy.config.get("Savestate", "CustomGui", true, "Enables 'Make a Savestate' Button in the pause menu. Disable this if you use other mods that changes the pause menu")
		.set(SavestateEvents.reloadgameoverenabled);
		ClientProxy.config.save();
		String msg= SavestateEvents.reloadgameoverenabled ? "msg.gameover.enabled":"msg.gameover.disabled"; 
		sender.sendMessage(new TextComponentTranslation(msg));
	}
	private void savestatetime(String[] args, ICommandSender sender, MinecraftServer server) {
		if (args.length==1) {
			sender.sendMessage(new TextComponentTranslation("command.savestatetime.info")); //Set the time it takes to savestate here. Increase the time when playing on big worlds! Usage: /tastools savestatetime <timeinMillis>
			return;
		}else if(args.length==2) {
			try {
				int i= Integer.parseInt(args[1]);
				if(i>50000) {
					sender.sendMessage(new TextComponentTranslation("command.savestatetime.toomuch")); //§cThe number is too high! If your world doesn't save correctly with a time below 50000 please contact the author
					return;
				}else if(i<0) {
					sender.sendMessage(new TextComponentTranslation("command.savestatetime.toolow")); //Please put in positive numbers!
					return;
				}else if(i<1000) {
					sender.sendMessage(new TextComponentTranslation("command.savestatetime.warn")); //Warning! A time lower than 1000 can cause issues with savestating! The suggested value is between 1000 and 5000
				}
				if (!server.isDedicatedServer()) {
					ClientProxy.config.get("Savestatetime","TimeInMillis", 5000, "Set's the delay between Minecraft saving all chunks and the mod starting to copy files... Big worlds need a bit longer to save the world, so here you can adjust that")
					.set(i);
					SavestateHandlerClient.savetimer=i;
					ClientProxy.config.save();
					sender.sendMessage(new TextComponentTranslation("command.savestatetime.success",args[1]));
				}else {
					CommonProxy.serverconfig.get("TimeToSave","TimeInMillis", 5000, "Set's the delay between Minecraft saving all chunks and the mod starting to copy files... Big worlds need a bit longer to save the world, so here you can adjust that").set(i);
					SavestateHandlerServer.endtimer=i;
					CommonProxy.serverconfig.save();
					sender.sendMessage(new TextComponentTranslation("command.savestatetime.success",args[1]));
				}
			}catch(NumberFormatException e) {
				sender.sendMessage(new TextComponentTranslation("command.savestatetime.waytoohigh", args[1]));
			}
		}
	}
	private void freezeCB() {
		if (ModLoader.freezeenabledMP) {
			CommonProxy.logger.info("Disabled Serverside settings for 'freeze'");
			ModLoader.freezeenabledMP = false;
			CommonProxy.serverconfig.get("Freeze", "Enabled", false, "Freezes the game when joining the Server")
					.set(false);
			CommonProxy.serverconfig.save();
		} else if (!ModLoader.freezeenabledMP) {
			CommonProxy.logger.info("Enabled Serverside settings for 'freeze'");
			ModLoader.freezeenabledMP = true;
			CommonProxy.serverconfig.get("Freeze", "Enabled", false, "Freezes the game when joining the Server")
					.set(true);
			CommonProxy.serverconfig.save();
		}
	}
	private void velocityCB() {
		if (VelocityEvents.velocityenabledServer) {
			CommonProxy.logger.info("Disabled Serverside settings for 'velocity'");
			VelocityEvents.velocityenabledServer = false;
			CommonProxy.serverconfig.get("Velocity", "Enabled", true,
					"Saves and applies Velocity when joining/leaving the server").set(false);
			CommonProxy.serverconfig.save();
		} else if (!VelocityEvents.velocityenabledServer) {
			CommonProxy.logger.info("Enabled Serverside settings for 'velocity'");
			VelocityEvents.velocityenabledServer = true;
			CommonProxy.serverconfig.get("Velocity", "Enabled", true,
					"Saves and applies Velocity when joining/leaving the server").set(true);
			CommonProxy.serverconfig.save();
		}
	}
	private void flintRig(ICommandSender sender) throws CommandException {
		FlintRig.enable = !FlintRig.enable;
		if (sender.canUseCommand(2, "Heck")) {
			if (FlintRig.enable) {
				sender.sendMessage(new TextComponentTranslation("msg.flintrig.enable"));	//§aNow only flintdrops
			} else {
				sender.sendMessage(new TextComponentTranslation("msg.flintrig.disable"));	//§cBack to normal
			}
		} else {
			throw new CommandException("You do not have the permission to use this command!", new Object());
		}
	}
}
