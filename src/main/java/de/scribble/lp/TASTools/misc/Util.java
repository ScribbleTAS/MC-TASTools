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
import de.scribble.lp.TASTools.savestates.SavestateHandlerServer;
import de.scribble.lp.TASTools.velocity.VelocityEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ScreenShotHelper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Util {
	public static boolean enableSavestateScreenshotting;
	public static boolean disableAdvancementMessages;
	public static boolean disableRecipeMessages;
	/**
	 * Disable Narrator Messages
	 */
	public static boolean disableSystemMessages;
	public static boolean disableTutorialMessages;
	
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
		SavestateHandlerClient.endtimer=savestatetime;
		
		Util.disableAdvancementMessages=config.get("Messages", "AdvancementMessages Enabled", false, "Disable Advancement messages in the top right").getBoolean();
		Util.disableRecipeMessages=config.get("Messages", "RecipeMessages Enabled", true, "Disable Recipe messages in the top right").getBoolean();
		Util.disableSystemMessages=config.get("Messages", "SystemMessages Enabled", false, "Disable System Messages in the top right (Like enableing the narrator)").getBoolean();
		Util.disableTutorialMessages=config.get("Messages", "TutorialMessages Enabled", false, "Disable Tutorial Messages in the top right").getBoolean();
		
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
