package de.scribble.lp.TASTools.fishmanip;

import java.io.File;

import de.scribble.lp.TASTools.ModLoader;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class FishManipEvents {

	public static FishManip fishrigger;
	
	@SubscribeEvent
	public void onJoinServer(PlayerLoggedInEvent ev) {
		if(!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			fishrigger= new FishManip(new File(Minecraft.getMinecraft().mcDataDir,"saves"+File.separator+Minecraft.getMinecraft().getIntegratedServer().getFolderName()+File.separator+"fish_rigging.txt"));
		}else {
			fishrigger= new FishManip(new File(FMLCommonHandler.instance().getSavesDirectory().getPath()+File.separator+ModLoader.getLevelname()+File.separator+"fish_rigging.txt"));
		}
	}
	
}
