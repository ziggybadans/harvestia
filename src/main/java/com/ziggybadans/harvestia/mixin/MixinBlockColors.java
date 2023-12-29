package com.ziggybadans.harvestia.mixin;

import com.ziggybadans.harvestia.world.SeasonColorManager;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockColors.class)
public abstract class MixinBlockColors {
    @Inject(method = "create", at = @At("RETURN"))
    private static void injectSeasonalLeavesColor(CallbackInfoReturnable<BlockColors> cir) {
        BlockColors blockColors = cir.getReturnValue();

        // Update the color provider for oak leaves
        blockColors.registerColorProvider((state, world, pos, tintIndex) -> {
            // Use the Biome dependent foliage color for the world and position, and then apply the seasonal tint
            //int biomeColor = world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor();
            return world == null ? FoliageColors.getDefaultColor() : SeasonColorManager.getSeasonalColor(world, pos);
        }, Blocks.OAK_LEAVES);
    }
}
