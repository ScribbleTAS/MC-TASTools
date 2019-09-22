package de.scribble.lp.TASTools.savestates;

import java.awt.image.BufferedImage;
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
import de.scribble.lp.TASTools.misc.Util;
import de.scribble.lp.TASTools.savestates.gui.GuiSavestateIngameMenu;
import de.scribble.lp.TASTools.savestates.gui.GuiSavestateLoadingScreen;
import de.scribble.lp.TASTools.savestates.gui.GuiSavestateSavingScreen;
import de.scribble.lp.TASTools.velocity.SavingVelocity;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
/**
 * This code is heavily 'inspired' from <br> bspkrs on github <br> https://github.com/bspkrs/WorldStateCheckpoints/blob/master/src/main/java/bspkrs/worldstatecheckpoints/CheckpointManager.java <br>
 * but it's more fitted to quickly load and save the savestates and removes extra gui overview. Oh, and no multithreadding, I have no idea how that works...
 */
public class SavestateHandlerClient {
	Minecraft mc=Minecraft.getMinecraft();
	protected static boolean isSaving=false;
	protected static boolean isLoading=false;
	protected static int endtimer=20;
	
	protected static File currentworldfolder;
	protected static File targetsavefolder=null;
	protected static WorldSettings settings;
	protected static String foldername;
	protected static String worldname;
	protected static BufferedImage screenshot;
	protected static String screenshotname;
	protected static BufferedImage worldIcon;
	
