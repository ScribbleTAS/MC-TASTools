package de.scribble.lp.TASTools.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.scribble.lp.TASTools.CommonProxy;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;

@Mixin(EntityFishHook.class)
public abstract class MixinEntityFishHook extends Entity{
	
	public MixinEntityFishHook(World worldIn) {
		super(worldIn);
	}
	@Shadow
	private int ticksCatchable;
	@Shadow
	private int ticksCaughtDelay;
	@Shadow
	private int ticksCatchableDelay;
	@Shadow
	private float fishApproachAngle;
	@Shadow
	private int lureSpeed;
	@Shadow
	private Entity caughtEntity;
	@Shadow
	private EntityPlayer angler;
	@Shadow
	private int luck;
	@Shadow
	private boolean inGround;

	@Inject(method="catchingFish", at = @At("HEAD"), cancellable = true)
    private void redoCatchingFish(BlockPos playerPos, CallbackInfo ci)
    {
        WorldServer worldserver = (WorldServer)this.world;
        int i = 1;
        BlockPos blockpos = playerPos.up();
        if (CommonProxy.fishrigger.isActive()){
        	if (this.world.isRainingAt(blockpos))
	        {
	            ++i;
	        }
	
        }else {
	        if (this.rand.nextFloat() < 0.25F && this.world.isRainingAt(blockpos))
	        {
	            ++i;
	        }
	
	        if (this.rand.nextFloat() < 0.5F && !this.world.canSeeSky(blockpos))
	        {
	            --i;
	        }
        }
        if (this.ticksCatchable > 0)
        {
            --this.ticksCatchable;

            if (this.ticksCatchable <= 0)
            {
                this.ticksCaughtDelay = 0;
                this.ticksCatchableDelay = 0;
            }
            else
            {
                this.motionY -= 0.2D * (double)this.rand.nextFloat() * (double)this.rand.nextFloat();
            }
        }
        else if (this.ticksCatchableDelay > 0)
        {
            this.ticksCatchableDelay -= i;

            if (this.ticksCatchableDelay > 0)
            {
                this.fishApproachAngle = (float)((double)this.fishApproachAngle + this.rand.nextGaussian() * 4.0D);
                float f = this.fishApproachAngle * 0.017453292F;
                float f1 = MathHelper.sin(f);
                float f2 = MathHelper.cos(f);
                double d0 = this.posX + (double)(f1 * (float)this.ticksCatchableDelay * 0.1F);
                double d1 = (double)((float)MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0F);
                double d2 = this.posZ + (double)(f2 * (float)this.ticksCatchableDelay * 0.1F);
                IBlockState state = worldserver.getBlockState(new BlockPos(d0, d1 - 1.0D, d2));

                if (state.getMaterial() == Material.WATER)
                {
                    if (this.rand.nextFloat() < 0.15F)
                    {
                        worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d0, d1 - 0.10000000149011612D, d2, 1, (double)f1, 0.1D, (double)f2, 0.0D);
                    }

                    float f3 = f1 * 0.04F;
                    float f4 = f2 * 0.04F;
                    worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d0, d1, d2, 0, (double)f4, 0.01D, (double)(-f3), 1.0D);
                    worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d0, d1, d2, 0, (double)(-f4), 0.01D, (double)f3, 1.0D);
                }
            }
            else
            {
            	CommonProxy.logger.info("FishManip: Caught a fish!");
                this.motionY = (double)(-0.4F * MathHelper.nextFloat(this.rand, 0.6F, 1.0F));
                this.playSound(SoundEvents.ENTITY_BOBBER_SPLASH, 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                double d3 = this.getEntityBoundingBox().minY + 0.5D;
                worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX, d3, this.posZ, (int)(1.0F + this.width * 20.0F), (double)this.width, 0.0D, (double)this.width, 0.20000000298023224D);
                worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, this.posX, d3, this.posZ, (int)(1.0F + this.width * 20.0F), (double)this.width, 0.0D, (double)this.width, 0.20000000298023224D);
                this.ticksCatchable = MathHelper.getInt(this.rand, 20, 40);
            }
        }
        else if (this.ticksCaughtDelay > 0)
        {
            this.ticksCaughtDelay -= i;
            float f5 = 0.15F;

            if (this.ticksCaughtDelay < 20)
            {
                f5 = (float)((double)f5 + (double)(20 - this.ticksCaughtDelay) * 0.05D);
            }
            else if (this.ticksCaughtDelay < 40)
            {
                f5 = (float)((double)f5 + (double)(40 - this.ticksCaughtDelay) * 0.02D);
            }
            else if (this.ticksCaughtDelay < 60)
            {
                f5 = (float)((double)f5 + (double)(60 - this.ticksCaughtDelay) * 0.01D);
            }

            if (this.rand.nextFloat() < f5)
            {
                float f6 = MathHelper.nextFloat(this.rand, 0.0F, 360.0F) * 0.017453292F;
                float f7 = MathHelper.nextFloat(this.rand, 25.0F, 60.0F);
                double d4 = this.posX + (double)(MathHelper.sin(f6) * f7 * 0.1F);
                double d5 = (double)((float)MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0F);
                double d6 = this.posZ + (double)(MathHelper.cos(f6) * f7 * 0.1F);
                IBlockState state = worldserver.getBlockState(new BlockPos((int) d4, (int) d5 - 1, (int) d6));

                if (state.getMaterial() == Material.WATER)
                {
                    worldserver.spawnParticle(EnumParticleTypes.WATER_SPLASH, d4, d5, d6, 2 + this.rand.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
                }
            }

            if (this.ticksCaughtDelay <= 0)
            {
                this.fishApproachAngle = MathHelper.nextFloat(this.rand, 0.0F, 360.0F);
                
                if(CommonProxy.fishrigger.isActive()) {
                	this.ticksCatchableDelay=20;
                }else {
                	this.ticksCatchableDelay = MathHelper.getInt(this.rand, 20, 80);
                }
            }
        }
        else
        {
        	if(CommonProxy.fishrigger.isActive()) {
            	this.ticksCatchableDelay=100;
            }else {
            	this.ticksCaughtDelay = MathHelper.getInt(this.rand, 100, 600);
            }
            this.ticksCaughtDelay -= this.lureSpeed * 20 * 5;
        }
        ci.cancel();
    }
	
	@Inject(method="handleHookRetraction", at = @At("HEAD"), cancellable = true)
	public void redoHandleHookRetraction(CallbackInfoReturnable<Integer> ci)
    {
        if (!this.world.isRemote && this.angler != null)
        {
            int i = 0;

            net.minecraftforge.event.entity.player.ItemFishedEvent event = null;
            if (this.caughtEntity != null)
            {
                this.bringInHookedEntity();
                this.world.setEntityState(this, (byte)31);
                i = this.caughtEntity instanceof EntityItem ? 3 : 5;
            }
            else if (this.ticksCatchable > 0)
            {
            	List<ItemStack> result = new ArrayList<ItemStack>();
            	if (CommonProxy.fishrigger.isActive()) {
            		result.add(CommonProxy.fishrigger.getItemFromTop());
            	}else {
            		LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)this.world);
                	lootcontext$builder.withLuck((float)this.luck + this.angler.getLuck()).withPlayer(this.angler).withLootedEntity(this); // Forge: add player & looted entity to LootContext
                	result = this.world.getLootTableManager().getLootTableFromLocation(LootTableList.GAMEPLAY_FISHING).generateLootForPools(this.rand, lootcontext$builder.build());
            	}
                event = new net.minecraftforge.event.entity.player.ItemFishedEvent(result, this.inGround ? 2 : 1, (EntityFishHook)(Object)this);
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
                if (event.isCanceled())
                {
                    this.setDead();
                    ci.setReturnValue(event.getRodDamage());
                }

                for (ItemStack itemstack : result)
                {
                    EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY, this.posZ, itemstack);
                    double d0 = this.angler.posX - this.posX;
                    double d1 = this.angler.posY - this.posY;
                    double d2 = this.angler.posZ - this.posZ;
                    double d3 = (double)MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    double d4 = 0.1D;
                    entityitem.motionX = d0 * 0.1D;
                    entityitem.motionY = d1 * 0.1D + (double)MathHelper.sqrt(d3) * 0.08D;
                    entityitem.motionZ = d2 * 0.1D;
                    this.world.spawnEntity(entityitem);
                    this.angler.world.spawnEntity(new EntityXPOrb(this.angler.world, this.angler.posX, this.angler.posY + 0.5D, this.angler.posZ + 0.5D, this.rand.nextInt(6) + 1));
                    Item item = itemstack.getItem();

                    if (item == Items.FISH || item == Items.COOKED_FISH)
                    {
                        this.angler.addStat(StatList.FISH_CAUGHT, 1);
                    }
                }

                i = 1;
            }

            if (this.inGround)
            {
                i = 2;
            }

            this.setDead();
            ci.setReturnValue(event == null ? i : event.getRodDamage());
        }
        else
        {
        	ci.setReturnValue(0);
        }
        ci.cancel();
    }

	protected abstract void bringInHookedEntity();
}
