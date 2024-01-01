package com.ziggybadans.harvestia.world;

import java.util.EnumSet;

public class CropConditions {
    private final EnumSet<Season> viableSeasons;
    private final float preferredMoisture;

    // Default conditions if a crop hasn't been registered
    public CropConditions(EnumSet<Season> viableSeasons, float preferredMoisture) {
        this.viableSeasons = viableSeasons;
        this.preferredMoisture = preferredMoisture;
    }

    public boolean canGrowIn(Season currentSeason) {
        return viableSeasons.contains(currentSeason);
    }

    public float getPreferredMoisture() {
        return preferredMoisture;
    }
}
