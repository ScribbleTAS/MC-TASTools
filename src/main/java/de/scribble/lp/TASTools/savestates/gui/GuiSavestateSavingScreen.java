package de.scribble.lp.TASTools.savestates.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;

public class GuiSavestateSavingScreen extends GuiScreen{


	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		
		ScaledResolution scaled = new ScaledResolution(Minecraft.getMinecraft(), mc.displayWidth, mc.displayHeight);
		int width = scaled.getScaledWidth();
		int height = scaled.getScaledHeight();
		
		drawCenteredString(fontRendererObj,I18n.format("gui.savestate.savingscreen.msg"),width / 2,height / 4 + 34 + -16, 0xFFFFFF); //Making a savestate, please wait!
		drawCenteredString(fontRendererObj, I18n.format("gui.savestate.savingscreen.credit"), width / 2, height / 4 + 165 -16, 0xFFB238);	//Idea and code from MightyPork and bspkrs' WorldStateCheckpoints!
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
}
