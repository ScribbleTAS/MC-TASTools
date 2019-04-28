package de.scribble.lp.TASTools.savestates.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiSavestatePause extends GuiScreen{

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		new GuiButton(1, 10, 10, "Make Savestate");
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	@Override
	public void updateScreen() {
		super.updateScreen();
	}
}
