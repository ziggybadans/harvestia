package com.ziggybadans.harvestia.world;

import net.minecraft.block.Block;
import org.apache.commons.lang3.Range;

import java.util.Set;

public class Disease {
    private final String name;
    private final Set<Block> affectedCrops;
    private final Range<Integer> favoredLightLevels;
    private final Range<Float> favoredMoistureLevels;
    private final Range<Float> favoredTemperatureRange;

    public Disease(String name, Set<Block> affectedCrops, Range<Integer> favoredLightLevels, Range<Float> favoredMoistureLevels, Range<Float> favoredTemperatureRange) {
        this.name = name;
        this.affectedCrops = affectedCrops;
        this.favoredLightLevels = favoredLightLevels;
        this.favoredMoistureLevels = favoredMoistureLevels;
        this.favoredTemperatureRange = favoredTemperatureRange;
    }

    public String getName() {
        return name;
    }

    public boolean canInfect(Block crop, int lightLevel, float moistureLevel, float temperature) {
        boolean canAffectCrop = affectedCrops.contains(crop) || affectedCrops.isEmpty();
        boolean inFavoredLightLevel = favoredLightLevels.contains(lightLevel);
        boolean inFavoredMoistureLevel = favoredMoistureLevels.contains(moistureLevel);
        boolean inFavoredTemperatureRange = favoredTemperatureRange.contains(temperature);

        return canAffectCrop && inFavoredLightLevel && inFavoredMoistureLevel && inFavoredTemperatureRange;
    }
}
