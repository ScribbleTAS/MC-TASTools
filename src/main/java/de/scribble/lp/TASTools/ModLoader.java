package de.scribble.lp.TASTools;

import de.scribble.lp.TASTools.duping.DupeCommandc;
import de.scribble.lp.TASTools.freeze.FreezeCommandc;
import de.scribble.lp.TASTools.freeze.FreezeHandler;
import de.scribble.lp.TASTools.misc.Util;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@Mod(modid = "tastools", name = "TAS-Tools")
public class ModLoader {
	 
	
	@Instance
	public static ModLoader instance = new ModLoader();
	
	@SidedProxy(serverSide = "de.scribble.lp.TASTools.proxy.CommonProxy", clientSide = "de.scribble.lp.TASTools.proxy.ClientProxy")
	public static CommonProxy proxy;
	
	public static SimpleNetworkWrapper NETWORK;

	public static boolean freezeenabledSP;
	public static boolean freezeenabledMP;
	
	public static String levelname;
	

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
			levelname=new Util().getLevelNamefromServer();
		}
		if(!CommonProxy.isDupeModLoaded()) {
			ev.registerServerCommand(new DupeCommandc());
		}
		ev.registerServerCommand(new TastoolsCommandc());
		ev.registerServerCommand(new FreezeCommandc());
	}
	
	@EventHandler
	public void serverStarted(FMLServerStartedEvent ev) {
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			if(freezeenabledMP) {
				FreezeHandler.startFreezeServer();
				FreezeHandler.startFreezeClient();
			}
		}
	}
}
