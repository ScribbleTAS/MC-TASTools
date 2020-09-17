package de.scribble.lp.TASTools.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MiscReapplying {

	private int timeuntilportal;

	public int getPortalTime(File file){
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
				else if(s.startsWith("TimeUntilPortal")) {
					String[] val=s.split("=");

					timeuntilportal=Integer.parseInt(val[1]);

				}
			}			
			Buff.close();
		}catch (IOException e) {
				e.printStackTrace();
		}
		return timeuntilportal;
	}
}
