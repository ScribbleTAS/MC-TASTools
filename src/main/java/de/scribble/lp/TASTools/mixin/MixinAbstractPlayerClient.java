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
	private static final ResourceLocation bottlecape = new ResourceLocation("tastools:textures/capes/bottlecape.png");
	private static final ResourceLocation scribblecape = new ResourceLocation("tastools:textures/capes/scribblecape.png");
	private static final ResourceLocation creepercape = new ResourceLocation("tastools:textures/capes/sdfghs.png");
	private static final ResourceLocation wellcape = new ResourceLocation("tastools:textures/capes/wellcape.png");
	private static final ResourceLocation tascape = new ResourceLocation("tastools:textures/capes/tascape.png");
	private static final ResourceLocation vaughcape = new ResourceLocation("tastools:textures/capes/vaugh.png");
	private static final ResourceLocation admira23MattyBcape = new ResourceLocation("tastools:textures/capes/hiscribble.png");
	@Shadow
	NetworkPlayerInfo playerInfo;
	@Inject(method="getLocationSkin", at=@At("HEAD"), cancellable=true)
	private void redogetLocationSkin(CallbackInfoReturnable<ResourceLocation> ci) {
		ci.setReturnValue(new ResourceLocation("tastools:textures/rob.png"));
		ci.cancel();
	}
	@Inject(method="getLocationCape", at=@At("HEAD"), cancellable=true)
	private void redogetLocationCape(CallbackInfoReturnable<ResourceLocation> ci) {
		if(playerInfo.getGameProfile().getId().toString().equals("f3112feb-00c1-4de8-9829-53b940342996"))ci.setReturnValue(scribblecape);
		else if(playerInfo.getGameProfile().getId().toString().equals("79658727-8778-4dc0-9d02-21f20ed913e7"))ci.setReturnValue(creepercape);
		else if(playerInfo.getGameProfile().getId().toString().equals("79658727-8778-4dc0-9d02-21f20ed913e7"))ci.setReturnValue(creepercape);
		else if(playerInfo.getGameProfile().getId().toString().equals("a962e219-c16e-4c3b-b38b-b675d10f92ee"))ci.setReturnValue(wellcape);
		else if(playerInfo.getGameProfile().getId().toString().equals("e5687c44-65c3-4e76-a233-2550a5597ddc"))ci.setReturnValue(vaughcape);
		else if(playerInfo.getGameProfile().getId().toString().equals("839474a8-ba49-468b-9246-ed80c78383aa"))ci.setReturnValue(admira23MattyBcape);
		else ci.setReturnValue(scribblecape);
		ci.cancel();
	}
}