package de.scribble.lp.TASTools.name;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent.NameFormat;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NameForcer {
	SkinManager skinManager=Minecraft.getMinecraft().getSkinManager();
	@SubscribeEvent
	public void onPlayerCreated(NameFormat ev) {
		if(ev.getDisplayname().equalsIgnoreCase("TAS_bot"))ev.setDisplayname(TextFormatting.GOLD+"TASBot");
		else if(ev.getDisplayname().equalsIgnoreCase("ScribbleLP"))ev.setDisplayname(stringToRainbow("Scribble", false));
		else if(ev.getDisplayname().equalsIgnoreCase("Curcuit_Block"))ev.setDisplayname(TextFormatting.BLUE+"Curcuit Block");
		else ev.setDisplayname(TextFormatting.GRAY+ev.getDisplayname());
	}
	
	
	
	public static String stringToRainbow(String parString, boolean parReturnToBlack)
	{
	   int stringLength = parString.length();
	   if (stringLength < 1)
	   {
	      return "";
	   }
	   String outputString = "";
	   TextFormatting[] colorChar = 
	      {
	         TextFormatting.RED,
	         TextFormatting.GOLD,
	         TextFormatting.YELLOW,
	         TextFormatting.GREEN,
	         TextFormatting.AQUA,
	         TextFormatting.BLUE,
	         TextFormatting.LIGHT_PURPLE,
	         TextFormatting.DARK_PURPLE
	      };
	   for (int i = 0; i < stringLength; i++)
	   {
	      outputString = outputString+colorChar[i%8]+parString.substring(i, i+1);
	   }
	   // return color to a common one after (most chat is white, but for other GUI might want black)
	   if (parReturnToBlack)
	   {
	      return outputString+TextFormatting.BLACK;
	   }
	   return outputString+TextFormatting.WHITE;
	}
}
