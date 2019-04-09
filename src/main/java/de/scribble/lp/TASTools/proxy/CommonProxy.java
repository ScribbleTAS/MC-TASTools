package de.scribble.lp.TASTools.proxy;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public static Logger logger;
	
	public void preInit(FMLPreInitializationEvent ev) {
		logger=ev.getModLog();
		logger.info("TAStools initialized");
	}
	
	public void init(FMLInitializationEvent ev) {
		
	}
	
	public void postInit(FMLPostInitializationEvent ev) {
		
	}

	

}
