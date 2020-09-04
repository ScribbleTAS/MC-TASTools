package de.scribble.lp.TASTools.shield;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import de.scribble.lp.TASTools.misc.FileStuff;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ShieldDownloader {
	private static final ResourceLocation bottleshield = new ResourceLocation("tastools:textures/shields/bottleshield.png");
	private static final String cacheLocation = "shieldstt/";
	private static String shieldname;
	
	@SubscribeEvent
	  public void onPlayerJoin(EntityJoinWorldEvent event)
	  {
	    if ((event.getWorld().isRemote) && ((event.getEntity() instanceof EntityPlayer)))
	    {
	      EntityPlayer player = (EntityPlayer)event.getEntity();
	      String uuid = player.getGameProfile().getId().toString();
	      shieldname=getShieldName(uuid);
	      download(uuid, cacheLocation);
	    }
	  }
	
	public static ResourceLocation getResourceLocation(EntityLivingBase entitylivingbaseIn){
		String playerUUID = entitylivingbaseIn.getUniqueID().toString();
		if(!shieldname.contentEquals("bottleshield")) {
			return new ResourceLocation(cacheLocation+shieldname);
		}else {
			return bottleshield;
		}
	}
	
	public static void download(String uuid, String location) {
		
		if ((shieldname != null) && (!shieldname.isEmpty())) {
			String url = "https://raw.githubusercontent.com/ScribbleLP/MC-TASTools/1.12.2/shields/"
					+ shieldname;
			TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
			ResourceLocation cachedLocation = new ResourceLocation(location + shieldname);
			if (!rExists(cachedLocation)) {
				IImageBuffer iib = new IImageBuffer() {
					public BufferedImage parseUserSkin(BufferedImage var1) {
						return var1;
					}

					public void skinAvailable() {
					}

				};
				ThreadDownloadImageData downloadedShield = new ThreadDownloadImageData(null, url, null, iib);
				textureManager.loadTexture(cachedLocation, downloadedShield);
			}
		}
	}
	public static String getShieldName(String uuid){
		File feil=new File(Minecraft.getMinecraft().mcDataDir+File.separator+"playerstt.txt");
		URL url;
		Map<String, String> uuids=Maps.<String, String>newHashMap();
		try {
			url=new URL("https://raw.githubusercontent.com/ScribbleLP/MC-TASTools/1.12.2/shields/shieldnames.txt");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return "bottleshield";
		}
		try {
			uuids = mapNames(FileStuff.readThingsFromURL(url, feil));
		} catch (IOException e) {
			e.printStackTrace();
			return "bottleshield";
		}
		if(uuids.containsKey(uuid)) {
			return uuids.get(uuid);
		}else {
			return "bottleshield"; // Everyone else
		}
	}
	
	private static boolean rExists(ResourceLocation resourceLocation) {
		try {
			IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation);
			if (resource != null) {
				return true;
			}
		} catch (IOException localIOException) {
		}
		return false;
	}
	private static Map<String, String> mapNames(List<String> input){
		Map<String, String> output = Maps.<String, String>newHashMap();
		for (int i = 0; i < input.size(); i++) {
			String line=input.get(i);
			if(line.contains(":")) {
				String[] split = line.split(":");
				output.put(split[0], split[1]);
			}else return null;
		}
		return output;
	}
}
