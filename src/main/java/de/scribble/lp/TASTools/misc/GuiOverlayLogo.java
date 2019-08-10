package de.scribble.lp.TASTools.misc;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class GuiOverlayLogo extends Gui{
	public static boolean potionenabled;
	@SubscribeEvent
	public void drawStuff2(RenderGameOverlayEvent.Post event){
		Minecraft mc = Minecraft.getMinecraft();
		if (event.isCancelable() || event.type != ElementType.FOOD) {
			return;
		}
		
		int posX = event.resolution.getScaledWidth() / 2;
		int posY = event.resolution.getScaledHeight();
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glDisable(GL11.GL_LIGHTING);
		if(!mc.thePlayer.capabilities.isCreativeMode) {
			mc.renderEngine.bindTexture(new ResourceLocation("tastools","textures/potion_small.png"));
			if (potionenabled) {
				drawTexturedModalRect(posX - 6, posY - 50, 0, 0, 13, 20);
			} else {
				drawTexturedModalRect(posX + 89, posY - 29, 13, 0, 2, 2);
				drawTexturedModalRect(posX + 89, posY - 25, 13, 0, 2, 2);
			}
		}
	}
}
