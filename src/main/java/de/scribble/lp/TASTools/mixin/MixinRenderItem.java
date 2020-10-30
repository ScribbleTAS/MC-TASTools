package de.scribble.lp.TASTools.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.scribble.lp.TASTools.shield.CustomTileEntityItemStackRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@Mixin(RenderItem.class)
public abstract class MixinRenderItem {
    @Unique
    private EntityLivingBase entity;

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/client/renderer/block/model/ItemCameraTransforms$TransformType;Z)V", at = @At("HEAD"))
    public void getAbstractClientPlayer(ItemStack stack, EntityLivingBase entitylivingbaseIn, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo ci) {
        entity = entitylivingbaseIn;
    }

    @Redirect(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/tileentity/TileEntityItemStackRenderer;renderByItem(Lnet/minecraft/item/ItemStack;)V"))
    private void redirectRenderByItem(TileEntityItemStackRenderer tileEntityItemStackRenderer, ItemStack itemStackIn) {
        if (itemStackIn.getItem() == Items.shield) {
            CustomTileEntityItemStackRenderer.instance.renderByItem(itemStackIn, entity);
        } else {
            tileEntityItemStackRenderer.renderByItem(itemStackIn);
        }
    }
}
