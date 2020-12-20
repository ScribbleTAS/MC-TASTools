package de.scribble.lp.TASTools;

import java.io.File;

import org.lwjgl.input.Keyboard;

import de.scribble.lp.TASTools.duping.DupeEvents;
import de.scribble.lp.TASTools.keystroke.GuiKeystrokes;
import de.scribble.lp.TASTools.misc.GuiOverlayLogo;
import de.scribble.lp.TASTools.misc.InfoGui;
import de.scribble.lp.TASTools.misc.MiscEvents;
import de.scribble.lp.TASTools.misc.Util;
import de.scribble.lp.TASTools.savestates.SavestateEvents;
import de.scribble.lp.TASTools.savestates.SavestateHandlerClient;
import de.scribble.lp.TASTools.shield.ShieldDownloader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;	

public class ClientProxy extends CommonProxy{
	
	public static KeyBinding DupeKey = new KeyBinding("Load Chests/Items (Dupemod)", Keyboard.KEY_I, "TASTools");
	public static KeyBinding FreezeKey = new KeyBinding("Freeze/Unfreeze Players", Keyboard.KEY_Y, "TASTools");
	public static KeyBinding SavestateSaveKey = new KeyBinding("Create Savestate", Keyboard.KEY_J, "TASTools");
	public static KeyBinding SavestateLoadKey = new KeyBinding("Load Latest Savestate", Keyboard.KEY_K, "TASTools");
	public static KeyBinding TestingKey = new KeyBinding("A keybind for quickly testing things", Keyboard.KEY_H, "TASTools");
	
	public static Configuration config;
	
	private static SavestateHandlerClient saveHandler;
	
	public void preInit(FMLPreInitializationEvent ev) {
		
		ClientRegistry.registerKeyBinding(DupeKey);
		ClientRegistry.registerKeyBinding(FreezeKey);
		ClientRegistry.registerKeyBinding(SavestateSaveKey);
		ClientRegistry.registerKeyBinding(SavestateLoadKey);
		//ClientRegistry.registerKeyBinding(TestingKey);
		
		config = new Configuration(ev.getSuggestedConfigurationFile());
		Util.reloadClientconfig(config);
		
		
		
		new File (Minecraft.getMinecraft().mcDataDir,"saves"+File.separator+"savestates").mkdir();
		super.preInit(ev);
	}
	
	public void init(FMLInitializationEvent ev) {
		super.init(ev);
		//disable dupemod in this mod
		if(!CommonProxy.isDupeModLoaded()){
			MinecraftForge.EVENT_BUS.register(new DupeEvents());
		}
		else {
			CommonProxy.logger.warn("Found the DupeMod to be installed! DupeMod is integrated in TAStools, so no need to load that!");
		}
		//disable keystrokes from this mod
		if(!CommonProxy.isTASModLoaded()) {
			MinecraftForge.EVENT_BUS.register(new GuiKeystrokes());
		}
		MinecraftForge.EVENT_BUS.register(new GuiOverlayLogo());
		MinecraftForge.EVENT_BUS.register(new SavestateEvents());
		MinecraftForge.EVENT_BUS.register(new MiscEvents());
		MinecraftForge.EVENT_BUS.register(new ShieldDownloader());
//		MinecraftForge.EVENT_BUS.register(new InfoGui());
		
		saveHandler=new SavestateHandlerClient();
	}
	
	public void postInit(FMLPostInitializationEvent ev) {
		super.postInit(ev);
	}
	public static SavestateHandlerClient getSaveHandlerClient() {
		return saveHandler;
	}
}
