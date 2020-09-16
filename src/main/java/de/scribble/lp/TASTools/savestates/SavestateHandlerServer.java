package de.scribble.lp.TASTools.savestates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.io.Files;

import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freeze.FreezeHandler;
import de.scribble.lp.TASTools.freeze.FreezePacket;
import de.scribble.lp.TASTools.velocity.SavingVelocity;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class SavestateHandlerServer {
	private boolean isSaving;
	protected static File currentworldfolder;
	protected static File targetsavefolder;
	public static int endtimer;
	
	public void saveState() {
		if(FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			if(!isSaving) {
				isSaving=true;
				this.currentworldfolder = new File(FMLCommonHandler.instance().getSavesDirectory().getPath()
						+ File.separator + ModLoader.getLevelname());
				targetsavefolder = null;
				if (!FreezeHandler.isServerFrozen()) {
					FreezeHandler.startFreezeServer();
					ModLoader.NETWORK.sendToAll(new FreezePacket(true));
				}
				ModLoader.NETWORK.sendToAll(new SavestatePacket());
				int i = 1;
				while (i <= 300) {
					if (i == 300) {
						CommonProxy.logger.error(
								"Couldn't make a savestate, there are too many savestates in the target directory");
						return;
					}
					if (i > 300) {
						CommonProxy.logger.error(
								"Aborting saving due to savestate count being greater than 300 for safety reasons");
						return;
					}
					targetsavefolder = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath()
							+ File.separator + "savestates" + File.separator + ModLoader.getLevelname() + "-Savestate"
							+ Integer.toString(i));
					if (!targetsavefolder.exists()) {
						break;
					}
					i++;
				}
				if (VelocityEvents.velocityenabledServer) {
					List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance()
							.getConfigurationManager().getPlayerList();
					for (int o = 0; o < players.size(); o++) {
						for (int e = 0; e < FreezeHandler.entity.size(); e++) {
							if (FreezeHandler.entity.get(e).getPlayername().equals(players.get(o).getName())) {
								new SavingVelocity().saveVelocityCustom(FreezeHandler.entity.get(o).getMotionX(),
										FreezeHandler.entity.get(o).getMotionY(),
										FreezeHandler.entity.get(o).getMotionZ(),
										FreezeHandler.entity.get(o).getFalldistance(),
										new File(currentworldfolder.getPath()+ File.separator + players.get(o).getName() + "_velocity.txt"));
							}
						}
					}
				}
				try {
					int[] incr = getInfoValues(getInfoFile(ModLoader.getLevelname()));
					if (incr[0] == 0) {
						saveInfo(getInfoFile(ModLoader.getLevelname()), null);
					} else {
						incr[0]++;
						saveInfo(getInfoFile(ModLoader.getLevelname()), incr);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().saveAllPlayerData();
				SavestateSaveEventsServer saver=new SavestateSaveEventsServer();
				saver.start();
			}
		}
	}

	public void setFlagandShutdown() {
		if(FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			if (!isSaving) {
				File currentworldfolder = new File(FMLCommonHandler.instance().getSavesDirectory().getPath() + File.separator + ModLoader.getLevelname());
				File targetsavefolder=null;
				//getting latest savestate
				int i=1;
				while(i<=300) {
					targetsavefolder = new File(FMLCommonHandler.instance().getSavesDirectory().getPath() + File.separator + "savestates"+ File.separator + ModLoader.getLevelname() + "-Savestate" + Integer.toString(i));
					if (!targetsavefolder.exists()) {
						if(i-1==0) {
							CommonProxy.logger.info("Couldn't find a valid savestate, abort serverstop!");
							return;
						}
						if(i>300) {
							CommonProxy.logger.error("Too many savestates found. Aborting serverstop for safety reasons");
							return;
						}
						targetsavefolder = new File(FMLCommonHandler.instance().getSavesDirectory().getPath() + File.separator + "savestates"+ File.separator + ModLoader.getLevelname() + "-Savestate" + Integer.toString(i-1));
						break;
					}
					i++;
				}
				try {
					int[] incr=getInfoValues(getInfoFile(ModLoader.getLevelname()));
					incr[1]++;
					saveInfo(getInfoFile(ModLoader.getLevelname()), incr);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				CommonProxy.serverconfig.get("Savestate","LoadSavestate", false, "This is used for loading a Savestate. When entering /savestate load, this will be set to true, and the server will delete the current world and copy the latest savestate when starting.").set(true);
				CommonProxy.serverconfig.save();
				FMLCommonHandler.instance().getMinecraftServerInstance().initiateShutdown();
			}
		}
	}
	private void copyDirectory(File sourceLocation, File targetLocation, String[] ignore) throws IOException
	    {
	        if (sourceLocation.isDirectory())
	        {
	            if (!targetLocation.exists())
	                targetLocation.mkdirs();

	            String[] children = sourceLocation.list();
	            for (int i = 0; i < children.length; i++)
	            {
	                boolean ignored = false;	
	                for (String str : ignore)
	                    if (str.equals(children[i]))
	                    {
	                        ignored = true;
	                        break;
	                    }

	                if (!ignored)
	                    copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]), ignore);
	            }
	        }
	        else
	        {
	            boolean ignored = false;
	            for (String str : ignore)
	                if (str.equals(sourceLocation.getName()))
	                {
	                    ignored = true;
	                    break;
	                }

	            if (!ignored)
	            {
	                InputStream in = new FileInputStream(sourceLocation);
	                OutputStream out = new FileOutputStream(targetLocation);

	                // Copy the bits from instream to outstream
	                byte[] buf = new byte[1024];
	                int len;
	                while ((len = in.read(buf)) > 0)
	                    out.write(buf, 0, len);

	                in.close();
	                out.close();
	            }
	        }
	    }

	/**
	 * Delete directory contents recursively. Leaves the specified starting
	 * directory empty. Ignores files / dirs listed in "ignore" array.
	 * 
	 * @param dir    directory to delete
	 * @param ignore ignored files
	 * @return true on success
	 */
	private boolean deleteDirContents(File dir, String[] ignore) {

		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean ignored = false;
				for (String str : ignore)
					if (str.equals(children[i])) {
						ignored = true;
						break;
					}

				if (!ignored) {
					boolean success = deleteDirContents(new File(dir, children[i]), ignore);
					if (!success)
						return false;
				}
			}
		} else {
			dir.delete();
		}
		return true;
	}

	public File getInfoFile(String worldname) {
		if(!new File(FMLCommonHandler.instance().getSavesDirectory().getPath() + File.separator + "savestates").exists())new File(FMLCommonHandler.instance().getSavesDirectory().getPath() + File.separator + "savestates").mkdir();
		
		File file = new File(FMLCommonHandler.instance().getSavesDirectory().getPath() + File.separator + "savestates"
				+ File.separator + ModLoader.getLevelname() + "-info.txt");
		return file;
	}
	
    public int[] getInfoValues(File file) throws IOException {
    	int[] out = {0,0};
    	if (file.exists()){
			try {
				BufferedReader buff = new BufferedReader(new FileReader(file));
				String s;
				int i = 0;
				while (i < 100) {
					s = buff.readLine();
					if (s.equalsIgnoreCase("END")) {
						break;
					} else if (s.startsWith("#")) {
						continue;
					} else if (s.startsWith("Total Savestates")) {
						String[] valls = s.split("=");
						out[0] = Integer.parseInt(valls[1]);
					} else if (s.startsWith("Total Rerecords")) {
						String[] valls = s.split("=");
						out[1] = Integer.parseInt(valls[1]);
					}
					i++;
				}
				buff.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
    	}
    	return out;
    }
	 
	public void saveInfo(File file, @Nullable int[] values) {
		StringBuilder output = new StringBuilder();
		output.append("#This file was generated by TASTools and diplays info about the usage of savestates!\n\n");
		if (values == null) {
			output.append("Total Savestates=1\nTotal Rerecords=0\nEND");
		} else {
			output.append("Total Savestates=" + Integer.toString(values[0]) + "\nTotal Rerecords="
					+ Integer.toString(values[1]) + "\nEND");
		}
		try {
			Files.write(output.toString().getBytes(), file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadSavestateOnServerStart() {
		CommonProxy.logger.info("Start loading the savestate");
		File currentworldfolder = new File(FMLCommonHandler.instance().getSavesDirectory().getPath() + File.separator + ModLoader.getLevelname());
		File targetsavefolder=null;
		//getting latest savestate
		int i=1;
		while(i<=300) {
			targetsavefolder = new File(FMLCommonHandler.instance().getSavesDirectory().getPath() + File.separator + "savestates"+ File.separator + ModLoader.getLevelname() + "-Savestate" + Integer.toString(i));
			if (!targetsavefolder.exists()) {
				if(i-1==0) {
					CommonProxy.logger.info("Couldn't find a valid savestate, abort loading the savestate!");
					return;
				}
				if(i>300) {
					CommonProxy.logger.error("Too many savestates found. Aborting loading for safety reasons");
					return;
				}
				targetsavefolder = new File(FMLCommonHandler.instance().getSavesDirectory().getPath() + File.separator + "savestates"+ File.separator + ModLoader.getLevelname() + "-Savestate" + Integer.toString(i-1));
				break;
			}
			i++;
		}
		deleteDirContents(currentworldfolder, new String[] { " " });
		try {
			copyDirectory(targetsavefolder, currentworldfolder, new String[] { " " });
		} catch (IOException e) {
			CommonProxy.logger.error("Could not copy the directory " + currentworldfolder.getPath() + " to "
					+ targetsavefolder.getPath() + " for some reason (Savestate load)");
			e.printStackTrace();
		}
		CommonProxy.logger.info("Done");
	}
	
	private class SavestateSaveEventsServer extends Thread {

		@Override
		public void run() {
			try {
				Thread.sleep(endtimer);
			} catch (InterruptedException e1) {
				CommonProxy.logger.catching(e1);
			}
			try {
				copyDirectory(currentworldfolder, targetsavefolder, new String[] { " " });

			} catch (IOException e) {
				CommonProxy.logger.error("Could not copy the directory " + currentworldfolder.getPath() + " to "
						+ targetsavefolder.getPath() + " for some reason (Savestate save)");
				e.printStackTrace();
			}
			isSaving = false;
			ModLoader.NETWORK.sendToAll(new SavestatePacket());
			return;
		}
	}
}
