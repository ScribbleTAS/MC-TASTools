package de.scribble.lp.TASTools.proxy;

import java.io.File;

import org.apache.logging.log4j.Logger;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freeze.FreezeEvents;
import de.scribble.lp.TASTools.freeze.FreezePacket;
import de.scribble.lp.TASTools.freeze.FreezePacketHandler;
import de.scribble.lp.TASTools.keystroke.KeystrokesPacket;
import de.scribble.lp.TASTools.keystroke.KeystrokesPacketHandler;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {

	public static Logger logger;
	public static Configuration serverconfig;
	public static boolean enableServerDuping;
	private static boolean istasmodloaded;
	private static boolean isdupemodloaded;


	
	public void preInit(FMLPreInitializationEvent ev) {
		logger=ev.getModLog();
		logger.info("TAStools initialized");
		istasmodloaded=Loader.isModLoaded("tasmod");
		isdupemodloaded=Loader.isModLoaded("dupemod");
		
		MinecraftForge.EVENT_BUS.register(new VelocityEvents());
		
		ModLoader.NETWORK= NetworkRegistry.INSTANCE.newSimpleChannel("tastools");
		ModLoader.NETWORK.registerMessage(FreezePacketHandler.class, FreezePacket.class, 0, Side.SERVER);
		ModLoader.NETWORK.registerMessage(FreezePacketHandler.class, FreezePacket.class, 1, Side.CLIENT);
		ModLoader.NETWORK.registerMessage(KeystrokesPacketHandler.class, KeystrokesPacket.class, 2, Side.CLIENT);
		
		if(ev.getSide()==Side.SERVER) {
			serverconfig=new Configuration(new File(ev.getModConfigurationDirectory()+File.separator+"tastoolsSERVER.cfg"));
			enableServerDuping=serverconfig.get("Duping","Enable",false,"Enables duping on the Server").getBoolean();
			ModLoader.freezeenabledMP=serverconfig.get("Freeze","Enabled", false, "Freezes the game when joining the Server").getBoolean();
			VelocityEvents.velocityenabledServer=serverconfig.get("Velocity","Enabled",true,"Saves and applies Velocity when joining/leaving the server").getBoolean();
			serverconfig.save();
		}
		
	}
	
	public void init(FMLInitializationEvent ev) {
		MinecraftForge.EVENT_BUS.register(new FreezeEvents());

	}
	
	public void postInit(FMLPostInitializationEvent ev) {
		
	}
	public static boolean isTASModLoaded() {
		return istasmodloaded;
	}
	public static boolean isDupeModLoaded() {
		return isdupemodloaded;
	}

}
