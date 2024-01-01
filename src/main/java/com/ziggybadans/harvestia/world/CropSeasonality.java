package com.ziggybadans.harvestia.world;

import java.util.Collections;
import java.util.EnumSet;

public class CropSeasonality {
    private EnumSet<Season> viableSeasons;

    public CropSeasonality(Season... seasons) {
        viableSeasons = EnumSet.noneOf(Season.class);
        Collections.addAll(viableSeasons, seasons);
    }

    public boolean canGrowIn(Season currentSeason) {
        return viableSeasons.contains(currentSeason);
    }
}
