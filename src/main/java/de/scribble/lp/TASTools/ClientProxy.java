package de.scribble.lp.TASTools;

import java.io.File;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import de.scribble.lp.TASTools.duping.DupeEvents;
import de.scribble.lp.TASTools.duping.DupeEvents2;
import de.scribble.lp.TASTools.freeze.FreezeEvents2;
import de.scribble.lp.TASTools.keystroke.GuiKeystrokes;
import de.scribble.lp.TASTools.misc.GuiOverlayLogo;
import de.scribble.lp.TASTools.misc.MiscEvents;
import de.scribble.lp.TASTools.misc.Util;
import de.scribble.lp.TASTools.savestates.SavestateEvents;
import de.scribble.lp.TASTools.savestates.SavestateEvents2;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import de.scribble.lp.TASTools.velocity.VelocityEvents2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;	

public class ClientProxy extends CommonProxy{
	
	public static KeyBinding DupeKey = new KeyBinding("Load Chests/Items (Dupemod)", Keyboard.KEY_I, "TASTools");
	public static KeyBinding FreezeKey = new KeyBinding("Freeze/Unfreeze Players", Keyboard.KEY_Y, "TASTools");
	public static KeyBinding SavestateSaveKey = new KeyBinding("Create Savestate", Keyboard.KEY_J, "TASTools");
	public static KeyBinding SavestateLoadKey = new KeyBinding("Load Latest Savestate", Keyboard.KEY_K, "TASTools");
	public static KeyBinding DragonKey = new KeyBinding("Make The Dragon Attack You", Keyboard.KEY_G, "TASTools");
	
	public static Configuration config;
	
	@Override
	public void preInit(FMLPreInitializationEvent ev) {
		
		ClientRegistry.registerKeyBinding(DupeKey);
		ClientRegistry.registerKeyBinding(FreezeKey);
		ClientRegistry.registerKeyBinding(SavestateSaveKey);
		ClientRegistry.registerKeyBinding(SavestateLoadKey);
		ClientRegistry.registerKeyBinding(DragonKey);
		config = new Configuration(ev.getSuggestedConfigurationFile());
		config.load();
		GuiKeystrokes.guienabled=config.get("Keystrokes","Enabled", true, "Activates the keystrokes on startup").getBoolean();
		String position=config.get("Keystrokes","CornerPos", "downLeft", "Sets the Keystroke to that specific corner. Options: downLeft,downRight,upRight,upLeft").getString();
		DupeEvents.dupingenabled=config.get("Duping","Enabled", true, "Activates the duping on startup").getBoolean();
		VelocityEvents.velocityenabledClient=config.get("Velocity", "Enabled", true, "Activates velocity saving on startup").getBoolean();
		ModLoader.freezeenabledSP=config.get("Freeze","Enabled", false, "Freezes the game when joining singleplayer").getBoolean();
		SavestateEvents.savestatepauseenabled=config.get("Savestate", "CustomGui", true, "Enables 'Make a Savestate' Button in the pause menu. Disable this if you use other mods that changes the pause menu").getBoolean();
		GuiOverlayLogo.potionenabled=config.get("GuiPotion","Enabled",true,"Enables the MC-TAS-Logo in the Gui").getBoolean();
		Util.enableSavestateScreenshotting=config.get("Screenshot", "Enabled", false, "Take a screenshot before the savestate so you know where you left off. Does not work on servers.").getBoolean();
		config.save();
		
		if (position.equals("downLeft")) {
			GuiKeystrokes.changeCorner(0);
		}
		else if (position.equals("downRight")) {
			GuiKeystrokes.changeCorner(1);
		}
		else if (position.equals("upRight")) {
			GuiKeystrokes.changeCorner(2);
		}
		else if (position.equals("upLeft")) {
			GuiKeystrokes.changeCorner(3);
		}
		new File (Minecraft.getMinecraft().mcDataDir,"saves"+File.separator+"savestates").mkdir();
		super.preInit(ev);
	}
	
	@Override
	public void init(FMLInitializationEvent ev) {
		super.init(ev);
		//disable dupemod in this mod
		if(!CommonProxy.isDupeModLoaded()){
			FMLCommonHandler.instance().bus().register(new DupeEvents());
			MinecraftForge.EVENT_BUS.register(new DupeEvents2());
		}
		else {
			CommonProxy.logger.warn("Found the DupeMod to be installed! DupeMod is integrated in TAStools, so no need to load that!");
		}
		//disable keystrokes from this mod
		if(!CommonProxy.isTASModLoaded()) {
			MinecraftForge.EVENT_BUS.register(new GuiKeystrokes());
		}
		FMLCommonHandler.instance().bus().register(new KeyBindings());
		MinecraftForge.EVENT_BUS.register(new FreezeEvents2());
		MinecraftForge.EVENT_BUS.register(new GuiOverlayLogo());
		MinecraftForge.EVENT_BUS.register(new SavestateEvents());
		FMLCommonHandler.instance().bus().register(new SavestateEvents2());
		MinecraftForge.EVENT_BUS.register(new VelocityEvents2());
		MinecraftForge.EVENT_BUS.register(new MiscEvents());

	}
	
	@Override
	public void postInit(FMLPostInitializationEvent ev) {
		super.postInit(ev);
	}

}
