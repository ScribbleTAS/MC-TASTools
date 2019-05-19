package de.scribble.lp.TASTools.velocity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReapplyingVelocity {
	
	public double[] getVelocity(File file){
		double [] motion= {0,0,0};
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
	public float getFalldistance(File file) {
		float falldistance=0F;
		try {
			BufferedReader Buff = new BufferedReader(new FileReader(file));
			String s;
			while (true){
				if((s=Buff.readLine()).equalsIgnoreCase("END")){
					break;
				}
				else if(s.startsWith("#")){				//comments
					continue;
				}
				else if(s.startsWith("Falldistance;")) {
					String[] fal=s.split(";");
					
					falldistance=Float.parseFloat(fal[1]);
					

				}
			}			
			Buff.close();
		}catch (IOException e) {
				e.printStackTrace();
		}
		return falldistance;
	}
}
