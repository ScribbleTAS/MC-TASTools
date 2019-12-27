package de.scribble.lp.TASTools.misc;

import java.io.File;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderLivingEvent;

public class MiscEvents {
	@SubscribeEvent
	public void onMainMenu(GuiOpenEvent ev) {
		if(ev.gui instanceof GuiMainMenu) {
			((GuiMainMenu) ev.gui).updateCounter=0;
			((GuiMainMenu) ev.gui).splashText="Well, someone is using TASTools!";
		}
	}
	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerLoggedOutEvent ev) {
		if (!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()&&!Minecraft.getMinecraft().getIntegratedServer().getPublic()) {
			File file = new File(Minecraft.getMinecraft().mcDataDir,
					"saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName()
							+ File.separator + "miscthings.txt");
			new MiscSaving().saveThings(ev.player, file);
		}
	}
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent ev) {
		if (!FMLCommonHandler.instance().getMinecraftServerInstance().isDedicatedServer()&&!Minecraft.getMinecraft().getIntegratedServer().getPublic()) {
			File file = new File(Minecraft.getMinecraft().mcDataDir,
					"saves" + File.separator + Minecraft.getMinecraft().getIntegratedServer().getFolderName()
							+ File.separator + "miscthings.txt");
			if (file.exists()) {
				ev.player.portalCounter=new MiscReapplying().getPortalTime(file);
			}
		}
	}
	@SubscribeEvent
	public void onRenderPlayer(RenderLivingEvent.Post ev) {
		if(ev.entity instanceof EntityPlayer) {
			((AbstractClientPlayer)ev.entity).locationSkin=new ResourceLocation("tastools:textures/rob.png");
			if(((AbstractClientPlayer) ev.entity).getDisplayName().equalsIgnoreCase("TAS_bot")){
				((AbstractClientPlayer) ev.entity).locationCape= new ResourceLocation("tastools:textures/capes/tascape.png");
			}else {
				((AbstractClientPlayer) ev.entity).locationCape= new ResourceLocation("tastools:textures/capes/bottlecape.png");
			}
		}
	}
}
