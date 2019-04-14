package de.scribble.lp.TASTools;

import org.lwjgl.input.Keyboard;

import de.scribble.lp.TASTools.commands.DupeCommandc;
import de.scribble.lp.TASTools.commands.FreezeCommandc;
import de.scribble.lp.TASTools.commands.TastoolsCommandc;
import de.scribble.lp.TASTools.freeze.FreezePacket;
import de.scribble.lp.TASTools.freeze.FreezePacketHandler;
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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "tastools", name = "TAS-Tools", version = "1.0")
public class ModLoader {
	 
	
	@Instance
	public static ModLoader instance = new ModLoader();
	
	@SidedProxy(serverSide = "de.scribble.lp.TASTools.proxy.CommonProxy", clientSide = "de.scribble.lp.TASTools.proxy.ClientProxy")
	public static CommonProxy proxy;
	
	public static SimpleNetworkWrapper NETWORK;
	

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
	public void serverStart(FMLServerStartingEvent ev) {
		if(!CommonProxy.isDupeModLoaded()) {
			ev.registerServerCommand(new DupeCommandc());
		}
		ev.registerServerCommand(new TastoolsCommandc());
		ev.registerServerCommand(new FreezeCommandc());
	}
}
