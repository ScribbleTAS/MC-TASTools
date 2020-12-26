package de.scribble.lp.TASTools.mixin;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.scribble.lp.TASTools.fishmanip.FishManipEvents;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;

@Mixin(EntityFishHook.class)
public abstract class MixinEntityFishHook {
    @Redirect(method = "catchingFish", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextFloat()F", ordinal = 0, remap = false))
    private float isRainingAlwaysIncrementRedirect(Random random) {
        if (FishManipEvents.fishrigger.isActive()) {
            return 0; // Force nextFloat < .25
        } else {
            return random.nextFloat();
        }
    }

    @Redirect(method = "catchingFish", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextFloat()F", ordinal = 1, remap = false))
    private float noOpenSkyNeverDecrementRedirect(Random random) {
        if (FishManipEvents.fishrigger.isActive()) {
            return 1.0F; // Force nextFloat > 0.5
        } else {
            return random.nextFloat();
        }
    }

    @Redirect(method = "handleHookRetraction", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/storage/loot/LootTable;generateLootForPools(Ljava/util/Random;Lnet/minecraft/world/storage/loot/LootContext;)Ljava/util/List;"))
    private List<ItemStack> redirectGenerateLoot(LootTable lootTable, Random rand, LootContext context) {
        if (FishManipEvents.fishrigger.isActive()) {
            return Collections.singletonList(FishManipEvents.fishrigger.getItemFromTop());
        } else {
            return lootTable.generateLootForPools(rand, context);
        }
    }
}
