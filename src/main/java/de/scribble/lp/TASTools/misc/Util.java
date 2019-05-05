package de.scribble.lp.TASTools.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.duping.DupeEvents;
import de.scribble.lp.TASTools.keystroke.GuiKeystrokes;
import de.scribble.lp.TASTools.savestates.SavestateEvents;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Util {
	public String getLevelNamefromServer() {
		String out=null;
		File file = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath()+File.separator+"server.properties");
		if (file.exists()) {
			try {
				BufferedReader buf = new BufferedReader(new FileReader(file));
				while (true) {
					String s=buf.readLine();
					if (s.startsWith("level-name")) {
						String [] temp=s.split("=");
						out=temp[1];
						break;
					}
				}
				buf.close();
	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return out;
	}
	@SideOnly(Side.SERVER)
	public void reloadServerconfig() {
		CommonProxy.serverconfig.load();
		CommonProxy.enableServerDuping=CommonProxy.serverconfig.get("Duping","Enable",false,"Enables duping on the Server").getBoolean();
		ModLoader.freezeenabledMP=CommonProxy.serverconfig.get("Freeze","Enabled", false, "Freezes the game when starting the Server").getBoolean();
		VelocityEvents.velocityenabledServer=CommonProxy.serverconfig.get("Velocity","Enabled",true,"Saves and applies Velocity when joining/leaving the server").getBoolean();
		CommonProxy.serverconfig.save();
	}
	@SideOnly(Side.CLIENT)
	public void reloadClientconfig() {
		GuiKeystrokes.guienabled=ClientProxy.config.get("Keystrokes","Enabled", true, "Activates the keystrokes on startup").getBoolean();
		String position=ClientProxy.config.get("Keystrokes","CornerPos", "downLeft", "Sets the Keystroke to that specific corner. Options: downLeft,downRight,upRight,upLeft").getString();
		DupeEvents.dupingenabled=ClientProxy.config.get("Duping","Enabled", false, "Activates the duping on startup").getBoolean();
		VelocityEvents.velocityenabledClient=ClientProxy.config.get("Velocity", "Enabled", true, "Activates velocity saving on startup").getBoolean();
		ModLoader.freezeenabledSP=ClientProxy.config.get("Freeze","Enabled", false, "Freezes the game when joining singleplayer").getBoolean();
		SavestateEvents.savestatepauseenabled=ClientProxy.config.get("Savestate", "CustomGui", true, "Enables 'Make a Savestate' Button in the pause menu. Disable this if you use other mods that changes the pause menu").getBoolean();
		GuiOverlayLogo.potionenabled=ClientProxy.config.get("GuiPotion","Enabled",true,"Enables the MC-TAS-Logo in the Gui").getBoolean();
	}
}
