package com.ziggybadans.harvestia.world;

import java.util.EnumSet;

public class CropSeasonality {
    private EnumSet<Season> viableSeasons;

    public CropSeasonality(Season... seasons) {
        viableSeasons = EnumSet.noneOf(Season.class);
        for (Season season : seasons) {
            viableSeasons.add(season);
        }
    }

    public boolean canGrowIn(Season currentSeason) {
        return viableSeasons.contains(currentSeason);
    }
}
