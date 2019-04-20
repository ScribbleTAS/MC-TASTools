package de.scribble.lp.TASTools.savestates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
/**
 * This code is heavily copied from <br> bspkrs on github <br> https://github.com/bspkrs/WorldStateCheckpoints/blob/master/src/main/java/bspkrs/worldstatecheckpoints/CheckpointManager.java
 *
 */
public class SavestateHandlerClient {
	Minecraft mc=Minecraft.getMinecraft();
	public boolean isSaving=false;
	
	public void saveState() {
		File currentworldfolder = new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName());
		File targetsavefolder = new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator+"savestates"+File.separator+"Savestate");
		if(!isSaving) {
			isSaving=true;
			mc.getIntegratedServer().saveAllWorlds(false);
			try {
				copyDirectory(currentworldfolder, targetsavefolder, new String[] {" "});
			} catch (IOException e) {
				e.printStackTrace();
			}
			isSaving=false;
		}
	}
	public void loadLastSavestate() {

		if(!isSaving) {
			File currentworldfolder = new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName());
			File targetsavefolder = new File(Minecraft.getMinecraft().mcDataDir, "saves" + File.separator+"savestates"+File.separator+"Savestate");
			String worldname= mc.world.getWorldInfo().getWorldName();
			
			//deleteDirAndContents(currentworldfolder);
			FMLCommonHandler.instance().getMinecraftServerInstance().stopServer();
			//FMLCommonHandler.instance().getMinecraftServerInstance().loadAllWorlds(targetsavefolder.getPath(), worldname, 0, null, null);
			//unloadWorldSilent();
			/*try {
				copyDirectory(targetsavefolder, currentworldfolder, new String[] {" "});
			}catch(IOException e) {
				e.printStackTrace();
			}*/
			
			
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
     * Delete directory and all contents recursively.
     * 
     * @param dir
     * @return
     */
    public boolean deleteDirAndContents(File dir)
    {
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                boolean success = deleteDirAndContents(new File(dir, children[i]));
                if (!success)
                    return false;
            }
        }
        return dir.delete();
    }

    /**
     * Delete directory contents recursively. Leaves the specified starting directory empty. Ignores files / dirs listed in "ignore" array.
     * 
     * @param dir directory to delete
     * @param ignore ignored files
     * @return true on success
     */
    public boolean deleteDirContents(File dir, String[] ignore)
    {
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


}
