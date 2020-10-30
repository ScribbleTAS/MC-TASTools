package de.scribble.lp.TASTools.mixin;

import de.scribble.lp.TASTools.CommonProxy;
import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.savestates.SavestateEvents;
import de.scribble.lp.TASTools.savestates.SavestatePacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameMenu.class)
public class MixinGuiIngameMenu extends GuiScreen {
    @Inject(method = "initGui", at = @At("RETURN"), cancellable = true)
    private void onInitGui(CallbackInfo ci) {
        if (SavestateEvents.savestatepauseenabled) {
            // Shift all the buttons to make room
            buttonList.get(0).yPosition += 24;
            buttonList.get(2).yPosition += 24;
            buttonList.get(3).yPosition += 24;
            this.buttonList.add(new GuiButton(13, this.width / 2 - 100, this.height / 4 + 96 + -16, 200, 20, I18n.format("gui.savestate.ingamemenu")));
        }
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    private void onActionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 13) {
            if (mc.player.canUseCommand(2, "savestate")) {
                ModLoader.NETWORK.sendToServer(new SavestatePacket(true));
            } else {
                CommonProxy.logger.info("You don't have the required permissions to use the savestate button!");
            }
            ci.cancel();
        }
    }
}
