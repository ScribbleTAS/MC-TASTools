package de.scribble.lp.TASTools.savestates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.savestates.gui.GuiSavestatePause;
import de.scribble.lp.TASTools.savestates.gui.GuiSavestateSavingScreen;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
/**
 * This code is heavily copied from <br> bspkrs on github <br> https://github.com/bspkrs/WorldStateCheckpoints/blob/master/src/main/java/bspkrs/worldstatecheckpoints/CheckpointManager.java
 *
 */
public class SavestateHandlerClient {
	Minecraft mc=Minecraft.getMinecraft();
	public boolean isSaving=false;
	private IPlayerFileData playerdatamanager;
	
	public static boolean copying=false;
	public static boolean deleting=false;
	
	protected static File currentworldfolder;
	protected static File targetsavefolder=null;
	protected static WorldSettings settings;
	protected static String foldername;
	protected static String worldname;
	
	
	public void saveState() {
		File currentworldfolder = new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName());
		File targetsavefolder=null;
		int i=1;
		while (i<=256) {
			if (i==256) {
				CommonProxy.logger.error("Couldn't make a savestate, there are too many savestates in the target directory");
				mc.displayGuiScreen(null);
				return;
			}
			if (i>256) {
				CommonProxy.logger.error("Aborting saving due to savestate count being greater than 256 for safety reasons");
				mc.displayGuiScreen(null);
				return;
			}
			targetsavefolder = new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator+"savestates"+File.separator+Minecraft.getMinecraft().getIntegratedServer().getFolderName()+"-Savestate"+Integer.toString(i));
			if (!targetsavefolder.exists()) {
				break;
			}
			i++;
		}
		if(!isSaving) {
			isSaving=true;
			copying=true;
			mc.displayGuiScreen(new GuiSavestateSavingScreen());
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			boolean flag =mc.getIntegratedServer().getWorld(mc.player.dimension).disableLevelSaving;
			mc.getIntegratedServer().getWorld(mc.player.dimension).disableLevelSaving=false;
			mc.getIntegratedServer().saveAllWorlds(false);
			mc.getIntegratedServer().getPlayerList().saveAllPlayerData();
			mc.getIntegratedServer().getWorld(mc.player.dimension).disableLevelSaving=flag;
			
			try {
				copyDirectory(currentworldfolder, targetsavefolder, new String[] {" "});
			} catch (IOException e) {
				CommonProxy.logger.error("Could not copy the directory "+currentworldfolder.getPath()+" to "+targetsavefolder.getPath()+" for some reason (Savestate save)");
				e.printStackTrace();
			}
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			mc.displayGuiScreen(null);
			isSaving=false;
		}
	}
	
	public void loadLastSavestate() {

		if(!isSaving) {
			currentworldfolder = new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator + mc.getIntegratedServer().getFolderName());
			foldername=mc.getIntegratedServer().getFolderName();
			worldname=mc.getIntegratedServer().getWorldName();
			//getting latest savestate
			int i=1;
			while(i<=256) {
				targetsavefolder = new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator+"savestates"+File.separator+Minecraft.getMinecraft().getIntegratedServer().getFolderName()+"-Savestate"+Integer.toString(i));
				if (!targetsavefolder.exists()) {
					if(i-1==0) {
						CommonProxy.logger.info("Couldn't find a valid savestate, abort loading the savestate!");
						return;
					}
					if(i>256) {
						CommonProxy.logger.error("Too many savestates found. Aborting loading for safety reasons");
					}
					targetsavefolder = new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator+"savestates"+File.separator+Minecraft.getMinecraft().getIntegratedServer().getFolderName()+"-Savestate"+Integer.toString(i-1));
					break;
				}
				i++;
				
			}
			
			SavestateEvents Events=new SavestateEvents();
			MinecraftForge.EVENT_BUS.register(Events);
			this.mc.world.sendQuittingDisconnectingPacket();
            this.mc.loadWorld((WorldClient)null);
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
    public void copyDirectory(File sourceLocation, File targetLocation, String[] ignore) throws IOException
    {
    	copying=true;
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
        copying=false;
    }

    /**
     * Delete directory contents recursively. Leaves the specified starting directory empty. Ignores files / dirs listed in "ignore" array.
     * 
     * @param dir directory to delete
     * @param ignore ignored files
     * @return true on success
     */
    public boolean deleteDirContents(File dir, String[] ignore){
    	
    	deleting=true;
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
        deleting=false;
        return true; 
    }

}
class SavestateEvents extends SavestateHandlerClient{
	private boolean launch=false;
	@SubscribeEvent
	public void onGuiScreenOpen(GuiOpenEvent ev) {
		if (ev.getGui() instanceof GuiDisconnected) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			deleteDirContents(currentworldfolder,  new String[] {" "});
			try {
				copyDirectory(targetsavefolder, currentworldfolder, new String[] {" "});
			} catch (IOException e) {
				CommonProxy.logger.error("Could not copy the directory "+currentworldfolder.getPath()+" to "+targetsavefolder.getPath()+" for some reason (Savestate load)");
				e.printStackTrace();
				MinecraftForge.EVENT_BUS.unregister(this);
				return;
			}
			FMLClientHandler.instance().getClient().launchIntegratedServer(foldername, worldname, null);
			MinecraftForge.EVENT_BUS.unregister(this);
		}
	}
}
