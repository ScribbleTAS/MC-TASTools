package de.scribble.lp.TASTools.freezeV2;


import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Maps;

import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.freezeV2.networking.MovementPacket;
import de.scribble.lp.TASTools.freezeV2.networking.PermissionPacket;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.NameFormat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FreezeHandlerClient {
	/**
	 * Indicates Server side freezing. Mouse Input freezing is indicated by "clientfrozen"
	 */
	private static boolean serverfrozen;
	/**
	 * Client isn't actually frozen, the server handles freezing... this is just to show that the mouse is diabled
	 */
	private static boolean clientfrozen;
	
	private static double moX=0;
	private static double moY=0;
	private static double moZ=0;
	
	private static float relX=0;
	private static float relY=0;
	private static float relZ=0;
	
	private static boolean enabled=false;
	
	private static MotionSaver saverClient;
	private static RelMotionSaver relsaverClient;
	
	public static void redirectRelativeMotion(EntityLivingBase entity, float xrel, float yrel, float zrel) {
		EntityPlayer player=(EntityPlayer)entity;
		
		if(relsaverClient==null) {
			relsaverClient=new RelMotionSaver(player.getName(), enabled, xrel, yrel, zrel);
		}
		
		if(enabled) {
			if(relsaverClient.isApplied()) {
			}else {
				relsaverClient.setApplied(true);
			}
			relX = 0F;
			relY = 0F;
			relZ = 0F;
		}else {
			if(!relsaverClient.isApplied()) {
				float[] in= {xrel, yrel, zrel};
				relsaverClient.setRelativeMotionSaved(in);
			}else {
				relsaverClient.setApplied(false);
			}
			relX=relsaverClient.getRelSavedX();
			relY=relsaverClient.getRelSavedY();
			relZ=relsaverClient.getRelSavedZ();
		}
	}
	public static void redirectMotion(EntityLivingBase entity, double x, double y, double z) {
		EntityPlayer player=(EntityPlayer)entity;
		
		if(saverClient==null) {
			saverClient=new MotionSaver(player.getName(), enabled, x, y, z);
		}
		
		if(enabled) {
			if(saverClient.isApplied()) {	//If freeze is enabled
			}else {					//The first tick freeze is enabled
				saverClient.setApplied(true);
			}
			moX = 0D;
			moY = 0D;
			moZ = 0D;
		}else {
			if(!saverClient.isApplied()) {	//If this freeze is disabled
				double[] in= {x, y, z};
				saverClient.setMotionSaved(in);
			}else {						//The first tick freeze is disabled
				saverClient.setMotionSavedY(saverClient.getMotionSavedY()+y);
				saverClient.setApplied(false);
			}
			moX=saverClient.getMotionSavedX();
			moY=saverClient.getMotionSavedY();
			moZ=saverClient.getMotionSavedZ();
		}
	}
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void pressKeybinding(InputEvent.KeyInputEvent ev) {
		if (ClientProxy.FreezeKey.isPressed() && Minecraft.getMinecraft().player.canUseCommand(2, "freeze")) {
			ModLoader.NETWORK.sendToServer(new PermissionPacket());
		}
	}
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onMenus(GuiOpenEvent ev) {
		if(ev.getGui() instanceof GuiMainMenu||ev.getGui() instanceof GuiMultiplayer) {
			if(enabled) {
				CommonProxy.logger.info("Unfreezing the mouse");
				enabled=false;
			}
		}else if(ev.getGui() instanceof GuiIngameMenu) {
			if(!enabled) {
				ModLoader.NETWORK.sendToServer(new MovementPacket(moX, moY, moZ, relX, relY, relZ));
			}
		}
	}
	public static double getMoX() {
		return moX;
	}
	public static double getMoY() {
		return moY;
	}
	public static double getMoZ() {
		return moZ;
	}
	public static float getRelX() {
		return relX;
	}
	public static float getRelY() {
		return relY;
	}
	public static float getRelZ() {
		return relZ;
	}
	public static void enable(boolean enable) {
		enabled=enable;
	}
	public static RelMotionSaver getRelsaverClient() {
		return relsaverClient;
	}
	public static MotionSaver getSaverClient() {
		return saverClient;
	}
	@SubscribeEvent
	public void onPlayerCreated(NameFormat ev) {
		if(!ev.getUsername().equals("TASBot")) {
			ev.setDisplayname("[TAS]"+" "+ev.getDisplayname());
		}
	}
}
