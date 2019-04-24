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
		
		fontRenderer.drawStringWithShadow("Loading a savestate, please wait!", width / 2 -81, height / 4 + 34 + -16, 0xFFFFFF);
		fontRenderer.drawStringWithShadow("Idea and code from MightyPork and bspkrs' WorldStateCheckpoints!", width / 2-168, height / 4 + 165 -16, 0xFFB238);
		if(SavestateHandlerClient.copying==true&&SavestateHandlerClient.deleting==false) {
			fontRenderer.drawStringWithShadow("Copying files...", width / 2-35, height / 4 + 50 + -16, 0xFFFFFF);
		}else if(SavestateHandlerClient.copying==false&&SavestateHandlerClient.deleting==false) {
			fontRenderer.drawStringWithShadow("Launching the world", width / 2-47, height / 4 + 50 + -16, 0xFFFFFF);
		}else if(SavestateHandlerClient.copying==false&&SavestateHandlerClient.deleting==true) {
			fontRenderer.drawStringWithShadow("Cleaning up...", width / 2-33, height / 4 + 50 + -16, 0xFFFFFF);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
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
