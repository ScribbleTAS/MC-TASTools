package de.scribble.lp.TASTools.duping;

import java.io.File;

import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class DupeEvents {
	private Minecraft mc= Minecraft.getMinecraft();
	protected static EntityPlayer playa;
	public static boolean dupingenabled;
	
	@SubscribeEvent
	public void onCloseServer(PlayerEvent.PlayerLoggedOutEvent ev){
		if(dupingenabled&&!ev.player.world.isRemote) {
			CommonProxy.logger.info("Start saving...");
			new RecordingDupe().saveFile(ev.player);
		}
	}
	
	@SubscribeEvent
	public void onOpenServer(PlayerEvent.PlayerLoggedInEvent ev){
		if (dupingenabled&&!ev.player.world.isRemote) {
			File file= new File(mc.mcDataDir, "saves" + File.separator +mc.getIntegratedServer().getFolderName()+File.separator+"latest_dupe.txt");
			if (file.exists()){
				CommonProxy.logger.info("Start refilling...");
				new RefillingDupe().refill(file, ev.player);
			}
		}
	}
	
	@SubscribeEvent
	public void pressKeybinding(InputEvent.KeyInputEvent ev){
		if (dupingenabled&&!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()){
			if(ClientProxy.DupeKey.isPressed()){
				ModLoader.NETWORK.sendToServer(new DupePacket());
			}
		}
	}
	public void startStopping(EntityPlayer player) {
		StopMoving stopit = new StopMoving();
		playa= player;
		Minecraft.getMinecraft().player.motionX=0;
		Minecraft.getMinecraft().player.motionY=0;
		Minecraft.getMinecraft().player.motionZ=0;
		Minecraft.getMinecraft().player.velocityChanged=true;
		playa.setEntityInvulnerable(true);
		MinecraftForge.EVENT_BUS.register(stopit);
	}
}
class StopMoving extends DupeEvents{
	Minecraft mc = Minecraft.getMinecraft();
	int length=0;
	@SubscribeEvent
	public void stopMoving(TickEvent.ClientTickEvent ev) {
		if(ev.phase==Phase.START) {
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
				if(!playa.isCreative()&&!playa.isSpectator()) {
					playa.setEntityInvulnerable(false);
				}
				MinecraftForge.EVENT_BUS.unregister(this);
			}
			length++;
		}
	}
}
