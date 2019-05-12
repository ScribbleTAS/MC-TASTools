package de.scribble.lp.TASTools.keystroke;

import de.scribble.lp.TASTools.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
		ScaledResolution scaled = new ScaledResolution(mc);
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
		if(mc.gameSettings.keyBindForward.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "W", 3, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindBack.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "S", 11, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindLeft.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "A", 19, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindRight.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "D", 27, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindJump.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "Space", 35, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindSneak.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "Shift", 67, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindSprint.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "Ctrl", 92, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindAttack.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "LK", 113, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindUseItem.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "RK", 127, height-13, 0xFFFFFF);
		}
	}
	
	private void downRight(int width, int height) {
		if(mc.gameSettings.keyBindForward.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "W", width-139, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindBack.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "S", width-131, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindLeft.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "A", width-123, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindRight.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "D", width-115, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindJump.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "Space", width-107, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindSneak.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "Shift", width-75, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindSprint.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "Ctrl", width-50, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindAttack.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "LK", width-29, height-13, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindUseItem.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "RK", width-15, height-13, 0xFFFFFF);
		}
	}
	
	private void upRight(int width, int height) {
		if(mc.gameSettings.keyBindForward.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "W", width-139, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindBack.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "S", width-131, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindLeft.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "A", width-123, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindRight.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "D", width-115, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindJump.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "Space", width-107, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindSneak.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "Shift", width-75, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindSprint.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "Ctrl", width-50, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindAttack.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "LK", width-29, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindUseItem.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "RK", width-15, 5, 0xFFFFFF);
		}
	}

	private void upLeft(int width, int height) {
		if(mc.gameSettings.keyBindForward.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "W", 3, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindBack.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "S", 11, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindLeft.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "A", 19, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindRight.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "D", 27, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindJump.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "Space", 35, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindSneak.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "Shift", 67, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindSprint.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "Ctrl", 92, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindAttack.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "LK", 113, 5, 0xFFFFFF);
		}
		if(mc.gameSettings.keyBindUseItem.isKeyDown()){
			new Gui().drawString(mc.fontRendererObj, "RK", 127, 5, 0xFFFFFF);
		}
	}
}
