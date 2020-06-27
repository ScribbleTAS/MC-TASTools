package de.scribble.lp.TASTools.cape;

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

public class CapeDownloader {
	public static HashMap<String, Boolean> playersCape = new HashMap();
	private static final ResourceLocation bottlecape = new ResourceLocation("tastools:textures/capes/bottlecape.png");
	private static final String cacheLocation = "capestt/";
	private static String capename;
	
	@SubscribeEvent
	  public void onPlayerJoin(EntityJoinWorldEvent event)
	  {
	    if ((event.getWorld().isRemote) && ((event.getEntity() instanceof EntityPlayer)))
	    {
	      EntityPlayer player = (EntityPlayer)event.getEntity();
	      String uuid = player.getGameProfile().getId().toString();
	      capename=getCapeName(uuid);
	      download(uuid, cacheLocation);
	    }
	  }
	
	public static ResourceLocation getResourceLocation(EntityLivingBase entitylivingbaseIn){
		String playerUUID = entitylivingbaseIn.getUniqueID().toString();
		if(!capename.contentEquals("bottlecape")) {
			return new ResourceLocation(cacheLocation+capename);
		}else {
			return bottlecape;
		}
	}
	
	public static void download(String uuid, String location) {
		
		if ((capename != null) && (!capename.isEmpty())) {
			String url = "https://raw.githubusercontent.com/ScribbleLP/MC-TASTools/1.12.2/capes/"
					+ capename;
			TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
			ResourceLocation cachedLocation = new ResourceLocation(location + capename);
			if (!rExists(cachedLocation)) {
				IImageBuffer iib = new IImageBuffer() {
					public BufferedImage parseUserSkin(BufferedImage var1) {
						return var1;
					}

					public void skinAvailable() {
					}

				};
				ThreadDownloadImageData downloadedCape = new ThreadDownloadImageData(null, url, null, iib);
				textureManager.loadTexture(cachedLocation, downloadedCape);
			}
		}
	}
	public static String getCapeName(String uuid){
		File feil=new File(Minecraft.getMinecraft().mcDataDir+File.separator+"playerstt.txt");
		URL url;
		Map<String, String> uuids=Maps.<String, String>newHashMap();
		try {
			url=new URL("https://raw.githubusercontent.com/ScribbleLP/MC-TASTools/1.12.2/capes/capenames.txt");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return "bottlecape";
		}
		try {
			uuids = mapNames(FileStuff.readThingsFromURL(url, feil));
		} catch (IOException e) {
			e.printStackTrace();
			return "bottlecape";
		}
		if(uuids.containsKey(uuid)) {
			return uuids.get(uuid);
		}else {
			return "bottlecape"; // Everyone else
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
