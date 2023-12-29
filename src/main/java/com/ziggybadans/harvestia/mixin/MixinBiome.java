package com.ziggybadans.harvestia.mixin;

import com.ziggybadans.harvestia.world.SeasonColorManager;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public abstract class MixinBiome {
    @Shadow
    public abstract float getTemperature();

    @Inject(method = "getTemperature()F", at = @At("RETURN"), cancellable = true)
    private void injectSeasonalTemperatureAdjustment(CallbackInfoReturnable<Float> cir) {
        // Retrieve the original temperature
        Biome self = (Biome)(Object)this;
        float originalTemperature = cir.getReturnValue();

        // Now retrieve the adjusted temperature based on the current season
        float adjustedTemperature = SeasonColorManager.getAdjustedBiomeTemperature(self);

        // Set the adjusted temperature as the return value
        cir.setReturnValue(adjustedTemperature);
    }
}
