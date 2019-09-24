package de.scribble.lp.TASTools.misc;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.duping.DupeEvents;
import de.scribble.lp.TASTools.keystroke.GuiKeystrokes;
import de.scribble.lp.TASTools.savestates.SavestateEvents;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;

public class Util {
	public static boolean enableSavestateScreenshotting;
	private static IntBuffer pixelBuffer;
	private static int[] pixelValues;
	public String getLevelNamefromServer() {
		String out=null;
		File file = new File(FMLCommonHandler.instance().getSavesDirectory().getAbsolutePath()+File.separator+"server.properties");
		if (file.exists()) {
			try {
				BufferedReader buf = new BufferedReader(new FileReader(file));
				while (true) {
					String s=buf.readLine();
					if (s.startsWith("level-name")) {
						String [] temp=s.split("=");
						out=temp[1];
						break;
					}
				}
				buf.close();
	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return out;
	}
	@SideOnly(Side.SERVER)
	public void reloadServerconfig() {
		CommonProxy.serverconfig.load();
		CommonProxy.enableServerDuping=CommonProxy.serverconfig.get("Duping","Enable",false,"Enables duping on the Server").getBoolean();
		ModLoader.freezeenabledMP=CommonProxy.serverconfig.get("Freeze","Enabled", false, "Freezes the game when starting the Server").getBoolean();
		VelocityEvents.velocityenabledServer=CommonProxy.serverconfig.get("Velocity","Enabled",true,"Saves and applies Velocity when joining/leaving the server").getBoolean();
		CommonProxy.serverconfig.save();
	}
	@SideOnly(Side.CLIENT)
	public void reloadClientconfig() {
		GuiKeystrokes.guienabled=ClientProxy.config.get("Keystrokes","Enabled", true, "Activates the keystrokes on startup").getBoolean();
		String position=ClientProxy.config.get("Keystrokes","CornerPos", "downLeft", "Sets the Keystroke to that specific corner. Options: downLeft,downRight,upRight,upLeft").getString();
		DupeEvents.dupingenabled=ClientProxy.config.get("Duping","Enabled", false, "Activates the duping on startup").getBoolean();
		VelocityEvents.velocityenabledClient=ClientProxy.config.get("Velocity", "Enabled", true, "Activates velocity saving on startup").getBoolean();
		ModLoader.freezeenabledSP=ClientProxy.config.get("Freeze","Enabled", false, "Freezes the game when joining singleplayer").getBoolean();
		SavestateEvents.savestatepauseenabled=ClientProxy.config.get("Savestate", "CustomGui", true, "Enables 'Make a Savestate' Button in the pause menu. Disable this if you use other mods that changes the pause menu").getBoolean();
		GuiOverlayLogo.potionenabled=ClientProxy.config.get("GuiPotion","Enabled",true,"Enables the MC-TAS-Logo in the Gui").getBoolean();
		Util.enableSavestateScreenshotting=ClientProxy.config.get("Screenshot", "Enabled", false, "Take a screenshot before the savestate so you know where you left off. Does not work on servers.").getBoolean();
	}

	@SideOnly(Side.CLIENT)
	public void saveScreenshotAt(File path, String name, BufferedImage image) {
		File file = new File(path, name);
		try {
			file = file.getCanonicalFile();
		} catch (IOException e) {
			CommonProxy.logger.error("Screenshot name has an invalid name");
			CommonProxy.logger.catching(e);
		}
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException e) {
			CommonProxy.logger.error("Something went wrong while writing the screenshot to a file");
			CommonProxy.logger.catching(e);
		}
	}

	@SideOnly(Side.CLIENT)
	public BufferedImage makeAscreenShot() {
		try {
			Framebuffer buffer = Minecraft.getMinecraft().getFramebuffer();
			int width = Minecraft.getMinecraft().displayWidth;
			int height = Minecraft.getMinecraft().displayHeight;
			if (OpenGlHelper.isFramebufferEnabled()) {
				width = buffer.framebufferTextureWidth;
				height = buffer.framebufferTextureHeight;
			}

			int k = width * height;

			if (pixelBuffer == null || pixelBuffer.capacity() < k) {
				pixelBuffer = BufferUtils.createIntBuffer(k);
				pixelValues = new int[k];
			}

			GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			pixelBuffer.clear();

			if (OpenGlHelper.isFramebufferEnabled()) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, buffer.framebufferTexture);
				GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
			} else {
				GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
			}

			pixelBuffer.get(pixelValues);
			TextureUtil.func_147953_a(pixelValues, width, height);
			BufferedImage bufferedimage = null;

			if (OpenGlHelper.isFramebufferEnabled()) {
				bufferedimage = new BufferedImage(buffer.framebufferWidth, buffer.framebufferHeight, 1);
				int l = buffer.framebufferTextureHeight - buffer.framebufferHeight;

				for (int i1 = l; i1 < buffer.framebufferTextureHeight; ++i1) {
					for (int j1 = 0; j1 < buffer.framebufferWidth; ++j1) {
						bufferedimage.setRGB(j1, i1 - l, pixelValues[i1 * buffer.framebufferTextureWidth + j1]);
					}
				}
			} else {
				bufferedimage = new BufferedImage(width, height, 1);
				bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);
			}
			return bufferedimage;
		} catch (Exception e) {
			CommonProxy.logger.error("Something went wrong while creating the screenshot");
			CommonProxy.logger.catching(e);
			return null;
		}
	}
}
