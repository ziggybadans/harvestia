package com.ziggybadans.harvestia.world;

public class SeasonTemperatureManager {
    public static float getSeasonalTemperatureModifier(Season currentSeason) {
        return switch (currentSeason) {
            case SPRING -> 0.0f; // No temperature change for spring
            case SUMMER -> 0.5f; // Warmer in summer, so a positive offset
            case AUTUMN -> -0.2f; // Slightly cooler in autumn
            case WINTER -> -1.0f; // Much cooler in winter
            default -> 0.0f; // Default no temperature change as fallback
        };
    }
}
