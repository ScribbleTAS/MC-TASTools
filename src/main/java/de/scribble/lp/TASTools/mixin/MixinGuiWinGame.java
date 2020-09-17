package de.scribble.lp.TASTools.mixin;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Lists;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

@Mixin(GuiWinGame.class)
public abstract class MixinGuiWinGame extends GuiScreen{
	@Shadow
	private List<String> field_146582_i;
	@Shadow
	private int field_146579_r;
	@Shadow
	private int field_146581_h;
	@Shadow
	private float field_146578_s;
	@Shadow
	private static ResourceLocation MINECRAFT_LOGO;
	@Shadow
	private static ResourceLocation VIGNETTE_TEXTURE;
	@Shadow
	private static Logger logger;

	@Inject(method = "initGui", at = @At("HEAD"), cancellable = true)
    public void redoInitGui(CallbackInfo ci)
    {
		if (this.field_146582_i == null)
        {
            this.field_146582_i = Lists.<String>newArrayList();
            IResource iresource = null;

            try
            {
                String s = "";
                String s1 = "" + TextFormatting.WHITE + TextFormatting.OBFUSCATED + TextFormatting.GREEN + TextFormatting.AQUA;
                int i = 274;
                iresource = this.mc.getResourceManager().getResource(new ResourceLocation("texts/end.txt"));
                InputStream inputstream = iresource.getInputStream();
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, Charsets.UTF_8));
                Random random = new Random(8124371L);

                while ((s = bufferedreader.readLine()) != null)
                {
                    String s2;
                    String s3;

                    for (s = s.replaceAll("PLAYERNAME", "[TAS] "+this.mc.getSession().getUsername()); s.contains(s1); s = s2 + TextFormatting.WHITE + TextFormatting.OBFUSCATED + "XXXXXXXX".substring(0, random.nextInt(4) + 3) + s3)
                    {
                        int j = s.indexOf(s1);
                        s2 = s.substring(0, j);
                        s3 = s.substring(j + s1.length());
                    }

                    this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(s, i));
                    this.field_146582_i.add("");
                }

                inputstream.close();

                for (int k = 0; k < 8; ++k)
                {
                    this.field_146582_i.add("");
                }

                inputstream = this.mc.getResourceManager().getResource(new ResourceLocation("texts/credits.txt")).getInputStream();
                bufferedreader = new BufferedReader(new InputStreamReader(inputstream, Charsets.UTF_8));

                while ((s = bufferedreader.readLine()) != null)
                {
                    s = s.replaceAll("PLAYERNAME", "[TAS] "+this.mc.getSession().getUsername());
                    s = s.replaceAll("\t", "    ");
                    this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(s, i));
                    this.field_146582_i.add("");
                }

                inputstream.close();
                this.field_146579_r = this.field_146582_i.size() * 12;
            }
            catch (Exception exception)
            {
                logger.error((String)"Couldn\'t load credits", (Throwable)exception);
            }
            finally
            {
                IOUtils.closeQuietly((Closeable)iresource);
            }
        }
        ci.cancel();
    }
	@Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
	public void redoDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci)
    {
		this.drawWinGameScreen(mouseX, mouseY, partialTicks);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        int i = 274;
        int j = this.width / 2 - i / 2;
        int k = this.height + 50;
        float f = -((float)this.field_146581_h + partialTicks) * this.field_146578_s;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, f, 0.0F);
        this.mc.getTextureManager().bindTexture(MINECRAFT_LOGO);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//        this.drawTexturedModalRect(j, k, 0, 0, 155, 44);
//        this.drawTexturedModalRect(j + 155, k, 0, 45, 155, 44);
        
        this.drawTexturedModalRect(j, k, 0, 0, 99, 44);
        this.drawTexturedModalRect(j + 99, k, 129, 0, 27, 44);
        this.drawTexturedModalRect(j + 99 + 26, k, 126, 0, 3, 44);
        this.drawTexturedModalRect(j + 99 + 26 + 3, k, 99, 0, 26, 44);
        this.drawTexturedModalRect(j + 155, k, 0, 45, 155, 44);
        int l = k + 200;

        for (int i1 = 0; i1 < this.field_146582_i.size(); ++i1)
        {
            if (i1 == this.field_146582_i.size() - 1)
            {
                float f1 = (float)l + f - (float)(this.height / 2 - 6);

                if (f1 < 0.0F)
                {
                    GlStateManager.translate(0.0F, -f1, 0.0F);
                }
            }

            if ((float)l + f + 12.0F + 8.0F > 0.0F && (float)l + f < (float)this.height)
            {
                String s = (String)this.field_146582_i.get(i1);

                if (s.startsWith("[C]"))
                {
                    this.fontRendererObj.drawStringWithShadow(s.substring(3), (float)(j + (i - this.fontRendererObj.getStringWidth(s.substring(3))) / 2), (float)l, 16777215);
                }
                else
                {
                    this.fontRendererObj.fontRandom.setSeed((long)i1 * 4238972211L + (long)(this.field_146581_h / 4));
                    this.fontRendererObj.drawStringWithShadow(s, (float)j, (float)l, 16777215);
                }
            }

            l += 12;
        }

        GlStateManager.popMatrix();
        this.mc.getTextureManager().bindTexture(VIGNETTE_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR);
        int j1 = this.width;
        int k1 = this.height;
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        vertexbuffer.pos(0.0D, (double)k1, (double)this.zLevel).tex(0.0D, 1.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        vertexbuffer.pos((double)j1, (double)k1, (double)this.zLevel).tex(1.0D, 1.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        vertexbuffer.pos((double)j1, 0.0D, (double)this.zLevel).tex(1.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        vertexbuffer.pos(0.0D, 0.0D, (double)this.zLevel).tex(0.0D, 0.0D).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        super.drawScreen(mouseX, mouseY, partialTicks);
        ci.cancel();
    }
	@Shadow
	abstract void drawWinGameScreen(int mouseX, int mouseY, float partialTicks);
}
