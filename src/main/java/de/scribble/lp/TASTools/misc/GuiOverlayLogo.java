package de.scribble.lp.TASTools.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiOverlayLogo extends Gui{
	ResourceLocation potion = new ResourceLocation("tastools:textures/Potion.png");
	@SubscribeEvent
	public void drawStuff2(RenderGameOverlayEvent.Post event){
		Minecraft mc = Minecraft.getMinecraft();
		if (!mc.player.capabilities.isCreativeMode) {
			if (event.isCancelable() || event.getType() != ElementType.HOTBAR) {
				return;
			}
			int posX = event.getResolution().getScaledWidth() / 2 + 10;
			int posY = event.getResolution().getScaledHeight()-200;

			mc.renderEngine.bindTexture(potion);
			drawTexturedModalRect(posX, posY, 10, 10, 20,20);
		}
	}
}
