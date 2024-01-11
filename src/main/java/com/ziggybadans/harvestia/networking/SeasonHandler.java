package com.ziggybadans.harvestia.networking;

import com.ziggybadans.harvestia.world.Season;
import com.ziggybadans.harvestia.world.SeasonData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class SeasonHandler {
    private static Season currentClientSeason = Season.SUMMER;

    public static Season getCurrentSeason(Level world) {
        if (world.isClientSide()) {
            return currentClientSeason;
        } else if (world instanceof ServerLevel) {
                return SeasonData.get((ServerLevel) world).getCurrentSeason();
        } else {
            System.out.println("Attempted to get season from an unrecognised level type");
            return currentClientSeason;
        }
    }

    public static Season getCurrentSeason() {
        //System.out.println("(Harvestia): Current season is " + currentClientSeason);
        return currentClientSeason;
    }

    public static void updateClientSeason(Season newSeason) {
        currentClientSeason = newSeason;
    }
}
