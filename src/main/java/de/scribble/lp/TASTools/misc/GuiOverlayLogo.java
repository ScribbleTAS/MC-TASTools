package de.scribble.lp.TASTools.misc;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiOverlayLogo extends Gui{
	ResourceLocation potion = new ResourceLocation("tastools:textures/potion_small.png");
	public static boolean potionenabled;
	@SubscribeEvent
	public void drawStuff2(RenderGameOverlayEvent.Post event){
		
		Minecraft mc = Minecraft.getMinecraft();
		if(event.getType()==ElementType.AIR) {
			int posX = event.getResolution().getScaledWidth() / 2;
			int posY = event.getResolution().getScaledHeight();
			if(!mc.player.isCreative()&&!mc.player.isSpectator()) {
			mc.renderEngine.bindTexture(potion);
				if (potionenabled) {
					GL11.glEnable(GL11.GL_BLEND);
					drawTexturedModalRect(posX - 6, posY - 50, 0, 0, 13, 20);
					GL11.glDisable(GL11.GL_BLEND);
				} else {
					drawTexturedModalRect(posX + 89, posY - 29, 13, 0, 2, 2);
					drawTexturedModalRect(posX + 89, posY - 25, 13, 0, 2, 2);
				}
			}
		}
	}
}
