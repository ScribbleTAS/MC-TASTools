package de.scribble.lp.TASTools.duping;

import java.io.File;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

public class DupeEvents {
	private Minecraft mc= Minecraft.getMinecraft();
	public static boolean dupingenabled;
	
	@SubscribeEvent
	public void onCloseServer(PlayerEvent.PlayerLoggedOutEvent ev){
		if(dupingenabled&&ev.player.world.isRemote) {
			CommonProxy.logger.info("Start saving...");
			new Recording().saveFile(ev.player);
		}
	}
	
	@SubscribeEvent
	public void onOpenServer(PlayerEvent.PlayerLoggedInEvent ev){
		if (dupingenabled&&ev.player.world.isRemote) {
			File file= new File(mc.mcDataDir, "saves" + File.separator +mc.getIntegratedServer().getFolderName()+File.separator+"latest_dupe.txt");
			if (file.exists()){
				CommonProxy.logger.info("Start refilling...");
				new Refilling().refill(file, ev.player);
			}
		}
	}
	
	@SubscribeEvent
	public void pressKeybinding(InputEvent.KeyInputEvent ev){
		if (dupingenabled&&mc.world.isRemote){
			if(ModLoader.DupeKey.isPressed()){
				File file= new File(mc.mcDataDir, "saves" + File.separator +mc.getIntegratedServer().getFolderName()+File.separator+"latest_dupe.txt");
				mc.player.sendChatMessage("/dupe"); //I'm lazy
			}
		}
	}
}
