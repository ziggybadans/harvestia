package com.ziggybadans.harvestia.mixin;

import com.ziggybadans.harvestia.world.Season;
import com.ziggybadans.harvestia.world.SeasonSharedManager;
import com.ziggybadans.harvestia.world.SeasonTemperatureManager;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class MixinAbstractBlockState {
    // Inject at the beginning of the method
    @Inject(method = "scheduledTick", at = @At("HEAD"), cancellable = true)
    public void onScheduledTick(ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        BlockState state = (BlockState) (Object) this;
        Block block = state.getBlock();

        // Make sure this is only affecting water blocks
        if (block == Blocks.WATER) {
            Season currentSeason = SeasonSharedManager.getCurrentSeason();
            Biome biome = (Biome) (Object) this;
            float baseTemperature = biome.getTemperature();
            float modifier = SeasonTemperatureManager.getSeasonalTemperatureModifier(currentSeason);
            float adjustedTemperature = baseTemperature + modifier;
            float freezeTemperatureThreshold = 0.15f;

            // Check if the adjusted temperature is below our threshold for freezing
            if (adjustedTemperature < freezeTemperatureThreshold) {
                // Replace water with ice
                world.setBlockState(pos, Blocks.ICE.getDefaultState());
                // Cancel further scheduled ticks for this block to prevent it from trying to update after freezing
                ci.cancel();
            }
        }
    }
}
