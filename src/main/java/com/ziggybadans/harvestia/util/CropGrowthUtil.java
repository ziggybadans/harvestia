package com.ziggybadans.harvestia.util;

import com.ziggybadans.harvestia.world.CropConditions;
import com.ziggybadans.harvestia.world.Season;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public final class CropGrowthUtil {

    // Private constructor to prevent instantiation
    private CropGrowthUtil() {}

    // Static method for calculating moisture level
    public static float calculateMoistureLevel(ServerWorld world, BlockPos pos) {
        final int maxWaterDistance = 4; // Vanilla water limit for farmland hydration
        float moistureValue = 0.0f;

        for (int x = -maxWaterDistance; x <= maxWaterDistance; x++) {
            for (int z = -maxWaterDistance; z <= maxWaterDistance; z++) {
                BlockPos waterCheckPos = pos.add(x, 0, z);
                if (world.getBlockState(waterCheckPos).getBlock() == Blocks.WATER) {
                    int distance = Math.abs(x) + Math.abs(z);
                    float moistureForThisBlock = 1.0f - (float)distance / maxWaterDistance;
                    moistureValue = Math.max(moistureValue, moistureForThisBlock);
                }
            }
        }
        return moistureValue;
    }

    public static float calculateSeasonGrowthChance(CropConditions cropConditions, Season currentSeason) {
        // Get the distance from the preferred season(s)
        int seasonDistance = cropConditions.getSeasonDistance(currentSeason);

        // Convert this distance to an exponential chance (decay rate can be adjusted as needed)
        double decayRate = 2.0; // The base of the exponential decay, can be tweaked for balance
        return (float) Math.pow(decayRate, -seasonDistance);
    }

    public static float getMoistureGrowthChance(CropConditions cropConditions, float moistureLevel) {
        float preferredMoisture = cropConditions.getPreferredMoisture();
        float moistureDifference = Math.abs(preferredMoisture - moistureLevel);

        // Linear stifling of growth for moisture, as before
        return Math.max(0, 1.0f - moistureDifference);
    }

    public static float getBiomeTemperature(float biomeTemperature) {
        // Map the [0, 1] range to [-30, 50] degrees Celsius
        return -30.0f + (biomeTemperature * 80.0f);
    }

    public static float getTemperatureGrowthModifier(CropConditions cropConditions, float temperature) {
        // Define the optimal temperature range for the crop
        float optimalMinTemp = cropConditions.getOptimalMinTemperature();
        float optimalMaxTemp = cropConditions.getOptimalMaxTemperature();

        // If the current temperature is within the optimal range, the modifier is 1 (no effect on growth)
        if (temperature >= optimalMinTemp && temperature <= optimalMaxTemp) {
            return 1.0f;
        }

        // If the temperature is outside the range, compute how far it is from the range
        float temperatureDifference = Math.max(optimalMinTemp - temperature, temperature - optimalMaxTemp);

        // Compute the temperature growth modifier here, possibly with some formula based on temperatureDifference
        // For example, we could apply an exponential decay similar to how seasonality was handled
        double decayRate = 2.0; // Adjust as needed for game balance

        // Convert the difference to an exponential decay modifier
        return (float) Math.pow(decayRate, -temperatureDifference);
    }
}
