package de.scribble.lp.TASTools.duping;

import java.io.File;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class DupeEvents {
	private Minecraft mc= Minecraft.getMinecraft();
	public static boolean dupingenabled;
	protected static EntityPlayer playa;
	
	
	@SubscribeEvent
	public void onOpenServer(PlayerLoggedInEvent ev){
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
		if(ClientProxy.DupeKey.getIsKeyPressed()&&dupingenabled){
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
		MinecraftForge.EVENT_BUS.register(stopit);
	}

}
class StopMoving extends DupeEvents{
	private Minecraft mc = Minecraft.getMinecraft();
	private int length=0;
	
	@SubscribeEvent
	public void stopMoving(TickEvent.ClientTickEvent ev) {
		if(ev.phase==Phase.START){
			if (length < 30) {
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
			if (length<=30) {
				length++;
			}
		}
	}
}

