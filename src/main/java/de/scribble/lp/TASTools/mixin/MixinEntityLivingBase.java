package de.scribble.lp.TASTools.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.scribble.lp.TASTools.freeze.FreezeHandlerVer2;
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
		// TODO Auto-generated constructor stub
	}
	@Shadow
	private float jumpMovementFactor;
	@Shadow
	private float prevLimbSwingAmount;
	@Shadow
	private float limbSwingAmount;
	@Shadow
	private float limbSwing;
	
	//@Inject(method = "travel", at = @At(value = "HEAD"), cancellable = true)
	public void injctTravel(float strafe, float vertical, float forward, CallbackInfo ci) {
		if (this.isServerWorld() || this.canPassengerSteer())
        {
            if (!this.isInWater() || (EntityLivingBase)(Object)this instanceof EntityPlayer && ((EntityPlayer)(EntityLivingBase)(Object)this).capabilities.isFlying)
            {
                if (!this.isInLava() || (EntityLivingBase)(Object)this instanceof EntityPlayer && ((EntityPlayer)(EntityLivingBase)(Object)this).capabilities.isFlying)
                {
                    if (this.isElytraFlying())
                    {
                        if (this.motionY > -0.5D)
                        {
                            this.fallDistance = 1.0F;
                        }

                        Vec3d vec3d = this.getLookVec();
                        float rotation = this.rotationPitch * 0.017453292F;
                        double direction = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
                        double movement = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                        double lengthvector = vec3d.lengthVector();
                        float gravityfactor = MathHelper.cos(rotation);
                        gravityfactor = (float)((double)gravityfactor * (double)gravityfactor * Math.min(1.0D, lengthvector / 0.4D));
                        this.motionY += -0.08D + (double)gravityfactor * 0.06D;

                        if (this.motionY < 0.0D && direction > 0.0D)
                        {
                            double d2 = this.motionY * -0.1D * (double)gravityfactor;
                            this.motionY += d2;
                            this.motionX += vec3d.x * d2 / direction;
                            this.motionZ += vec3d.z * d2 / direction;
                        }

                        if (rotation < 0.0F)
                        {
                            double d10 = movement * (double)(-MathHelper.sin(rotation)) * 0.04D;
                            this.motionY += d10 * 3.2D;
                            this.motionX -= vec3d.x * d10 / direction;
                            this.motionZ -= vec3d.z * d10 / direction;
                        }

                        if (direction > 0.0D)
                        {
                            this.motionX += (vec3d.x / direction * movement - this.motionX) * 0.1D;
                            this.motionZ += (vec3d.z / direction * movement - this.motionZ) * 0.1D;
                        }

                        this.motionX *= 0.9900000095367432D;
                        this.motionY *= 0.9800000190734863D;
                        this.motionZ *= 0.9900000095367432D;
                        if((EntityLivingBase)(Object)this instanceof EntityPlayerSP) {
                        	FreezeHandlerVer2.redirectMotion((EntityLivingBase)(Object)this, motionX, motionY, motionZ);
                        	motionX=FreezeHandlerVer2.getMoX();
                        	motionY=FreezeHandlerVer2.getMoY();
                        	motionZ=FreezeHandlerVer2.getMoZ();
                        }
                        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                        
                        //Flying into a wall
                        if (this.collidedHorizontally && !this.world.isRemote)
                        {
                            double d11 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                            double d3 = movement - d11;
                            float f5 = (float)(d3 * 10.0D - 3.0D);

                            if (f5 > 0.0F)
                            {
                                this.playSound(this.getFallSound((int)f5), 1.0F, 1.0F);
                                this.attackEntityFrom(DamageSource.FLY_INTO_WALL, f5);
                            }
                        }

                        if (this.onGround && !this.world.isRemote)
                        {
                            this.setFlag(7, false);
                        }
                    }
                    //^^^^Elytra^^^^^
                    //vvvvNot flyingVVVV
                    else
                    {
                        float slipperiness = 0.91F;
                        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(this.posX, this.getEntityBoundingBox().minY - 1.0D, this.posZ);

                        if (this.onGround)
                        {
                            IBlockState underState = this.world.getBlockState(blockpos$pooledmutableblockpos);
                            slipperiness = underState.getBlock().getSlipperiness(underState, this.world, blockpos$pooledmutableblockpos, this) * 0.91F;
                        }

                        float friction = 0.16277136F / (slipperiness * slipperiness * slipperiness);
                        float totalfriction;

                        if (this.onGround)
                        {
                            totalfriction = this.getAIMoveSpeed() * friction;
                        }
                        else
                        {
                            totalfriction = this.jumpMovementFactor;
                        }
                        if((EntityLivingBase)(Object)this instanceof EntityPlayerSP) {
                        	FreezeHandlerVer2.redirectRelativeMotion((EntityLivingBase)(Object)this, strafe, vertical, forward);
                        	strafe=FreezeHandlerVer2.getRelX();
                        	vertical=FreezeHandlerVer2.getRelY();
                        	forward=FreezeHandlerVer2.getRelZ();
                        }
                        this.moveRelative(strafe, vertical, forward, totalfriction);
                        slipperiness = 0.91F;

                        if (this.onGround)
                        {
                            IBlockState underState = this.world.getBlockState(blockpos$pooledmutableblockpos.setPos(this.posX, this.getEntityBoundingBox().minY - 1.0D, this.posZ));
                            slipperiness = underState.getBlock().getSlipperiness(underState, this.world, blockpos$pooledmutableblockpos, this) * 0.91F;
                        }

                        if (this.isOnLadder()) //Sliding down the ladder
                        {
                            float f9 = 0.15F;
                            this.motionX = MathHelper.clamp(this.motionX, -0.15000000596046448D, 0.15000000596046448D);
                            this.motionZ = MathHelper.clamp(this.motionZ, -0.15000000596046448D, 0.15000000596046448D);
                            this.fallDistance = 0.0F;

                            if (this.motionY < -0.15D)
                            {
                                this.motionY = -0.15D;
                            }

                            boolean flag = this.isSneaking() && (EntityLivingBase)(Object)this instanceof EntityPlayer;

                            if (flag && this.motionY < 0.0D)
                            {
                                this.motionY = 0.0D;
                            }
                        }
                        if((EntityLivingBase)(Object)this instanceof EntityPlayerSP) {
                        	FreezeHandlerVer2.redirectMotion((EntityLivingBase)(Object)this, motionX, motionY, motionZ);
                        	motionX=FreezeHandlerVer2.getMoX();
                        	motionY=FreezeHandlerVer2.getMoY();
                        	motionZ=FreezeHandlerVer2.getMoZ();
                        }
                        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

                        if (this.collidedHorizontally && this.isOnLadder())	//Climbing ladder
                        {
                            this.motionY = 0.2D;
                        }

                        if (this.isPotionActive(MobEffects.LEVITATION)) //Levitation
                        {
                            this.motionY += (0.05D * (double)(this.getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1) - this.motionY) * 0.2D;
                        }
                        else
                        {
                            blockpos$pooledmutableblockpos.setPos(this.posX, 0.0D, this.posZ);

                            if (!this.world.isRemote || this.world.isBlockLoaded(blockpos$pooledmutableblockpos) && this.world.getChunkFromBlockCoords(blockpos$pooledmutableblockpos).isLoaded())
                            {
                                if (!this.hasNoGravity())
                                {
                                    this.motionY -= 0.08D;	//Apply gravity
                                }
                            }
                            else if (this.posY > 0.0D)
                            {
                                this.motionY = -0.1D;	//If chunk is not loaded
                            }
                            else
                            {
                                this.motionY = 0.0D;
                            }
                        }

                        this.motionY *= 0.9800000190734863D;
                        this.motionX *= (double)slipperiness;
                        this.motionZ *= (double)slipperiness;
                        blockpos$pooledmutableblockpos.release();
                    }
                }
                else
                {
                    double d4 = this.posY;
                    if((EntityLivingBase)(Object)this instanceof EntityPlayerSP) {
                    	FreezeHandlerVer2.redirectRelativeMotion((EntityLivingBase)(Object)this, strafe, vertical, forward);
                    	strafe=FreezeHandlerVer2.getRelX();
                    	vertical=FreezeHandlerVer2.getRelY();
                    	forward=FreezeHandlerVer2.getRelZ();
                    }
                    this.moveRelative(strafe, vertical, forward, 0.02F);
                    if((EntityLivingBase)(Object)this instanceof EntityPlayerSP) {
                    	FreezeHandlerVer2.redirectMotion((EntityLivingBase)(Object)this, motionX, motionY, motionZ);
                    	motionX=FreezeHandlerVer2.getMoX();
                    	motionY=FreezeHandlerVer2.getMoY();
                    	motionZ=FreezeHandlerVer2.getMoZ();
                    }
                    this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                    this.motionX *= 0.5D;
                    this.motionY *= 0.5D;
                    this.motionZ *= 0.5D;

                    if (!this.hasNoGravity())
                    {
                        this.motionY -= 0.02D;
                    }

                    if (this.collidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d4, this.motionZ))
                    {
                        this.motionY = 0.30000001192092896D;
                    }
                }
            }
            else
            {
                double d0 = this.posY;
                float f1 = this.getWaterSlowDown();
                float f2 = 0.02F;
                float f3 = (float)EnchantmentHelper.getDepthStriderModifier((EntityLivingBase)(Object)this);

                if (f3 > 3.0F)
                {
                    f3 = 3.0F;
                }

                if (!this.onGround)
                {
                    f3 *= 0.5F;
                }

                if (f3 > 0.0F)
                {
                    f1 += (0.54600006F - f1) * f3 / 3.0F;
                    f2 += (this.getAIMoveSpeed() - f2) * f3 / 3.0F;
                }
                if((EntityLivingBase)(Object)this instanceof EntityPlayerSP) {
                	FreezeHandlerVer2.redirectRelativeMotion((EntityLivingBase)(Object)this, strafe, vertical, forward);
                	strafe=FreezeHandlerVer2.getRelX();
                	vertical=FreezeHandlerVer2.getRelY();
                	forward=FreezeHandlerVer2.getRelZ();
                }
                this.moveRelative(strafe, vertical, forward, f2);
                if((EntityLivingBase)(Object)this instanceof EntityPlayerSP) {
                	FreezeHandlerVer2.redirectMotion((EntityLivingBase)(Object)this, motionX, motionY, motionZ);
                	motionX=FreezeHandlerVer2.getMoX();
                	motionY=FreezeHandlerVer2.getMoY();
                	motionZ=FreezeHandlerVer2.getMoZ();
                }
                this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                this.motionX *= (double)f1;
                this.motionY *= 0.800000011920929D;
                this.motionZ *= (double)f1;

                if (!this.hasNoGravity())
                {
                    this.motionY -= 0.02D;
                }

                if (this.collidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d0, this.motionZ))
                {
                    this.motionY = 0.30000001192092896D;
                }
            }
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d5 = this.posX - this.prevPosX;
        double d7 = this.posZ - this.prevPosZ;
        double d9 = this instanceof net.minecraft.entity.passive.EntityFlying ? this.posY - this.prevPosY : 0.0D;
        float f10 = MathHelper.sqrt(d5 * d5 + d9 * d9 + d7 * d7) * 4.0F;

        if (f10 > 1.0F)
        {
            f10 = 1.0F;
        }

        this.limbSwingAmount += (f10 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
        ci.cancel();
	}
	@Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;move(Lnet/minecraft/entity/MoverType;DDD)V"))
	public void redirectTravelMotion(EntityLivingBase entity, MoverType type, double x, double y, double z) {
		if((EntityLivingBase)(Object)this instanceof EntityPlayerSP) {
        	FreezeHandlerVer2.redirectMotion(entity, x, y, z);
        	motionX=FreezeHandlerVer2.getMoX();
        	motionY=FreezeHandlerVer2.getMoY();
        	motionZ=FreezeHandlerVer2.getMoZ();
        }
		this.move(type, motionX, motionY, motionZ);
	}
	@Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityLivingBase;moveRelative(FFFF)V"))
	public void redirectTravelRelMotion(EntityLivingBase entity, float x, float y, float z, float friction) {
		if((EntityLivingBase)(Object)this instanceof EntityPlayerSP) {
        	FreezeHandlerVer2.redirectRelativeMotion(entity, x, y, z);
        	x=FreezeHandlerVer2.getRelX();
        	y=FreezeHandlerVer2.getRelY();
        	z=FreezeHandlerVer2.getRelZ();
        }
		this.moveRelative(x, y, z, friction);
	}
		
	@Shadow
	protected abstract float getWaterSlowDown();
	@Shadow
	protected abstract PotionEffect getActivePotionEffect(Potion levitation);
	@Shadow
	protected abstract boolean isPotionActive(Potion levitation);
	@Shadow
	protected abstract boolean isOnLadder();
	@Shadow
	protected abstract float getAIMoveSpeed();
	@Shadow
	protected abstract SoundEvent getFallSound(int f5);
	@Shadow
	protected abstract boolean isElytraFlying();
	@Shadow
	protected abstract boolean isServerWorld();
}
