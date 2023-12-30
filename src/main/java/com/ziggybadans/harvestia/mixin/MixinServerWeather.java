package com.ziggybadans.harvestia.mixin;

import com.ziggybadans.harvestia.world.Season;
import com.ziggybadans.harvestia.world.SeasonSharedManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class MixinServerWeather {
    @Unique
    private boolean lastWasRaining = false;

    @Inject(method = "tickWeather", at = @At(value = "HEAD"))
    private void onTickWeatherStart(CallbackInfo ci) {
        ServerWorld self = (ServerWorld) (Object) this;
        ServerWorldProperties properties = (ServerWorldProperties) self.getLevelProperties();

        boolean currentlyRaining = self.isRaining();

        // Only update weather times when the weather state changes
        if (lastWasRaining != currentlyRaining) {
            Season currentSeason = SeasonSharedManager.getCurrentSeason();

            // Multipliers
            float clearWeatherMultiplier = 1.0f;
            float rainMultiplier = 1.0f;

            switch (currentSeason) {
                case SUMMER:
                    clearWeatherMultiplier = 0.25f; // Clear weather periods are 4 times shorter in summer
                    rainMultiplier = 0.5f; // Rain periods are twice as short in summer
                    break;
                // Add cases for other seasons if you want different adjustments
            }

            // Adjust the weather times dynamically based on the current season
            if (!currentlyRaining && lastWasRaining) {
                // When it stops raining, adjust the clearWeatherTime
                int baseClearTime = properties.getClearWeatherTime();
                int adjustedClearTime = Math.max((int) (baseClearTime * clearWeatherMultiplier), 1);
                properties.setClearWeatherTime(adjustedClearTime);
            } else if (currentlyRaining && !lastWasRaining) {
                // When it starts raining, adjust the rainTime
                int baseRainTime = properties.getRainTime();
                int adjustedRainTime = Math.max((int) (baseRainTime * rainMultiplier), 1);
                properties.setRainTime(adjustedRainTime);
            }

            // Update the last known rain state
            lastWasRaining = currentlyRaining;
        }
    }
}
