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
		else if(ev.getDisplayname().equalsIgnoreCase("ScribbleLP"))ev.setDisplayname(TextFormatting.DARK_AQUA+"Scribble");
		else if(ev.getDisplayname().equalsIgnoreCase("Curcuit_Block"))ev.setDisplayname(TextFormatting.BLUE+"Curcuit Block");
		else ev.setDisplayname(TextFormatting.GRAY+ev.getDisplayname());
	}
}
