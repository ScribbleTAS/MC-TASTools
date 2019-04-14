package de.scribble.lp.TASTools.proxy;

import org.apache.logging.log4j.Logger;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freeze.AnotherFreezeEvents;
import de.scribble.lp.TASTools.freeze.FreezePacket;
import de.scribble.lp.TASTools.freeze.FreezePacketHandler;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
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
		
		ModLoader.NETWORK= NetworkRegistry.INSTANCE.newSimpleChannel("tastools");
		ModLoader.NETWORK.registerMessage(FreezePacketHandler.class, FreezePacket.class, 0, Side.SERVER);
		ModLoader.NETWORK.registerMessage(FreezePacketHandler.class, FreezePacket.class, 1, Side.CLIENT);
	}
	
	public void init(FMLInitializationEvent ev) {
		MinecraftForge.EVENT_BUS.register(new AnotherFreezeEvents());
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
