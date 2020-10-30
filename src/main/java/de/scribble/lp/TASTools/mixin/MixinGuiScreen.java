package de.scribble.lp.TASTools.mixin;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

@Mixin(GuiScreen.class)
public class MixinGuiScreen extends Gui {
    @Shadow public Minecraft mc;
    @Shadow public int height;
    @Shadow public int width;

    private final ResourceLocation bottle = new ResourceLocation("tastools:textures/potion.png");

    @Inject(method = "drawBackground", at = @At("RETURN"))
    public void redodrawBackground(int tint, CallbackInfo ci) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        Tessellator tessellator2 = Tessellator.getInstance();
        WorldRenderer bufferbuilder2 = tessellator2.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(bottle);
        int[] rgba = {64, 64, 64, 100};
        bufferbuilder2.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder2.pos(0.0D, this.height, 0.0D).tex(0.0D, (float) this.height / 32.0F + (float) tint).color(rgba[0], rgba[1], rgba[2], rgba[3]).endVertex();
        bufferbuilder2.pos(this.width, this.height, 0.0D).tex((float) this.width / 32.0F, (float) this.height / 32.0F + (float) tint).color(rgba[0], rgba[1], rgba[2], rgba[3]).endVertex();
        bufferbuilder2.pos(this.width, 0.0D, 0.0D).tex((float) this.width / 32.0F, tint).color(rgba[0], rgba[1], rgba[2], rgba[3]).endVertex();
        bufferbuilder2.pos(0.0D, 0.0D, 0.0D).tex(0.0D, tint).color(rgba[0], rgba[1], rgba[2], rgba[3]).endVertex();
        tessellator2.draw();
    }
    @Redirect(method= "drawBackground", at = @At(value="INVOKE", target="Lnet/minecraft/client/renderer/WorldRenderer;color(IIII)Lnet/minecraft/client/renderer/WorldRenderer;",ordinal=3))
    public WorldRenderer addTint(WorldRenderer builder) {
    	builder.color(74, 64, 64, 255);
    	return builder;
    }
}
