package de.scribble.lp.TASTools.freeze;


import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Maps;

import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.CommonProxy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FreezeHandlerVer2 {
	/**
	 * Indicates Server side freezing. Mouse Input freezing is indicated by "clientfrozen"
	 */
	private static boolean serverfrozen;
	/**
	 * Client isn't actually frozen, the server handles freezing... this is just to show that the mouse is diabled
	 */
	private static boolean clientfrozen;
	
	static double moX;
	static double moY;
	static double moZ;
	
	static float relX;
	static float relY;
	static float relZ;
	
	static int cooldown=20;
	static boolean enabled=false;
	static boolean once=false;
	
//	public static double motionSavedX=0;
//	public static double motionSavedY=0;
//	public static double motionSavedZ=0;
//	
//	public static float relSavedX=0;
//	public static float relSavedY=0;
//	public static float relSavedZ=0;
	
	public static MotionSaver saver;
	public static RelMotionSaver relsaver;
	
	public static void redirectRelativeMotion(EntityLivingBase entity, float xrel, float yrel, float zrel) {
		EntityPlayer player=(EntityPlayer)entity;
		if (ClientProxy.FreezeKey.isPressed() && Minecraft.getMinecraft().player.canUseCommand(2, "freeze")) {
			enabled=!enabled;
		}
		if(relsaver==null) {
			relsaver=new RelMotionSaver(player.getName(), enabled, xrel, yrel, zrel);
		}
		
		if(enabled) {
			if(relsaver.isApplied()) {
			}else {
				relsaver.setApplied(true);
			}
			relX = 0F;
			relY = 0F;
			relZ = 0F;
		}else {
			if(!relsaver.isApplied()) {
				float[] in= {xrel, yrel, zrel};
				relsaver.setRelativeMotionSaved(in);
			}else {
				relsaver.setApplied(false);
			}
			relX=relsaver.getRelSavedX();
			relY=relsaver.getRelSavedY();
			relZ=relsaver.getRelSavedZ();
		}
	}
	public static void redirectMotion(EntityLivingBase entity, double x, double y, double z) {
		EntityPlayer player=(EntityPlayer)entity;
		if (ClientProxy.FreezeKey.isPressed() && Minecraft.getMinecraft().player.canUseCommand(2, "freeze")) {
			enabled=!enabled;
		}
		if(saver==null) {
			saver=new MotionSaver(player.getName(), enabled, x, y, z);
		}
		
		if(enabled) {
			if(saver.isApplied()) {	//If freeze is enabled
			}else {					//The first tick freeze is enabled
				saver.setApplied(true);
			}
			moX = 0D;
			moY = 0D;
			moZ = 0D;
		}else {
			if(!saver.isApplied()) {	//If this freeze is disabled
				double[] in= {x, y, z};
				saver.setMotionSaved(in);
			}else {						//The first tick freeze is disabled
				saver.setMotionSavedY(saver.getMotionSavedY()+y);
				saver.setApplied(false);
			}
			moX=saver.getMotionSavedX();
			moY=saver.getMotionSavedY();
			moZ=saver.getMotionSavedZ();
		}
	}
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void pressKeybinding(InputEvent.KeyInputEvent ev) {
//		if (ClientProxy.FreezeKey.isPressed() && Minecraft.getMinecraft().player.canUseCommand(2, "freeze")) {
//			enabled=!enabled;
//		}
	}
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onMenus(GuiOpenEvent ev) {
		if(ev.getGui() instanceof GuiMainMenu||ev.getGui() instanceof GuiMultiplayer) {
			if(FreezeHandler.isClientFrozen()) {
				CommonProxy.logger.info("Unfreezing the mouse");
				FreezeHandler.stopFreezeClient();
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
	public static boolean isClientfrozen() {
		return clientfrozen;
	}
	public static boolean isServerfrozen() {
		return serverfrozen;
	}
}
