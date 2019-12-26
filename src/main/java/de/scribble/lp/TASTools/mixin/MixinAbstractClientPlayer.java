package de.scribble.lp.TASTools.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;

@Mixin(AbstractClientPlayer.class)
public class MixinAbstractClientPlayer {

	@Inject(method="getLocationSkin", at=@At("HEAD"), cancellable=true)
	private void redogetLocationSkin(CallbackInfoReturnable<ResourceLocation> ci) {
		ci.setReturnValue(new ResourceLocation("tastools:textures/rob.png"));
		ci.cancel();
	}
} 