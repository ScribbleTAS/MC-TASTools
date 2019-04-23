package de.scribble.lp.TASTools.savestates.gui;

import de.scribble.lp.TASTools.savestates.SavestateHandlerClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class GuiSavestateLoadingScreen extends GuiScreen{

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		ScaledResolution scaled = new ScaledResolution(Minecraft.getMinecraft());
		int width = scaled.getScaledWidth();
		int height = scaled.getScaledHeight();
		
		fontRenderer.drawStringWithShadow("Making a savestate, please wait!", width / 2 -77, height / 4 + 34 + -16, 0xFFFFFF);
		fontRenderer.drawStringWithShadow("Idea and code from MightyPork and bspkrs' WorldStateCheckpoints!", width / 2-165, height / 4 + 165 -16, 0xFFB238);
		if(SavestateHandlerClient.copying==true) {
			fontRenderer.drawStringWithShadow("Copying files...", width / 2-35, height / 4 + 50 + -16, 0xFFFFFF);
		}else {
			fontRenderer.drawStringWithShadow("Done!", width / 2-13, height / 4 + 50 + -16, 0xFFFFFF);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	@Override
	public void updateScreen() {
		super.updateScreen();
	}
}
