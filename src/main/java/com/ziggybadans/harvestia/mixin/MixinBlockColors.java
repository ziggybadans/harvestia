package com.ziggybadans.harvestia.mixin;

import com.ziggybadans.harvestia.world.SeasonColorManager;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.world.FoliageColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockColors.class)
public abstract class MixinBlockColors {
    @Inject(method = "create", at = @At("RETURN"))
    private static void injectSeasonalLeavesColor(CallbackInfoReturnable<BlockColors> cir) {
        BlockColors blockColors = cir.getReturnValue();

        // Array of blocks
        Block[] seasonalBlocks = new Block[] {
                Blocks.OAK_LEAVES,
                Blocks.BIRCH_LEAVES,
                Blocks.SPRUCE_LEAVES,
                Blocks.VINE,
                Blocks.GRASS_BLOCK,
                Blocks.GRASS,
                Blocks.TALL_GRASS
        };

        for (Block block : seasonalBlocks) {
            // Update the color provider for oak leaves
            blockColors.registerColorProvider((state, world, pos, tintIndex) -> {
                // Use the Biome dependent foliage color for the world and position, and then apply the seasonal tint
                return world == null ? FoliageColors.getDefaultColor() : SeasonColorManager.getSeasonalColor(world, pos);
            }, block);
        }

    }
}
