package de.scribble.lp.TASTools.keystroke;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.scribble.lp.TASTools.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class GuiKeystrokes extends Gui {
	
	Minecraft mc=Minecraft.getMinecraft();
	
	public static boolean guienabled=true;
	private static int corner=0;
	
	/**
	 * 0=downLeft
	 * 1=downRight
	 * 2=upRight
	 * 3=upLeft
	 * 4=not changed
	 * @param cornernumber
	 */
	public static void changeCorner(int cornernumber) {
		if(cornernumber<=4&&cornernumber>=0) {
			if (cornernumber==4) {
				CommonProxy.logger.info("Didn't change the corner");
				return;
			}
			corner=cornernumber;
		}else {
			CommonProxy.logger.error("Error in changeCorner. The number has to be 0-4. Number entered: "+cornernumber);
		}
	}
	
	@SubscribeEvent
	public void drawStuff(RenderGameOverlayEvent.Post event){
		if (event.isCancelable() || event.type != ElementType.HOTBAR) {
			return;
		}
		ScaledResolution scaled = new ScaledResolution(mc, 0, 0);
		int width = scaled.getScaledWidth();
		int height = scaled.getScaledHeight();
		if(guienabled) {
			if(corner==0) {
				downLeft(width, height);
			}
			else if (corner==1) {
				downRight(width, height);
			}
			else if (corner==2) {
				upRight(width, height);
			}
			else if (corner==3) {
				upLeft(width, height);
			}
		}
	}
	
	private void downLeft(int width, int height) {
		if(mc.gameSettings.keyBindForward.isPressed()){
			new Gui().drawString(mc.fontRenderer, "W", 3, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindBack.isPressed()){
			new Gui().drawString(mc.fontRenderer, "S", 11, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindLeft.isPressed()){
			new Gui().drawString(mc.fontRenderer, "A", 19, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindRight.isPressed()){
			new Gui().drawString(mc.fontRenderer, "D", 27, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindJump.isPressed()){
			new Gui().drawString(mc.fontRenderer, "Space", 35, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindSneak.isPressed()){
			new Gui().drawString(mc.fontRenderer, "Shift", 67, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindSprint.isPressed()){
			new Gui().drawString(mc.fontRenderer, "Ctrl", 92, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindAttack.isPressed()){
			new Gui().drawString(mc.fontRenderer, "LK", 113, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindUseItem.isPressed()){
			new Gui().drawString(mc.fontRenderer, "RK", 127, height-13, 0xFFFFFF);
		}
	}
	
	private void downRight(int width, int height) {
		if(mc.gameSettings.keyBindForward.isPressed()){
			new Gui().drawString(mc.fontRenderer, "W", width-139, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindBack.isPressed()){
			new Gui().drawString(mc.fontRenderer, "S", width-131, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindLeft.isPressed()){
			new Gui().drawString(mc.fontRenderer, "A", width-123, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindRight.isPressed()){
			new Gui().drawString(mc.fontRenderer, "D", width-115, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindJump.isPressed()){
			new Gui().drawString(mc.fontRenderer, "Space", width-107, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindSneak.isPressed()){
			new Gui().drawString(mc.fontRenderer, "Shift", width-75, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindSprint.isPressed()){
			new Gui().drawString(mc.fontRenderer, "Ctrl", width-50, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindAttack.isPressed()){
			new Gui().drawString(mc.fontRenderer, "LK", width-29, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindUseItem.isPressed()){
			new Gui().drawString(mc.fontRenderer, "RK", width-15, height-13, 0xFFFFFF);
		}
	}
	
	private void upRight(int width, int height) {
		if(mc.gameSettings.keyBindForward.isPressed()){
			new Gui().drawString(mc.fontRenderer, "W", width-139, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindBack.isPressed()){
			new Gui().drawString(mc.fontRenderer, "S", width-131, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindLeft.isPressed()){
			new Gui().drawString(mc.fontRenderer, "A", width-123, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindRight.isPressed()){
			new Gui().drawString(mc.fontRenderer, "D", width-115, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindJump.isPressed()){
			new Gui().drawString(mc.fontRenderer, "Space", width-107, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindSneak.isPressed()){
			new Gui().drawString(mc.fontRenderer, "Shift", width-75, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindSprint.isPressed()){
			new Gui().drawString(mc.fontRenderer, "Ctrl", width-50, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindAttack.isPressed()){
			new Gui().drawString(mc.fontRenderer, "LK", width-29, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindUseItem.isPressed()){
			new Gui().drawString(mc.fontRenderer, "RK", width-15, 5, 0xFFFFFF);
		}
	}

	private void upLeft(int width, int height) {
		if(mc.gameSettings.keyBindForward.isPressed()){
			new Gui().drawString(mc.fontRenderer, "W", 3, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindBack.isPressed()){
			new Gui().drawString(mc.fontRenderer, "S", 11, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindLeft.isPressed()){
			new Gui().drawString(mc.fontRenderer, "A", 19, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindRight.isPressed()){
			new Gui().drawString(mc.fontRenderer, "D", 27, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindJump.isPressed()){
			new Gui().drawString(mc.fontRenderer, "Space", 35, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindSneak.isPressed()){
			new Gui().drawString(mc.fontRenderer, "Shift", 67, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindSprint.isPressed()){
			new Gui().drawString(mc.fontRenderer, "Ctrl", 92, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindAttack.isPressed()){
			new Gui().drawString(mc.fontRenderer, "LK", 113, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindUseItem.isPressed()){
			new Gui().drawString(mc.fontRenderer, "RK", 127, 5, 0xFFFFFF);
		}
	}
}
