package de.scribble.lp.TASTools;

import java.io.File;

import org.apache.logging.log4j.Logger;

import de.scribble.lp.TASTools.duping.DupeCommandc;
import de.scribble.lp.TASTools.duping.DupePacket;
import de.scribble.lp.TASTools.duping.DupePacketHandler;
import de.scribble.lp.TASTools.freeze.FreezeCommandc;
import de.scribble.lp.TASTools.freeze.FreezeEvents;
import de.scribble.lp.TASTools.freeze.FreezeHandler;
import de.scribble.lp.TASTools.freeze.FreezePacket;
import de.scribble.lp.TASTools.freeze.FreezePacketHandler;
import de.scribble.lp.TASTools.keystroke.KeystrokesPacket;
import de.scribble.lp.TASTools.keystroke.KeystrokesPacketHandler;
import de.scribble.lp.TASTools.misc.Util;
import de.scribble.lp.TASTools.savestates.SavestateCommandc;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "tastools", name = "TAS-Tools")
public class ModLoader {
	 
	
	@Instance
	public static ModLoader instance = new ModLoader();
	
	@SidedProxy(clientSide = "de.scribble.lp.TASTools.ClientProxy")
	private static ClientProxy proxy;
	public static SimpleNetworkWrapper NETWORK;

	public static boolean freezeenabledSP;
	public static boolean freezeenabledMP;
	public static String levelname;
	public static Logger logger;
	public static Configuration serverconfig;
	public static boolean enableServerDuping;
	private static boolean istasmodloaded;
	private static boolean isdupemodloaded;
	
	

	@EventHandler
	public void preInit(FMLPreInitializationEvent ev) {
		logger=ev.getModLog();
		logger.info("TAStools initialized");
		istasmodloaded=Loader.isModLoaded("tasmod");
		isdupemodloaded=Loader.isModLoaded("dupemod");
		
		MinecraftForge.EVENT_BUS.register(new VelocityEvents());
		
		NETWORK= NetworkRegistry.INSTANCE.newSimpleChannel("tastools");
		NETWORK.registerMessage(FreezePacketHandler.class, FreezePacket.class, 0, Side.SERVER);
		NETWORK.registerMessage(FreezePacketHandler.class, FreezePacket.class, 1, Side.CLIENT);
		NETWORK.registerMessage(KeystrokesPacketHandler.class, KeystrokesPacket.class, 2, Side.CLIENT);
		NETWORK.registerMessage(DupePacketHandler.class, DupePacket.class, 3, Side.SERVER);
		
		if(ev.getSide()==Side.SERVER) {
			serverconfig=new Configuration(new File(ev.getModConfigurationDirectory()+File.separator+"tastoolsSERVER.cfg"));
			enableServerDuping=serverconfig.get("Duping","Enable",false,"Enables duping on the Server").getBoolean();
			freezeenabledMP=serverconfig.get("Freeze","Enabled", false, "Freezes the game when starting the Server").getBoolean();
			VelocityEvents.velocityenabledServer=serverconfig.get("Velocity","Enabled",true,"Saves and applies Velocity when joining/leaving the server").getBoolean();
			serverconfig.save();
		}
	}
	
	@EventHandler
	public void init(FMLInitializationEvent ev) {
		MinecraftForge.EVENT_BUS.register(new FreezeEvents());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent ev) {
	}
	
	
	@EventHandler
	public void serverStart(FMLServerStartingEvent ev) {
		if (FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()) {
			levelname=new Util().getLevelNamefromServer();
		}
		if(!isDupeModLoaded()) {
			ev.registerServerCommand(new DupeCommandc());
		}
		ev.registerServerCommand(new TastoolsCommandc());
		ev.registerServerCommand(new FreezeCommandc());
		ev.registerServerCommand(new SavestateCommandc());
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
	public static boolean isTASModLoaded() {
		return istasmodloaded;
	}
	public static boolean isDupeModLoaded() {
		return isdupemodloaded;
	}
}
