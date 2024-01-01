package com.ziggybadans.harvestia.world;

import java.util.EnumSet;
import java.util.List;

public class CropConditions {
    private final EnumSet<Season> viableSeasons;
    private final float preferredMoisture;
    private float optimalMinTemperature;
    private float optimalMaxTemperature;
    private int optimalLightLevel;

    // Default conditions if a crop hasn't been registered
    public CropConditions(EnumSet<Season> viableSeasons, float preferredMoisture, float optimalMinTemperature, float optimalMaxTemperature, int optimalLightLevel) {
        this.viableSeasons = viableSeasons;
        this.preferredMoisture = preferredMoisture;
        this.optimalMinTemperature = optimalMinTemperature;
        this.optimalMaxTemperature = optimalMaxTemperature;
        this.optimalLightLevel = optimalLightLevel;
    }

    public boolean canGrowIn(Season currentSeason) {
        return viableSeasons.contains(currentSeason);
    }

    public float getPreferredMoisture() {
        return preferredMoisture;
    }

    public float getOptimalMinTemperature() {
        return optimalMinTemperature;
    }

    public float getOptimalMaxTemperature() {
        return optimalMaxTemperature;
    }

    public int getOptimalLightLevel() {
        return optimalLightLevel;
    }

    private static final List<Season> ORDERED_SEASONS = List.of(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER);

    public int getSeasonDistance(Season currentSeason) {
        if (viableSeasons.contains(currentSeason)) {
            // The current season is viable for the crop
            return 0;
        }

        // Find the indices of the current seasons and viable seasons within the ordered list
        int currentIndex = ORDERED_SEASONS.indexOf(currentSeason);

        // Loop once through the seasons to find the closest next viable season
        // The distance starts with the maximum possible value
        int minDistance = ORDERED_SEASONS.size();
        for (Season viableSeason : viableSeasons) {
            int viableIndex = ORDERED_SEASONS.indexOf(viableSeason);

            // Calculate the distance in both directions and find the shortest
            int forwardDistance = Math.floorMod((viableIndex - currentIndex), ORDERED_SEASONS.size());
            int backwardDistance = Math.floorMod((currentIndex - viableIndex), ORDERED_SEASONS.size());
            int actualDistance = Math.min(forwardDistance, backwardDistance);

            minDistance = Math.min(minDistance, actualDistance);
        }
        return minDistance;
    }
}
