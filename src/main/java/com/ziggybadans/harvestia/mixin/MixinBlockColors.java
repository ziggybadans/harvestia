package com.ziggybadans.harvestia.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.client.color.block.BlockColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockColors.class)
public abstract class MixinBlockColors {
    @Inject(method = "create", at = @At("RETURN"))
    private static void injectOakLeavesColor(CallbackInfoReturnable<BlockColors> cir) {
        BlockColors blockColors = cir.getReturnValue();
        int brownColor = 0xA0522D; // This is the brown color

        // Replace the color provider for oak leaves with our brown color
        blockColors.registerColorProvider((state, world, pos, tintIndex) -> brownColor, Blocks.OAK_LEAVES);
    }
}
