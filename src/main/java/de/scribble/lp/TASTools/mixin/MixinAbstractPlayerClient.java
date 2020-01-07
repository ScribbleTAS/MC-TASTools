package de.scribble.lp.TASTools.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;

@Mixin(AbstractClientPlayer.class)
public class MixinAbstractPlayerClient {
	@Shadow
	NetworkPlayerInfo playerInfo;
	@Inject(method="getLocationSkin", at=@At("HEAD"), cancellable=true)
	private void redogetLocationSkin(CallbackInfoReturnable<ResourceLocation> ci) {
		ci.setReturnValue(new ResourceLocation("tastools:textures/rob.png"));
		ci.cancel();
	}
	@Inject(method="getLocationCape", at=@At("HEAD"), cancellable=true)
	private void redogetLocationCape(CallbackInfoReturnable<ResourceLocation> ci) {
		if(playerInfo.getGameProfile().getName().equals("TAS_bot"))ci.setReturnValue(new ResourceLocation("tastools:textures/capes/tascape.png"));
		else ci.setReturnValue(new ResourceLocation("tastools:textures/capes/bottlecape.png"));
		ci.cancel();
	}
}