package de.scribble.lp.TASTools.savestates.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiSavestatePause extends GuiScreen{

	@Override
    public void initGui()
    {
		buttonList.clear();
        buttonList.add(new GuiButton(4, 10, 200, "Hallo"));
    }
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
