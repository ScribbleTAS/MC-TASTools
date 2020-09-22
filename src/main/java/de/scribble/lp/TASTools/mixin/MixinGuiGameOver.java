package de.scribble.lp.TASTools.mixin;

import de.scribble.lp.TASTools.ModLoader;
import de.scribble.lp.TASTools.savestates.SavestateEvents;
import de.scribble.lp.TASTools.savestates.SavestatePacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGameOver.class)
public class MixinGuiGameOver extends GuiScreen {
    @Unique
    private GuiButton reloadButton;

    @Inject(method = "initGui", at = @At(value = "INVOKE", target = "Ljava/util/List;clear()V", shift = At.Shift.AFTER, remap = false))
    private void onInitGui(CallbackInfo ci) {
        if (SavestateEvents.reloadgameoverenabled) {
            reloadButton = new GuiButton(2, this.width / 2 - 100, this.height / 4 + 120, I18n.format("Reload the savestate"));
            this.buttonList.add(reloadButton);
        }
    }

    @Inject(method = "initGui", at = @At("RETURN"))
    private void onPostInitGui(CallbackInfo ci) {
        if (SavestateEvents.reloadgameoverenabled) {
            reloadButton.enabled = true;
        }
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    private void onActionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 2) {
            ModLoader.NETWORK.sendToServer(new SavestatePacket(false));
            ci.cancel();
        }
    }
}
