package de.scribble.lp.TASTools;

import java.io.File;

import org.apache.logging.log4j.Logger;

import de.scribble.lp.TASTools.duping.DupePacket;
import de.scribble.lp.TASTools.duping.DupePacketHandler;
import de.scribble.lp.TASTools.enderdragon.DragonEvents;
import de.scribble.lp.TASTools.freeze.FreezeEvents;
import de.scribble.lp.TASTools.freeze.FreezePacket;
import de.scribble.lp.TASTools.freeze.FreezePacketHandler;
import de.scribble.lp.TASTools.keystroke.KeystrokesPacket;
import de.scribble.lp.TASTools.keystroke.KeystrokesPacketHandler;
import de.scribble.lp.TASTools.misc.MiscPacket;
import de.scribble.lp.TASTools.misc.MiscPacketHandler;
import de.scribble.lp.TASTools.savestates.SavestatePacket;
import de.scribble.lp.TASTools.savestates.SavestatePacketHandler;
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
		ModLoader.NETWORK.registerMessage(DupePacketHandler.class, DupePacket.class, 3, Side.SERVER);
		ModLoader.NETWORK.registerMessage(SavestatePacketHandler.class, SavestatePacket.class, 4, Side.SERVER);
		ModLoader.NETWORK.registerMessage(SavestatePacketHandler.class, SavestatePacket.class, 5, Side.CLIENT);
		ModLoader.NETWORK.registerMessage(MiscPacketHandler.class, MiscPacket.class, 6, Side.CLIENT);
		
		if(ev.getSide()==Side.SERVER) {
			//Make a Serverconfig
			serverconfig=new Configuration(new File(ev.getModConfigurationDirectory().getPath()+File.separator+"tastoolsSERVER.cfg"));
			serverconfig.load();
			ModLoader.freezeenabledMP=serverconfig.get("Freeze","Enabled", false, "Freezes the game when starting the Server").getBoolean();
			VelocityEvents.velocityenabledServer=serverconfig.get("Velocity","Enabled",true,"Saves and applies Velocity when joining/leaving the server").getBoolean();
			ModLoader.stopit=serverconfig.get("Savestate","LoadSavestate", false, "This is used for loading a Savestate. When entering /savestate load, this will be set to true, and the server will delete the current world and copy the latest savestate when starting.").getBoolean();
			serverconfig.save();
			//Generate a folder for the savestates
			new File(FMLCommonHandler.instance().getSavesDirectory().getPath()+File.separator+"savestates").mkdir();
		}
		
	}
	
	public void init(FMLInitializationEvent ev) {
		MinecraftForge.EVENT_BUS.register(new FreezeEvents());
		MinecraftForge.EVENT_BUS.register(new DragonEvents());
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
