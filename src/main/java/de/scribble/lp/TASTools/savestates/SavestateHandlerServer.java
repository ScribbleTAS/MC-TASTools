package de.scribble.lp.TASTools.savestates;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freeze.FreezeHandler;
import de.scribble.lp.TASTools.freeze.FreezePacket;
import de.scribble.lp.TASTools.velocity.SavingVelocity;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class SavestateHandlerServer {

	public void saveState() {
		if(FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			File currentworldfolder= new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath() + File.separator + ModLoader.getLevelname());
			File targetsavefolder=null;
			if(!FreezeHandler.isServerFrozen()) {
				FreezeHandler.startFreezeServer();
				ModLoader.NETWORK.sendToAll(new FreezePacket(true));
			}
			ModLoader.NETWORK.sendToAll(new SavestatePacket());
			int i=1;
			while (i<=256) {
				if (i==256) {
					CommonProxy.logger.error("Couldn't make a savestate, there are too many savestates in the target directory");
					return;
				}
				if (i>256) {
					CommonProxy.logger.error("Aborting saving due to savestate count being greater than 256 for safety reasons");
					return;
				}
				targetsavefolder = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath() + File.separator + "savestates"+ File.separator + ModLoader.getLevelname() + "-Savestate"+Integer.toString(i));
				if (!targetsavefolder.exists()) {
					break;
				}
				i++;
			}
			if(VelocityEvents.velocityenabledServer) {
				List<EntityPlayerMP> players= FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
				for(int o=0;o<players.size();o++) {
					for(int e=0;e<FreezeHandler.entity.size();e++) {
						if(FreezeHandler.entity.get(e).getPlayername().equals(players.get(o).getName())){
							new SavingVelocity().saveVelocityCustom(FreezeHandler.entity.get(o).getMotionX(), FreezeHandler.entity.get(o).getMotionY(), FreezeHandler.entity.get(o).getMotionZ(), new File(currentworldfolder.getPath()+File.separator+players.get(o).getName()+"_velocity.txt"));
						}
					}
				}
			}
			FMLCommonHandler.instance().getMinecraftServerInstance().saveAllWorlds(false);
			try {
				copyDirectory(currentworldfolder, targetsavefolder, new String[] {" "});
				
			} catch (IOException e) {
				CommonProxy.logger.error("Could not copy the directory "+currentworldfolder.getPath()+" to "+targetsavefolder.getPath()+" for some reason (Savestate save)");
				e.printStackTrace();
			}
			ModLoader.NETWORK.sendToAll(new SavestatePacket());
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
//TODO Add Info File like in SavestateHandlerClient
	    /**
	     * Delete directory contents recursively. Leaves the specified starting directory empty. Ignores files / dirs listed in "ignore" array.
	     * 
	     * @param dir directory to delete
	     * @param ignore ignored files
	     * @return true on success
	     */
	 private boolean deleteDirContents(File dir, String[] ignore){
	    	
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
