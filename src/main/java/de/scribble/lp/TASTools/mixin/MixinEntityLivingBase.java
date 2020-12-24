package de.scribble.lp.TASTools.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.scribble.lp.TASTools.freezeV2.FreezeHandlerClient;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity{
	public MixinEntityLivingBase(World worldIn) {
		super(worldIn);
	}
	@Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;move(Lnet/minecraft/entity/MoverType;DDD)V"))
	public void redirectTravelMotion(EntityLivingBase entity, MoverType type, double x, double y, double z) {
		if((EntityLivingBase)(Object)this instanceof EntityPlayerSP) {
        	FreezeHandlerClient.redirectMotion(entity, x, y, z);
        	motionX=FreezeHandlerClient.getMoX();
        	motionY=FreezeHandlerClient.getMoY();
        	motionZ=FreezeHandlerClient.getMoZ();
        }
		this.move(type, motionX, motionY, motionZ);
	}
	@Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;moveRelative(FFFF)V"))
	public void redirectTravelRelMotion(EntityLivingBase entity, float x, float y, float z, float friction) {
		if((EntityLivingBase)(Object)this instanceof EntityPlayerSP) {
        	FreezeHandlerClient.redirectRelativeMotion(entity, x, y, z);
        	x=FreezeHandlerClient.getRelX();
        	y=FreezeHandlerClient.getRelY();
        	z=FreezeHandlerClient.getRelZ();
        }
		this.moveRelative(x, y, z, friction);
	}
		
}
