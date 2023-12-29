package com.ziggybadans.harvestia.world;

public class SeasonColorManager {
    private static boolean seasonColors = false;

    public static void toggleSeasonColors() {
        seasonColors = !seasonColors;
    }

    public static void setSeasonColorsEnabled(boolean seasonColorsEnabled) {
        seasonColors = seasonColorsEnabled;
    }

    public static boolean isSeasonColorsEnabled() {
        return seasonColors;
    }
}
