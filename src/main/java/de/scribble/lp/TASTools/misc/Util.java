package de.scribble.lp.TASTools.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraftforge.fml.common.FMLCommonHandler;

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
	public void reloadServerconfig() {
		CommonProxy.serverconfig.load();
		CommonProxy.enableServerDuping=CommonProxy.serverconfig.get("Duping","Enable",false,"Enables duping on the Server").getBoolean();
		ModLoader.freezeenabledMP=CommonProxy.serverconfig.get("Freeze","Enabled", false, "Freezes the game when starting the Server").getBoolean();
		VelocityEvents.velocityenabledServer=CommonProxy.serverconfig.get("Velocity","Enabled",true,"Saves and applies Velocity when joining/leaving the server").getBoolean();
		CommonProxy.serverconfig.save();
	}
}
