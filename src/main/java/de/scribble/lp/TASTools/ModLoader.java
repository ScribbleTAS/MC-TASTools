package de.scribble.lp.TASTools;

import org.lwjgl.input.Keyboard;

import de.scribble.lp.TASTools.commands.DupeCommandc;
import de.scribble.lp.TASTools.commands.TastoolsCommandc;
import de.scribble.lp.TASTools.keystroke.GuiKeystrokes;
import de.scribble.lp.TASTools.proxy.CommonProxy;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "tastools", name = "TAS-Tools", version = "1.0")
public class ModLoader {
	 
	
	@Instance
	public static ModLoader instance = new ModLoader();
	
	@SidedProxy(serverSide = "de.scribble.lp.TASTools.proxy.CommonProxy", clientSide = "de.scribble.lp.TASTools.proxy.ClientProxy")
	public static CommonProxy proxy;
	
	//Keybinds
	public static KeyBinding DupeKey = new KeyBinding("Load Chests/Items", Keyboard.KEY_I, "DupeMod");
	
	
	
	
	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent ev) {
		proxy.preInit(ev);
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent ev) {
		proxy.init(ev);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent ev) {
		proxy.postInit(ev);
	}
	
	@EventHandler
	public void ServerStart(FMLServerStartingEvent ev) {
		ev.registerServerCommand(new DupeCommandc());
		ev.registerServerCommand(new TastoolsCommandc());
	}
}
