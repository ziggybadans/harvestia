package com.ziggybadans.harvestia.world;

public class SeasonSharedManager {
    private static Season currentSeason = Season.SPRING;

    public static void setCurrentSeason(Season season) {
        currentSeason = season;
    }

    public static Season getCurrentSeason() {
        return currentSeason;
    }

    public static Season setSeasonFromString(String seasonName) {
        try {
            Season parsedSeason = Season.valueOf(seasonName.toUpperCase());
            setCurrentSeason(parsedSeason);
            return parsedSeason;
        } catch (IllegalArgumentException e) {
            return null; // Invalid season string
        }
    }
}
