package de.scribble.lp.TASTools.mixin;

import de.scribble.lp.TASTools.misc.Util;
import net.minecraft.client.gui.toasts.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiToast.class)
public class MixinGuiToast {
    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    public void redoAddToast(IToast toastIn, CallbackInfo ci) {
        if (toastIn instanceof RecipeToast && Util.disableRecipeMessages) {
            ci.cancel();
        } else if (toastIn instanceof AdvancementToast && Util.disableAdvancementMessages) {
            ci.cancel();
        } else if (toastIn instanceof SystemToast && Util.disableSystemMessages) {
            ci.cancel();
        } else if (toastIn instanceof TutorialToast && Util.disableTutorialMessages) {
            ci.cancel();
        }
    }
}
