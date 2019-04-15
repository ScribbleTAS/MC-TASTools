package de.scribble.lp.TASTools.velocity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freeze.FreezePacket;
import de.scribble.lp.TASTools.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class ReapplyingVelocity {
	
	private double [] motion= {0,0,0};

	public double[] getVelocity(EntityPlayer player, File file){
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
					
					motion[0]=Double.parseDouble(vel[1]);
					motion[1]=Double.parseDouble(vel[2]);
					motion[2]=Double.parseDouble(vel[3]);

				}
			}			
			Buff.close();
		}catch (IOException e) {
				e.printStackTrace();
		}
		return motion;
	}
}
