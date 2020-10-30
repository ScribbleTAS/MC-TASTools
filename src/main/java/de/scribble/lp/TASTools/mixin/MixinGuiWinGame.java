package de.scribble.lp.TASTools.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.util.Session;

@Mixin(GuiWinGame.class)
public abstract class MixinGuiWinGame extends GuiScreen {
    @Redirect(method = "initGui", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Session;getUsername()Ljava/lang/String;"))
    private String redirectGetUsername(Session session) {
        return "[TAS] " + session.getUsername();
    }
    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiWinGame;drawTexturedModalRect(IIIIII)V", ordinal = 0))
    private void cancelVanilla() {
    	return;
    }
    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiWinGame;drawTexturedModalRect(IIIIII)V", ordinal = 1))
    private void cancelVanilla2() {
    	return;
    }
    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiWinGame;drawTexturedModalRect(IIIIII)V", ordinal = 1))
    private void onDrawMinecraftLogo(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        int j = this.width / 2 - 137;
        int k = this.height + 50;
        this.drawTexturedModalRect(j, k, 0, 0, 99, 44);
        this.drawTexturedModalRect(j + 99, k, 129, 0, 27, 44);
        this.drawTexturedModalRect(j + 99 + 26, k, 126, 0, 3, 44);
        this.drawTexturedModalRect(j + 99 + 26 + 3, k, 99, 0, 26, 44);
        this.drawTexturedModalRect(j + 155, k, 0, 45, 155, 44);
    }
}