	public void saveState() {
		if(!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			
			currentworldfolder = new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName());
			targetsavefolder=null;
			worldname=Minecraft.getMinecraft().getIntegratedServer().getFolderName();
			
			List<EntityPlayerMP> players=FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerList();
			if (!isSaving && !isLoading) {
				isSaving = true;
				// Check for worlds in the savestate folder
				int i = 1;
				while (i <= 300) {
					if (i == 300) {
						CommonProxy.logger.error("Couldn't make a savestate, there are too many savestates in the target directory");
						isSaving = false;
						return;
					}
					if (i > 300) {
						CommonProxy.logger.error("Aborting saving due to savestate count being greater than 300 for safety reasons");
						isSaving = false;
						return;
					}
					targetsavefolder = new File(Minecraft.getMinecraft().mcDataDir,
							"saves" + File.separator + "savestates" + File.separator
									+ Minecraft.getMinecraft().getIntegratedServer().getFolderName() + "-Savestate"
									+ Integer.toString(i));
					if (!targetsavefolder.exists()) {
						screenshotname="Savestate "+i+".png";	//Setting the name of the savestate as the ScreenshotName
						break;
					}
					i++;
				}
				// Save the world
				isSaving = true;
				if(Util.enableSavestateScreenshotting) {
					screenshot = new Util().makeAscreenShot();
					worldIcon=new Util().createWorldIcon(screenshot);
				}
				ModLoader.NETWORK.sendToAll(new SavestatePacket());
				File file = new File(Minecraft.getMinecraft().mcDataDir,
						"saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName()
								+ File.separator + "latest_velocity.txt");
				
				//Normally works without this, but low tickrates make it so the player doesn't get saved
				Minecraft.getMinecraft().getIntegratedServer().getPlayerList().saveAllPlayerData();

				// For LAN-Servers
				if (players.size() > 1) {
					Minecraft.getMinecraft().getIntegratedServer().saveAllWorlds(false);
					if (!FreezeHandler.isServerFrozen()) {
						FreezeHandler.startFreezeServer();
						ModLoader.NETWORK.sendToAll(new FreezePacket(true));
					}

				}
				// Save the velocity
				if (VelocityEvents.velocityenabledClient) {
					new SavingVelocity().saveVelocity(mc.thePlayer, file);
					// Save Velocity for other LAN-Players
					if (players.size() > 1) {
						if (FreezeHandler.isServerFrozen()) {
							for (int i1 = 0; i1 < players.size(); i1++) {
								for (int j = 0; j < FreezeHandler.entity.size(); j++) {
									if (FreezeHandler.entity.get(j).getPlayername().equals(players.get(i1).getName())) {
										new SavingVelocity().saveVelocityCustom(
												FreezeHandler.entity.get(j).getMotionX(),
												FreezeHandler.entity.get(i1).getMotionY(),
												FreezeHandler.entity.get(i1).getMotionZ(), file);
									}
								}
							}
						}
					}
				}
				// Save the info file
				try {
					int[] incr = getInfoValues(getInfoFile(worldname));
					if (incr[0] == 0) {
						saveInfo(getInfoFile(worldname), null);
					} else {
						incr[0]++;
						saveInfo(getInfoFile(worldname), incr);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				SavestateSaveEventsClient lol = new SavestateSaveEventsClient();
				MinecraftForge.EVENT_BUS.register(lol);
			}else {
				CommonProxy.logger.error("Saving savestate is blocked by another action. If this is permanent, restart the game.");
			}
		}
	}
	
	public void loadLastSavestate() {
		if(!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			if(!isSaving&&!isLoading) {
				isLoading=true;
				currentworldfolder = new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator + mc.getIntegratedServer().getFolderName());
				foldername=mc.getIntegratedServer().getFolderName();
				worldname=Minecraft.getMinecraft().getIntegratedServer().getFolderName();
				//getting latest savestate
				int i=1;
				while(i<=300) {
					targetsavefolder = new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator+"savestates"+File.separator+Minecraft.getMinecraft().getIntegratedServer().getFolderName()+"-Savestate"+Integer.toString(i));
					if (!targetsavefolder.exists()) {
						if(i-1==0) {
							CommonProxy.logger.info("Couldn't find a valid savestate, abort loading the savestate!");
							isLoading=false;
							return;
						}
						if(i>300) {
							CommonProxy.logger.error("Too many savestates found. Aborting loading for safety reasons");
							isLoading=false;
							return;
						}
						targetsavefolder = new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator+"savestates"+File.separator+Minecraft.getMinecraft().getIntegratedServer().getFolderName()+"-Savestate"+Integer.toString(i-1));
						break;
					}
					i++;
				}
				try {
					int[] incr=getInfoValues(getInfoFile(worldname));
					incr[1]++;
					saveInfo(getInfoFile(worldname), incr);
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.mc.ingameGUI.getChatGUI().clearChatMessages();
				SavestateLoadEventsClient Events=new SavestateLoadEventsClient();
				MinecraftForge.EVENT_BUS.register(Events);
				FMLCommonHandler.instance().firePlayerLoggedOut(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerList().get(0));
	            this.mc.loadWorld((WorldClient)null);
	            this.mc.displayGuiScreen(new GuiSavestateLoadingScreen());
			}else {
				CommonProxy.logger.error("Loading savestate is blocked by another action. If this is permanent, restart the game.");
			}
		}
	}
	
	/**
     * Copy directory from source to target location recursively, ignoring strings in the "ignore" array. Target location will be created if
     * needed. Source directory is not copied, only its contents.
     * 
     * @param sourceLocation source
     * @param targetLocation target
     * @param ignore array of ignored names (strings)
     * @throws IOException
     */
    protected void copyDirectory(File sourceLocation, File targetLocation, String[] ignore) throws IOException
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
     * Delete directory contents recursively. Leaves the specified starting directory empty. Ignores files / dirs listed in "ignore" array.
     * 
     * @param dir directory to delete
     * @param ignore ignored files
     * @return true on success
     */
    protected boolean deleteDirContents(File dir, String[] ignore){
    	
        if (dir.isDirectory())
        {
            String[] children = dir.list();
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
                {
                    boolean success = deleteDirContents(new File(dir, children[i]), ignore);
                    if (!success)
                        return false;
                }
            }
        }
        else
        {
            dir.delete();
        }
        return true; 
    }

    public File getInfoFile(String worldname) {
    	if(!new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator+"savestates").exists()) new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator+"savestates").mkdir();

    	File file = new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator+"savestates"+File.separator+worldname+"-info.txt");
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
    public void saveInfo(File file,@Nullable int[] values) {
    	StringBuilder output= new StringBuilder();
    	output.append("#This file was generated by TASTools and diplays info about the usage of savestates!\n\n");
    	if(values==null) {
    		output.append("Total Savestates=1\nTotal Rerecords=0\nEND");
    	}else {
       		output.append("Total Savestates="+Integer.toString(values[0])+"\nTotal Rerecords="+Integer.toString(values[1])+"\nEND");
    	}
    	try{
    		Files.write(output.toString().getBytes(), file);
		} catch (IOException e) {
			e.printStackTrace();
    	}
    }
    public void displayLoadingScreen() {
    	if (mc.currentScreen instanceof GuiSavestateSavingScreen) {
			mc.displayGuiScreen(null);
		} else {
			mc.displayGuiScreen(new GuiSavestateSavingScreen());
		}
    }

	public void displayIngameMenu() {
		if (!SavestateEvents.savestatepauseenabled) {
			mc.displayGuiScreen(new GuiIngameMenu());
		} else {
			mc.displayGuiScreen(new GuiSavestateIngameMenu());
		}
	}
}
class SavestateSaveEventsClient extends SavestateHandlerClient{
	int tickspassed=0;
	@SubscribeEvent
	public void onTick(TickEvent ev) {
		if (ev.phase==Phase.START) {
			if (tickspassed>=endtimer) {
				mc.getIntegratedServer().saveAllWorlds(false);
				try {
					copyDirectory(currentworldfolder, targetsavefolder, new String[] {" "});
					
				} catch (IOException e) {
					CommonProxy.logger.error("Could not copy the directory "+currentworldfolder.getPath()+" to "+targetsavefolder.getPath()+" for some reason (Savestate save)");
					e.printStackTrace();
					isSaving=false;
					return;
				}
				if(Util.enableSavestateScreenshotting) {
					new Util().saveWorldIcon(worldIcon, targetsavefolder);
					new Util().saveScreenshotAt(targetsavefolder, screenshotname, screenshot);
				}
				ModLoader.NETWORK.sendToAll(new SavestatePacket(true));
				MinecraftForge.EVENT_BUS.unregister(this);
				isSaving=false;
				return;
			}
			tickspassed++;
		}
	}
}
class SavestateLoadEventsClient extends SavestateHandlerClient{
	private int tickspassed=0;
	@SubscribeEvent
	public void onTick(TickEvent ev) {
		if (ev.phase == Phase.START) {
			if (!mc.isIntegratedServerRunning()) {
				if (tickspassed >= endtimer) {
					if (!(mc.currentScreen instanceof GuiSavestateLoadingScreen)) {
						isLoading=false;
						return;
					}
					MinecraftForge.EVENT_BUS.unregister(this);
					deleteDirContents(currentworldfolder, new String[] { " " });
					try {
						copyDirectory(targetsavefolder, currentworldfolder, new String[] { " " });
					} catch (IOException e) {
						CommonProxy.logger.error("Could not copy the directory " + currentworldfolder.getPath() + " to "
								+ targetsavefolder.getPath() + " for some reason (Savestate load)");
						e.printStackTrace();
						MinecraftForge.EVENT_BUS.unregister(this);
						isLoading=false;
						return;
					}
					FMLClientHandler.instance().getClient().launchIntegratedServer(foldername, worldname, null);
					isLoading=false;
				}
				tickspassed++;
			}
		}
	}
}
