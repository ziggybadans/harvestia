package com.ziggybadans.harvestia.mixin;

import com.ziggybadans.harvestia.HarvestiaClient;
import com.ziggybadans.harvestia.world.Season;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public class ServerWeatherMixin {
    @Unique
    private boolean lastWasRaining = false;

    @Inject(method = "tickWeather", at = @At("HEAD"))
    private void onTickWeatherStart(CallbackInfo ci) {
        ServerWorld self = (ServerWorld)(Object)this;
        ServerWorldProperties properties = (ServerWorldProperties) self.getLevelProperties();

        boolean currentlyRaining = self.isRaining();

        if (lastWasRaining != currentlyRaining) {
            MinecraftClient client = MinecraftClient.getInstance();
            Season currentSeason = HarvestiaClient.getCurrentClientSeason(client);

            float clearWeatherMultiplier = 1.0f;
            float rainMultiplier = 1.0f;

            switch (currentSeason) {
                case SUMMER:
                    clearWeatherMultiplier = 0.25f;
                    rainMultiplier = 0.5f;
                    break;
            }

            if (!currentlyRaining && lastWasRaining) {
                int baseClearTime = properties.getClearWeatherTime();
                int adjustedClearTime = Math.max((int)(baseClearTime * clearWeatherMultiplier), 1);
                properties.setClearWeatherTime(adjustedClearTime);
            } else if (currentlyRaining && !lastWasRaining) {
                int baseRainTime = properties.getRainTime();
                int adjustedRainTime = Math.max((int)(baseRainTime * rainMultiplier), 1);
                properties.setRainTime(adjustedRainTime);
            }

            lastWasRaining = currentlyRaining;
        }
    }
}
