package de.scribble.lp.TASTools;

import de.scribble.lp.TASTools.duping.DupeCommandc;
import de.scribble.lp.TASTools.enderdragon.DragonCommandc;
import de.scribble.lp.TASTools.freeze.FreezeCommandc;
import de.scribble.lp.TASTools.freeze.FreezeHandler;
import de.scribble.lp.TASTools.misc.Util;
import de.scribble.lp.TASTools.savestates.SavestateCommandc;
import de.scribble.lp.TASTools.savestates.SavestateHandlerServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

/**
 * 
 * @author ScribbleLP
 *
 */
@Mod(modid = "tastools", name = "TASTools", version =ModLoader.VERSION)
public class ModLoader {
	 
	
	@Instance
	public static ModLoader instance = new ModLoader();
	
	@SidedProxy(serverSide = "de.scribble.lp.TASTools.CommonProxy", clientSide = "de.scribble.lp.TASTools.ClientProxy")
	public static CommonProxy proxy;
	
	public static SimpleNetworkWrapper NETWORK;

	public static boolean freezeenabledSP;
	public static boolean freezeenabledMP;
	
	private static String levelname;
	public static boolean stopit;
	
	public static final String VERSION="${version}";		//If you want to know how I did this, check the build.gradle file under minceraft and "replace"
	public static final String MCVERSION="${mcversion}";
	

	@EventHandler
	public void preInit(FMLPreInitializationEvent ev) {
		proxy.preInit(ev);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent ev) {
		proxy.init(ev);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent ev) {
		proxy.postInit(ev);
	}
	
	@EventHandler
	public void serverStart(FMLServerStartingEvent ev) {
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			
		}
		if(!CommonProxy.isDupeModLoaded()) {
			ev.registerServerCommand(new DupeCommandc());
		}
		ev.registerServerCommand(new TastoolsCommandc());
		ev.registerServerCommand(new FreezeCommandc());
		ev.registerServerCommand(new SavestateCommandc());
		ev.registerServerCommand(new DragonCommandc());
	}
	
	@EventHandler
	public void serverStarted(FMLServerStartedEvent ev) {
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			if(freezeenabledMP) {
				FreezeHandler.startFreezeServer();
			}
		}
	}
	@EventHandler
	public void serverAboutToStart(FMLServerAboutToStartEvent ev) {
		if(FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			levelname=new Util().getLevelNamefromServer().replace("\\", "");
			stopit=CommonProxy.serverconfig.get("Savestate","LoadSavestate", false, "This is used for loading a Savestate. When entering /savestate load, this will be set to true, and the server will delete the current world and copy the latest savestate when starting.").getBoolean();
			if (stopit) {
				new SavestateHandlerServer().loadSavestateOnServerStart();
				CommonProxy.serverconfig.get("Savestate","LoadSavestate", false, "This is used for loading a Savestate. When entering /savestate load, this will be set to true, and the server will delete the current world and copy the latest savestate when starting.").set(false);
				CommonProxy.serverconfig.save();
			}
		}
	}
	public static String getLevelname() {
		return levelname;
	}
}
