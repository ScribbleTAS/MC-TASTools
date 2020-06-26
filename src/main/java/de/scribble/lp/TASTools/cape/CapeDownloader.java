package de.scribble.lp.TASTools.cape;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

public class CapeDownloader
{
  
	
	public static void download(String uuid, String location) {
		
		String capename=getCapeName(uuid);
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
	public static String getCapeName(String uuid) {
		
		switch (uuid) {
		case "f3112feb-00c1-4de8-9829-53b940342996":
			return "scribblecape.png"; // ScribbleLP
		case "b8abdafc-5002-40df-ab68-63206ea4c7e8":
			return "tascape.png"; // TAS_Bot
		case "79658727-8778-4dc0-9d02-21f20ed913e7":
			return "creepercape.png"; // xdTAG_CLAN
		case "a962e219-c16e-4c3b-b38b-b675d10f92ee":
			return "wellcape.png"; // mcmaxmcmc
		case "e5687c44-65c3-4e76-a233-2550a5597ddc":
			return "vaughcape.png"; // vaugh gaming
		case "839474a8-ba49-468b-9246-ed80c78383aa":
			return "hiscribble.png"; // Curcuit_Block
		case "8d461cc7-a7a2-4cd5-a7af-91a84d6a8466":
			return "caeccape.png"; // Caec
		case "272ef53b-5ac1-4515-b981-11a6549654af":
			return "uneasecape.png";
		default:
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
}

