package de.scribble.lp.TASTools.mixin;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

@Mixin(GuiScreen.class)
public class MixinGuiScreen extends Gui{
	@Shadow
	private Minecraft mc;
	@Shadow
	private int height;
	@Shadow
	private int width;
	
	private ResourceLocation bottle = new ResourceLocation("tastools:textures/potion.png");

	@Inject(method="drawBackground", at=@At("HEAD"), cancellable=true)
	public void redodrawBackground(int tint, CallbackInfo ci) {
		GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer bufferbuilder = tessellator.getBuffer();
        this.mc.getTextureManager().bindTexture(optionsBackground);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, (double)this.height, 0.0D).tex(0.0D, (double)((float)this.height / 32.0F + (float)tint)).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos((double)this.width, (double)this.height, 0.0D).tex((double)((float)this.width / 32.0F), (double)((float)this.height / 32.0F + (float)tint)).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos((double)this.width, 0.0D, 0.0D).tex((double)((float)this.width / 32.0F), (double)tint).color(64, 64, 64, 255).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double)tint).color(75, 64, 64, 255).endVertex();
        tessellator.draw();
        
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        Tessellator tessellator2 = Tessellator.getInstance();
        VertexBuffer bufferbuilder2 = tessellator2.getBuffer();
        this.mc.getTextureManager().bindTexture(bottle);
        int[] rgba= {64,64,64,100};
        bufferbuilder2.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0D, (double)this.height, 0.0D).tex(0.0D, (double)((float)this.height / 32.0F + (float)tint)).color(rgba[0],rgba[1],rgba[2],rgba[3]).endVertex();
        bufferbuilder.pos((double)this.width, (double)this.height, 0.0D).tex((double)((float)this.width / 32.0F), (double)((float)this.height / 32.0F + (float)tint)).color(rgba[0],rgba[1],rgba[2],rgba[3]).endVertex();
        bufferbuilder.pos((double)this.width, 0.0D, 0.0D).tex((double)((float)this.width / 32.0F), (double)tint).color(rgba[0],rgba[1],rgba[2],rgba[3]).endVertex();
        bufferbuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, (double)tint).color(rgba[0],rgba[1],rgba[2],rgba[3]).endVertex();
        tessellator2.draw();
        
        ci.cancel();
	}
}
