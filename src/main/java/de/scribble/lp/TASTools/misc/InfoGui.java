package de.scribble.lp.TASTools.misc;

import java.awt.MouseInfo;

import de.scribble.lp.TASTools.freezeV2.FreezeHandlerClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * A tool for debugging things
 * @author ScribbleLP
 *
 */
public class InfoGui extends Gui{
	Minecraft mc= Minecraft.getMinecraft();
    @SubscribeEvent
    public void drawStuff(RenderGameOverlayEvent.Post event) {
        if (event.isCancelable() || event.getType() != ElementType.HOTBAR) {
            return;
        }
        ScaledResolution scaled = new ScaledResolution(mc);
        int width = scaled.getScaledWidth();
        int height = scaled.getScaledHeight();
        if (!(mc.gameSettings.showDebugInfo)) {
                new Gui().drawCenteredString(mc.fontRenderer, FreezeHandlerClient.getRelX()+ " " + FreezeHandlerClient.getRelY() + " " + FreezeHandlerClient.getRelZ(), width/2, 10, 0xFFFFFF);    //Coordinates
        }
    }
}
