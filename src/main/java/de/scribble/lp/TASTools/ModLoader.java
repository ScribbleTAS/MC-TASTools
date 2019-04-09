package de.scribble.lp.TASTools;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.logging.log4j.Logger;
import org.jline.utils.Log;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "tastools", name = "TAS-Tools", version = "1.0")
public class ModLoader {
	public Logger logger; 
	@Instance
	public static ModLoader instance = new ModLoader();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent ev) {
		logger=ev.getModLog();
		logger.info("TAStools initialized");
	}
}
