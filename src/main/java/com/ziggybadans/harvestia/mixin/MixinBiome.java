package com.ziggybadans.harvestia.mixin;

import com.ziggybadans.harvestia.world.Season;
import com.ziggybadans.harvestia.world.SeasonSharedManager;
import com.ziggybadans.harvestia.world.SeasonTemperatureManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public abstract class MixinBiome {
    @Inject(method = "doesNotSnow", at = @At("HEAD"), cancellable = true)
    private void onDoesNotSnow(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        Season currentSeason = SeasonSharedManager.getCurrentSeason();

        Biome biome = (Biome) (Object) this;
        float baseTemperature = biome.getTemperature();

        // Retrieve the seasonal temperature modifier and apply it to the base temperature
        float modifier = SeasonTemperatureManager.getSeasonalTemperatureModifier(currentSeason);
        float adjustedTemperature = baseTemperature + modifier;

        float snowTemperatureThreshold = 0.15f; // Threshold above which it normally doesn't snow
        boolean doesNotSnow = adjustedTemperature >= snowTemperatureThreshold;

        cir.setReturnValue(doesNotSnow);
    }
}
