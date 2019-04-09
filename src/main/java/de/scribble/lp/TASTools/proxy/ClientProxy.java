package de.scribble.lp.TASTools.proxy;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.duping.DupeEvents;
import de.scribble.lp.TASTools.keystroke.GuiKeystrokes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;	

public class ClientProxy extends CommonProxy{
	public static Configuration config;
	public void preInit(FMLPreInitializationEvent ev) {
		super.preInit(ev);
		ClientRegistry.registerKeyBinding(ModLoader.DupeKey);
		config = new Configuration(ev.getSuggestedConfigurationFile());
		config.load();
		GuiKeystrokes.guienabled=config.get("Keystrokes","Enabled", true, "Activates the keystrokes on startup").getBoolean();
		DupeEvents.dupingenabled=config.get("Duping","Enabled", true, "Activates the duping on startup").getBoolean();
		config.save();
	}
	
	public void init(FMLInitializationEvent ev) {
		MinecraftForge.EVENT_BUS.register(new DupeEvents());
		MinecraftForge.EVENT_BUS.register(new GuiKeystrokes());
		super.init(ev);
	}
	
	public void postInit(FMLPostInitializationEvent ev) {
		super.postInit(ev);
	}
}
