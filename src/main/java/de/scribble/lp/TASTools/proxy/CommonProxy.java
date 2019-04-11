package de.scribble.lp.TASTools.proxy;

import org.apache.logging.log4j.Logger;

import de.scribble.lp.TASTools.commands.DupeCommandc;
import de.scribble.lp.TASTools.commands.TastoolsCommandc;
import de.scribble.lp.TASTools.freeze.FreezePacket;
import de.scribble.lp.TASTools.freeze.FreezePacketHandler;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class CommonProxy {

	public static Logger logger;
	private static boolean istasmodloaded;
	private static boolean isdupemodloaded;
	

	
	public void preInit(FMLPreInitializationEvent ev) {
		logger=ev.getModLog();
		logger.info("TAStools initialized");
		istasmodloaded=Loader.isModLoaded("tasmod");
		isdupemodloaded=Loader.isModLoaded("dupemod");
		
		MinecraftForge.EVENT_BUS.register(new VelocityEvents());
	}
	
	public void init(FMLInitializationEvent ev) {
		
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
