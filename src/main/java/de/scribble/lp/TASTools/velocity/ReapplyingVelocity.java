package de.scribble.lp.TASTools.velocity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freeze.FreezePacket;
import de.scribble.lp.TASTools.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;

public class ReapplyingVelocity {
	
	static double motionX=0;
	static double motionY=0;
	static double motionZ=0;

	public void reapply(EntityPlayer player, File file){
		try{
			
			BufferedReader Buff = new BufferedReader(new FileReader(file));
			String s;
			while (true){
				if((s=Buff.readLine()).equalsIgnoreCase("END")){
					break;
				}
				else if(s.startsWith("#")){				//comments
					continue;
				}
				else if(s.startsWith("XYZ")) {
					String[] vel=s.split(";");
					
					player.motionX=Double.parseDouble(vel[1]);
					player.motionY=Double.parseDouble(vel[2]);
					player.motionZ=Double.parseDouble(vel[3]);
					player.velocityChanged=true;
				}
			}
			
			Buff.close();
		}catch (IOException e) {
				e.printStackTrace();
		}
	}
}
