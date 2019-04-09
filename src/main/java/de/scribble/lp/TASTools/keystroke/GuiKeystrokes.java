package de.scribble.lp.TASTools.keystroke;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiKeystrokes extends Gui {
	
	Minecraft mc=Minecraft.getMinecraft();
	
	public static boolean guienabled=true;
	
	@SubscribeEvent
	public void drawStuff(RenderGameOverlayEvent.Post event){
		if (event.isCancelable() || event.getType() != ElementType.HOTBAR) {
			return;
		}
		ScaledResolution scaled = new ScaledResolution(mc);
		int width = scaled.getScaledWidth();
		int height = scaled.getScaledHeight();
		
		if(guienabled) {
			if(mc.gameSettings.keyBindForward.isKeyDown()){
				new Gui().drawString(mc.fontRenderer, "W", 3, height-13, 0xFFFFFF);
			}
			if(mc.gameSettings.keyBindBack.isKeyDown()){
				new Gui().drawString(mc.fontRenderer, "S", 11, height-13, 0xFFFFFF);
			}
			if(mc.gameSettings.keyBindLeft.isKeyDown()){
				new Gui().drawString(mc.fontRenderer, "A", 19, height-13, 0xFFFFFF);
			}
			if(mc.gameSettings.keyBindRight.isKeyDown()){
				new Gui().drawString(mc.fontRenderer, "D", 27, height-13, 0xFFFFFF);
			}
			if(mc.gameSettings.keyBindJump.isKeyDown()){
				new Gui().drawString(mc.fontRenderer, "Space", 35, height-13, 0xFFFFFF);
			}
			if(mc.gameSettings.keyBindSneak.isKeyDown()){
				new Gui().drawString(mc.fontRenderer, "Shift", 67, height-13, 0xFFFFFF);
			}
			if(mc.gameSettings.keyBindSprint.isKeyDown()){
				new Gui().drawString(mc.fontRenderer, "Ctrl", 92, height-13, 0xFFFFFF);
			}
			if(mc.gameSettings.keyBindAttack.isKeyDown()){
				new Gui().drawString(mc.fontRenderer, "LK", 112, height-13, 0xFFFFFF);
			}
			if(mc.gameSettings.keyBindUseItem.isKeyDown()){
				new Gui().drawString(mc.fontRenderer, "RK", 127, height-13, 0xFFFFFF);
			}
		}
	}
}
