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

import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.duping.DupeEvents;
import de.scribble.lp.TASTools.keystroke.GuiKeystrokes;
import de.scribble.lp.TASTools.savestates.SavestateEvents;
import de.scribble.lp.TASTools.savestates.SavestateHandlerClient;
import de.scribble.lp.TASTools.savestates.SavestateHandlerServer;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	public static void reloadServerconfig(Configuration serverconfig) {
		CommonProxy.serverconfig.load();
		ModLoader.freezeenabledMP=serverconfig.get("Freeze","Enabled", false, "Freezes the game when starting the Server").getBoolean();
		VelocityEvents.velocityenabledServer=serverconfig.get("Velocity","Enabled",true,"Saves and applies Velocity when joining/leaving the server").getBoolean();
		ModLoader.stopit=serverconfig.get("Savestate","LoadSavestate", false, "This is used for loading a Savestate. When entering /savestate load, this will be set to true, and the server will delete the current world and copy the latest savestate when starting.").getBoolean();
		SavestateHandlerServer.endtimer=serverconfig.get("TimeToSave","TimeInMillis", 5000, "Set's the delay between Minecraft saving all chunks and the mod starting to copy files... Big worlds need a bit longer to save the world, so here you can adjust that").getInt();
		MiscEvents.ignorerespawntimerServer=serverconfig.get("IgnoreRespawnTimer","Ignore", false, "Disables the 60 ticks of invulnerability when joining the server").getBoolean();
		CommonProxy.serverconfig.save();
	}
	@SideOnly(Side.CLIENT)
	public static void reloadClientconfig(Configuration config) {
		config.load();
		
		GuiKeystrokes.guienabled=config.get("Keystrokes","Enabled", true, "Activates the keystrokes on startup").getBoolean();
		
		String position=config.get("Keystrokes","CornerPos", "downLeft", "Sets the Keystroke to that specific corner. Options: downLeft,downRight,upRight,upLeft").getString();
		
		if (position.equals("downLeft")) {
			GuiKeystrokes.changeCorner(0);
		}
		else if (position.equals("downRight")) {
			GuiKeystrokes.changeCorner(1);
		}
		else if (position.equals("upRight")) {
			GuiKeystrokes.changeCorner(2);
		}
		else if (position.equals("upLeft")) {
			GuiKeystrokes.changeCorner(3);
		}
		
		DupeEvents.dupingenabled=config.get("Duping","Enabled", true, "Activates the duping on startup").getBoolean();
		VelocityEvents.velocityenabledClient=config.get("Velocity", "Enabled", true, "Activates velocity saving on startup").getBoolean();
		ModLoader.freezeenabledSP=config.get("Freeze","Enabled", false, "Freezes the game when joining singleplayer").getBoolean();
		
		SavestateEvents.savestatepauseenabled=config.get("Savestate", "CustomGuiPause", true, "Enables 'Make a Savestate' Button in the pause menu. Disable this if you use other mods that change the pause menu").getBoolean();
		SavestateEvents.reloadgameoverenabled=config.get("Savestate", "CustomGuiGameOver", true, "Enables 'Reload a Savestate' Button in the game over screen. Disable this if you use other mods that change the game over screen").getBoolean();
		
		GuiOverlayLogo.potionenabled=config.get("GuiPotion","Enabled",true,"Enables the MC-TAS-Logo in the Gui").getBoolean();
		Util.enableSavestateScreenshotting=config.get("Screenshot", "Enabled", false, "Take a screenshot before the savestate so you know where you left off. Does not work on servers.").getBoolean();
		
		int savestatetime=config.get("Savestatetime","TimeInMillis", 5000, "Set's the delay between Minecraft saving all chunks and the mod starting to copy files... Big worlds need a bit longer to save the world, so here you can adjust that").getInt();
		if (savestatetime>50000) {
			ClientProxy.logger.warn("Savestatetime in config is too high! Correcting it to 50000");
			config.get("Savestatetime","TimeInMillis", 5000, "Set's the delay between Minecraft saving all chunks and the mod starting to copy files... Big worlds need a bit longer to save the world, so here you can adjust that")
			.set(50000);
			savestatetime=50000;
		}else if(savestatetime<0) {
			ClientProxy.logger.warn("Savestatetime in config is negative! Correcting it to the default");
			config.get("Savestatetime","TimeInMillis", 5000, "Set's the delay between Minecraft saving all chunks and the mod starting to copy files... Big worlds need a bit longer to save the world, so here you can adjust that")
			.set(5000);
			savestatetime=5000;
		}
		SavestateHandlerClient.savetimer=savestatetime;
		
//		SavestateHandlerClient.loadtimer=config.get("Loadstatetime", "TimeInMillis", 50, "Set the delay for loading a savestate. One more thing to change when testing compatibility").getInt();
		
		config.save();
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
		Framebuffer buffer = Minecraft.getMinecraft().getFramebuffer();
		int width = Minecraft.getMinecraft().displayWidth;
		int height = Minecraft.getMinecraft().displayHeight;
		try {
			if (OpenGlHelper.isFramebufferEnabled()) {
				width = buffer.framebufferTextureWidth;
				height = buffer.framebufferTextureHeight;
			}

			int i = width * height;

			if (pixelBuffer == null || pixelBuffer.capacity() < i) {
				pixelBuffer = BufferUtils.createIntBuffer(i);
				pixelValues = new int[i];
			}

			GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			pixelBuffer.clear();

			if (OpenGlHelper.isFramebufferEnabled()) {
				GlStateManager.bindTexture(buffer.framebufferTexture);
				GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
						(IntBuffer) pixelBuffer);
			} else {
				GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV,
						(IntBuffer) pixelBuffer);
			}

			pixelBuffer.get(pixelValues);
			TextureUtil.processPixelValues(pixelValues, width, height);
			BufferedImage bufferedimage = null;

			if (OpenGlHelper.isFramebufferEnabled()) {
				bufferedimage = new BufferedImage(buffer.framebufferWidth, buffer.framebufferHeight, 1);
				int j = buffer.framebufferTextureHeight - buffer.framebufferHeight;

				for (int k = j; k < buffer.framebufferTextureHeight; ++k) {
					for (int l = 0; l < buffer.framebufferWidth; ++l) {
						bufferedimage.setRGB(l, k - j, pixelValues[k * buffer.framebufferTextureWidth + l]);
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
