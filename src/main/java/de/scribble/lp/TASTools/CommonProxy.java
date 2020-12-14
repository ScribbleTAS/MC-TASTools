package de.scribble.lp.TASTools;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.scribble.lp.TASTools.duping.DupePacket;
import de.scribble.lp.TASTools.duping.DupePacketHandler;
import de.scribble.lp.TASTools.enderdragon.DragonEvents;
import de.scribble.lp.TASTools.fishmanip.FishManipEvents;
import de.scribble.lp.TASTools.flintrig.FlintRig;
import de.scribble.lp.TASTools.flintrig.ZombieDrops;
import de.scribble.lp.TASTools.freeze.FreezeEvents;
import de.scribble.lp.TASTools.freeze.FreezePacket;
import de.scribble.lp.TASTools.freeze.FreezePacketHandler;
import de.scribble.lp.TASTools.keystroke.KeystrokesPacket;
import de.scribble.lp.TASTools.keystroke.KeystrokesPacketHandler;
import de.scribble.lp.TASTools.misc.MiscPacket;
import de.scribble.lp.TASTools.misc.MiscPacketHandler;
import de.scribble.lp.TASTools.misc.Util;
import de.scribble.lp.TASTools.savestates.SavestateHandlerServer;
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

	public static Logger logger=LogManager.getLogger("TASTools");
	public static Configuration serverconfig;
	public static boolean enableServerDuping;
	private static boolean istasmodloaded;
	private static boolean isdupemodloaded;
	
	private static SavestateHandlerServer saveHandler;

	public void preInit(FMLPreInitializationEvent ev) {
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
			Util.reloadServerconfig(serverconfig);
			
			//Generate a folder for the savestates
			new File(FMLCommonHandler.instance().getSavesDirectory().getPath()+File.separator+"savestates").mkdir();
			
			saveHandler=new SavestateHandlerServer();
		}
	}
	
	public void init(FMLInitializationEvent ev) {
		MinecraftForge.EVENT_BUS.register(new FreezeEvents());
		MinecraftForge.EVENT_BUS.register(new DragonEvents());
		MinecraftForge.EVENT_BUS.register(new FishManipEvents());
		MinecraftForge.EVENT_BUS.register(new FlintRig());
		MinecraftForge.EVENT_BUS.register(new ZombieDrops());
	}
	
	public void postInit(FMLPostInitializationEvent ev) {
		
	}
	public static boolean isTASModLoaded() {
		return istasmodloaded;
	}
	public static boolean isDupeModLoaded() {
		return isdupemodloaded;
	}
	public static SavestateHandlerServer getSaveHandler() {
		return saveHandler;
	}
}
