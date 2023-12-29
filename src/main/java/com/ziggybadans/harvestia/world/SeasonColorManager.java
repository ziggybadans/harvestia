package com.ziggybadans.harvestia.world;

import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

public class SeasonColorManager {

    public static int getSeasonalColor(BlockRenderView world, BlockPos pos) {
        // Get the default biome color
        int biomeColor = BiomeColors.getFoliageColor(world, pos);

        // Calculate the color tint based on the current season
        float[] seasonTint = getColorTintForSeason(SeasonSharedManager.getCurrentSeason());

        // Apply the tint to the biome color
        return applyColorTint(biomeColor, seasonTint);
    }

    private static float[] getColorTintForSeason(Season season) {
        return switch (season) {
            case SPRING -> new float[]{0.9f, 1.1f, 0.9f}; // Slightly green tint for spring
            case SUMMER -> new float[]{1f, 1f, 1f}; // No tint for summer
            case AUTUMN -> new float[]{1.2f, 0.8f, 0.4f}; // Orange/brown tint for autumn
            case WINTER -> new float[]{0.8f, 0.9f, 1.1f}; // Slightly blue/cold tint for winter
            default -> new float[]{1f, 1f, 1f}; // Default as a fallback
        };
    }

    private static int applyColorTint(int color, float[] tint) {
        int r = Math.min(255, (int) ((color >> 16 & 255) * tint[0]));
        int g = Math.min(255, (int) ((color >> 8 & 255) * tint[1]));
        int b = Math.min(255, (int) ((color & 255) * tint[2]));
        return (r << 16) + (g << 8) + b;
    }
}
