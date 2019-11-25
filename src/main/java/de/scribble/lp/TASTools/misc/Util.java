package de.scribble.lp.TASTools.misc;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import de.scribble.lp.TASTools.ClientProxy;
import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.duping.DupeEvents;
import de.scribble.lp.TASTools.keystroke.GuiKeystrokes;
import de.scribble.lp.TASTools.savestates.SavestateEvents;
import de.scribble.lp.TASTools.savestates.SavestateHandlerClient;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ScreenShotHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Util {
	public static boolean enableSavestateScreenshotting;
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
		SavestateHandlerClient.endtimer=ClientProxy.config.get("TimeToSave","TimeInMillis", 1000,"Set's the delay between Minecraft saving all chunks and the mod starting to copy files... Big worlds need a bit longer to save the world, so here you can adjust that").getInt();
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
		Minecraft mc = Minecraft.getMinecraft();
		BufferedImage image=ScreenShotHelper.createScreenshot(mc.displayWidth, mc.displayHeight, mc.getFramebuffer());
		return image;
	}
	@SideOnly(Side.CLIENT)
	public BufferedImage createWorldIcon(@Nullable BufferedImage preImage) {
		Minecraft mc = Minecraft.getMinecraft();
		BufferedImage bufferedimage;
		if(preImage==null) {
			bufferedimage = ScreenShotHelper.createScreenshot(mc.displayWidth, mc.displayHeight, mc.getFramebuffer());
		}else bufferedimage = preImage;

        int i = bufferedimage.getWidth();
        int j = bufferedimage.getHeight();
        int k = 0;
        int l = 0;

        if (i > j)
        {
            k = (i - j) / 2;
            i = j;
        }
        else
        {
            l = (j - i) / 2;
        }

        BufferedImage bufferedimage1 = new BufferedImage(64, 64, 1);
		Graphics graphics = bufferedimage1.createGraphics();
		graphics.drawImage(bufferedimage, 0, 0, 64, 64, k, l, k + i, l + i, (ImageObserver)null);
		graphics.dispose();

		return bufferedimage1;
	}
	@SideOnly(Side.CLIENT)
	public void saveWorldIcon(BufferedImage image, File path) {
		File location = new File(path, "icon.png");
        try {
			ImageIO.write(image, "png", location);
		} catch (IOException e) {
			CommonProxy.logger.error("Something went wrong while writing the worldIcon to a file");
			CommonProxy.logger.catching(e);
		}
	}
}
