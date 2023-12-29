package com.ziggybadans.harvestia.mixin;

import com.ziggybadans.harvestia.world.SeasonColorManager;
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
    private static void injectOakLeavesColor(CallbackInfoReturnable<BlockColors> cir) {
        BlockColors blockColors = cir.getReturnValue();
        int defaultColor = FoliageColors.getDefaultColor(); // Store the default color
        int brownColor = 0xA0522D; // This is the brown color

        // Update the color provider for oak leaves
        blockColors.registerColorProvider((state, world, pos, tintIndex) -> {
            // Use the season color if enabled, otherwise default color
            return SeasonColorManager.isSeasonColorsEnabled() ? brownColor : defaultColor;
        }, Blocks.OAK_LEAVES);
    }
}
