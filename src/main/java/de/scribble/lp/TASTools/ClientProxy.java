package de.scribble.lp.TASTools;

import java.io.File;

import org.lwjgl.input.Keyboard;

import de.scribble.lp.TASTools.duping.DupeEvents;
import de.scribble.lp.TASTools.keystroke.GuiKeystrokes;
import de.scribble.lp.TASTools.misc.GuiOverlayLogo;
import de.scribble.lp.TASTools.savestates.SavestateEvents;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;	

public class ClientProxy extends CommonProxy{
	
	public static KeyBinding DupeKey = new KeyBinding("Load Chests/Items", Keyboard.KEY_I, "DupeMod");
	public static KeyBinding FreezeKey = new KeyBinding("Freeze/Unfreeze Players", Keyboard.KEY_Y, "TASTools");
	public static KeyBinding SavestateSaveKey = new KeyBinding("Create a savestate", Keyboard.KEY_J, "TASTools");
	public static KeyBinding SavestateLoadKey = new KeyBinding("Load latest savestate", Keyboard.KEY_K, "TASTools");
	
	public static Configuration config;
	
	public void preInit(FMLPreInitializationEvent ev) {
		
		ClientRegistry.registerKeyBinding(DupeKey);
		ClientRegistry.registerKeyBinding(FreezeKey);
		ClientRegistry.registerKeyBinding(SavestateSaveKey);
		ClientRegistry.registerKeyBinding(SavestateLoadKey);
		config = new Configuration(ev.getSuggestedConfigurationFile());
		config.load();
		GuiKeystrokes.guienabled=config.get("Keystrokes","Enabled", true, "Activates the keystrokes on startup").getBoolean();
		String position=config.get("Keystrokes","CornerPos", "downLeft", "Sets the Keystroke to that specific corner. Options: downLeft,downRight,upRight,upLeft").getString();
		DupeEvents.dupingenabled=config.get("Duping","Enabled", false, "Activates the duping on startup").getBoolean();
		VelocityEvents.velocityenabledClient=config.get("Velocity", "Enabled", true, "Activates velocity saving on startup").getBoolean();
		ModLoader.freezeenabledSP=config.get("Freeze","Enabled", false, "Freezes the game when joining singleplayer").getBoolean();
		SavestateEvents.savestatepauseenabled=config.get("Savestate", "CustomGui", true, "Enables 'Make a Savestate' Button in the pause menu. Disable this if you use other mods that changes the pause menu").getBoolean();
		GuiOverlayLogo.potionenabled=config.get("GuiPotion","Enabled",true,"Enables the MC-TAS-Logo in the Gui").getBoolean();
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
	}
	
	public void postInit(FMLPostInitializationEvent ev) {
		super.postInit(ev);
	}

}
