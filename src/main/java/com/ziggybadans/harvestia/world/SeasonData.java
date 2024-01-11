package com.ziggybadans.harvestia.world;

import com.ziggybadans.harvestia.ModNetwork;
import com.ziggybadans.harvestia.networking.SeasonUpdatePacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.network.PacketDistributor;

public class SeasonData extends SavedData {
    private static final String DATA_NAME = "season_data";
    private Season currentSeason;
    private int daysElapsed;
    public int SEASON_LENGTH = 14;

    public SeasonData() {
        this.currentSeason = Season.SPRING;
        this.daysElapsed = 0;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putInt("daysElapsed", daysElapsed);
        compoundTag.putString("currentSeason", currentSeason.getName());
        return compoundTag;
    }

    public static SeasonData load(CompoundTag compoundTag) {
        SeasonData data = new SeasonData();
        if (compoundTag.contains("currentSeason", 8)) {
            data.currentSeason = Season.valueOf(compoundTag.getString("currentSeason"));
        }
        data.daysElapsed = compoundTag.getInt("daysElapsed");
        return data;
    }

    public static SeasonData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(SeasonData::load, SeasonData::new, DATA_NAME);
    }

    public void incrementDay() {
        daysElapsed++;
        boolean seasonChanged = false;
        if (daysElapsed >= SEASON_LENGTH) {
            daysElapsed = 0;
            currentSeason = Season.values()[(currentSeason.ordinal() + 1) % Season.values().length];
            setDirty();
            seasonChanged = true;
        }
        if (seasonChanged) {
            ModNetwork.INSTANCE.send(PacketDistributor.ALL.noArg(), new SeasonUpdatePacket(currentSeason));
        }
    }

    public void setCurrentSeason(Season newSeason) {
        currentSeason = newSeason;
        setDirty();
        ModNetwork.INSTANCE.send(PacketDistributor.ALL.noArg(), new SeasonUpdatePacket(currentSeason));
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }
    public int getDaysElapsed() {
        return daysElapsed;
    }
}
