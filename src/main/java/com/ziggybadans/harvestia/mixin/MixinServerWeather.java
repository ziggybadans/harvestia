package com.ziggybadans.harvestia.mixin;

import com.ziggybadans.harvestia.world.Season;
import com.ziggybadans.harvestia.world.SeasonSharedManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerWorld.class)
public class MixinServerWeather {
    @Redirect(method = "tickWeather", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ServerWorldProperties;setClearWeatherTime(I)V"))
    private void modifyClearWeatherTime(ServerWorldProperties serverWorldProperties, int clearTime) {
        Season currentSeason = SeasonSharedManager.getCurrentSeason();

        if (currentSeason == Season.SUMMER) {
            if (!serverWorldProperties.isRaining()) {
                clearTime = 500;
            }
        }

        serverWorldProperties.setClearWeatherTime(clearTime);
    }
    @Redirect(method = "tickWeather", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/ServerWorldProperties;setRainTime(I)V"))
    private void modifyRainTime(ServerWorldProperties serverWorldProperties, int rainTime) {
        // Obtain the current season
        Season currentSeason = SeasonSharedManager.getCurrentSeason();

        // Adjust rain time based on season
        if (currentSeason == Season.SUMMER) {
            if (serverWorldProperties.isRaining()) {
                rainTime = 1300;
            }
        }

        serverWorldProperties.setRainTime(rainTime);
    }
}
