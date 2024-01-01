package com.ziggybadans.harvestia.mixin;

import com.ziggybadans.harvestia.network.ThreadLocalManager;
import com.ziggybadans.harvestia.world.Season;
import com.ziggybadans.harvestia.world.SeasonState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public abstract class BiomeMixin {
    @Inject(method = "doesNotSnow", at = @At("HEAD"), cancellable = true)
    private void injectDoesNotSnow(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        MinecraftServer server = ThreadLocalManager.getServer();
        if (server != null) {
            SeasonState seasonState = SeasonState.get(server);
            Season currentSeason = seasonState.getCurrentSeason();
            //Harvestia.LOGGER.info("(Temperature) Current season is: " + currentSeason);

            Biome biome = (Biome)(Object)this;
            float baseTemperature = biome.getTemperature();
            float snowTemperatureThreshold = 0.15f;
            //Harvestia.LOGGER.info("Current base temperature is: " + baseTemperature);

            float modifier = currentSeason.getTemperatureModifier();
            //Harvestia.LOGGER.info("Current temperature modifier is: " + modifier);

            float adjustedTemperature = baseTemperature + modifier;
            boolean doesNotSnow = (adjustedTemperature >= snowTemperatureThreshold);
            //Harvestia.LOGGER.info("Does it snow?: " + !doesNotSnow);

            cir.setReturnValue(doesNotSnow);
        }
    }
}
