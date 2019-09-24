package de.scribble.lp.TASTools.duping;

import java.io.File;

import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.savestates.gui.GuiSavestateIngameMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class DupeEvents {
	private Minecraft mc= Minecraft.getMinecraft();
	public static boolean dupingenabled;
	protected static EntityPlayer playa;
	
	@SubscribeEvent
	public void onCloseServer(PlayerEvent.PlayerLoggedOutEvent ev){
		if((mc.currentScreen instanceof GuiIngameMenu||mc.currentScreen instanceof GuiSavestateIngameMenu)&&dupingenabled&&!mc.getIntegratedServer().getPublic()){
			CommonProxy.logger.info("Start saving items/chests...");
			new RecordingDupe().saveFile(ev.player);
		}
	}
	
	@SubscribeEvent
	public void onOpenServer(PlayerEvent.PlayerLoggedInEvent ev){
		if (dupingenabled&&!mc.getIntegratedServer().getPublic()) {
			File file= new File(mc.mcDataDir, "saves" + File.separator +mc.getIntegratedServer().getFolderName()+File.separator+"latest_dupe.txt");
			if (file.exists()){
				CommonProxy.logger.info("Start refilling...");
				new RefillingDupe().refill(file, ev.player);
			}
		}
	}
	
	@SubscribeEvent
	public void pressKeybinding(InputEvent.KeyInputEvent ev){
		if(ClientProxy.DupeKey.isPressed()&&dupingenabled){
			ModLoader.NETWORK.sendToServer(new DupePacket());
		}
	}
	public void startStopping(EntityPlayer player) {
		StopMoving stopit = new StopMoving();
		playa= player;
		Minecraft.getMinecraft().thePlayer.motionX=0;
		Minecraft.getMinecraft().thePlayer.motionY=0;
		Minecraft.getMinecraft().thePlayer.motionZ=0;
		Minecraft.getMinecraft().thePlayer.velocityChanged=true;
		playa.capabilities.disableDamage=true;
		MinecraftForge.EVENT_BUS.register(stopit);
	}

}
class StopMoving extends DupeEvents{
	private Minecraft mc = Minecraft.getMinecraft();
	private int length=0;
	
	@SubscribeEvent
	public void stopMoving(TickEvent.ClientTickEvent ev) {
		if(ev.phase==Phase.START){
			if (length < 5) {
				mc.gameSettings.keyBindForward.pressed = false;
				mc.gameSettings.keyBindBack.pressed = false;
				mc.gameSettings.keyBindRight.pressed = false;
				mc.gameSettings.keyBindLeft.pressed = false;
				mc.gameSettings.keyBindJump.pressed = false;
				mc.gameSettings.keyBindSprint.pressed = false;
				mc.gameSettings.keyBindSneak.pressed = false;
				mc.gameSettings.keyBindDrop.pressed = false;
				mc.gameSettings.keyBindAttack.pressed = false;
				mc.gameSettings.keyBindUseItem.pressed = false;
			}
			if(length==60) {
				if(!playa.capabilities.isCreativeMode&&!playa.isSpectator()) {
					playa.capabilities.disableDamage=false;
				}
			}
			if (length<=60) {
				length++;
			}
		}
	}
}
