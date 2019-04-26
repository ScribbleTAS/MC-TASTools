package de.scribble.lp.TASTools.savestates.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class GuiSavestateSavingScreen extends GuiScreen{


	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		
		ScaledResolution scaled = new ScaledResolution(Minecraft.getMinecraft());
		int width = scaled.getScaledWidth();
		int height = scaled.getScaledHeight();
		
		fontRenderer.drawStringWithShadow("Making a savestate, please wait!", width / 2 -75, height / 4 + 34 + -16, 0xFFFFFF);
		fontRenderer.drawStringWithShadow("Idea and code from MightyPork and bspkrs' WorldStateCheckpoints!", width / 2-165, height / 4 + 165 -16, 0xFFB238);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
}
