package com.ziggybadans.harvestia.util;

import com.ziggybadans.harvestia.Harvestia;
import com.ziggybadans.harvestia.network.SeasonUpdatePacket;
import com.ziggybadans.harvestia.world.Season;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.PersistentState;
import net.minecraft.world.WorldProperties;

public class SeasonState extends PersistentState {
    private Season currentSeason = Season.SPRING;
    private int daysInCurrentSeason;
    private boolean seasonPaused = false;
    public static final int SEASON_LENGTH = 7;
    private long lastDayTime = 0;

    public void tick(MinecraftServer server) {
        if (seasonPaused) {
            Harvestia.LOGGER.info("Season transitions are currently paused.");
            return;
        }
        WorldProperties properties = server.getOverworld().getLevelProperties();
        long currentTime = properties.getTimeOfDay();
        //Harvestia.LOGGER.info("Current world time: " + currentTime + ", Last day time: " + lastDayTime);

        if ((lastDayTime / 24000) < (currentTime / 24000)) {
            lastDayTime = currentTime - (currentTime % 24000);
            daysInCurrentSeason++;
            Harvestia.LOGGER.info("New day in current season: " + daysInCurrentSeason + " out of " + SEASON_LENGTH);

            if (daysInCurrentSeason >= SEASON_LENGTH) {
                Harvestia.LOGGER.info("Transitioning to next season.");
                transitionToNextSeason(server);
            }
        }
    }

    public void setSeasonPaused(boolean paused) {
        this.seasonPaused = paused;
        this.setDirty(true);
        Harvestia.LOGGER.info("Seasons paused status set to: " + paused);
    }

    public boolean isSeasonPaused() {
        return seasonPaused;
    }

    private void transitionToNextSeason(MinecraftServer server) {
        // Move to the next season, and wrap around at the end
        currentSeason = Season.values()[(currentSeason.ordinal() + 1) % Season.values().length];
        daysInCurrentSeason = 0;

        // Mark state as dirty to ensure it's saved
        setDirty(true);
        Harvestia.LOGGER.info("Transitioned to new season: " + currentSeason);

        sendSeasonUpdateToAllPlayers(server);
    }

    public int getDaysInCurrentSeason() {
        return daysInCurrentSeason;
    }

    public long getLastDayTime() {
        return lastDayTime;
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(Season currentSeason, MinecraftServer server) {
        Harvestia.LOGGER.info("Setting current season to: " + currentSeason);
        this.currentSeason = currentSeason;
        this.setDirty(true);
        sendSeasonUpdateToAllPlayers(server);
    }

    private void sendSeasonUpdateToAllPlayers(MinecraftServer server) {
        PacketByteBuf packetByteBuf = SeasonUpdatePacket.createPacket(this);
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            Harvestia.LOGGER.info("Sending season update to player: " + player.getName().getString());
            ServerPlayNetworking.send(player, SeasonUpdatePacket.CHANNEL_NAME, packetByteBuf);
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putString("currentSeason", currentSeason.name());
        nbt.putInt("daysInCurrentSeason", daysInCurrentSeason);
        nbt.putLong("lastDayTime", lastDayTime);
        nbt.putBoolean("seasonPaused", seasonPaused);
        return nbt;
    }

    public static SeasonState readNbt(NbtCompound nbt) {
        SeasonState state = new SeasonState();
        if (nbt.contains("currentSeason", 8)) {
            state.currentSeason = Season.valueOf(nbt.getString("currentSeason"));
        }
        if (nbt.contains("daysInCurrentSeason")) {
            state.daysInCurrentSeason = nbt.getInt("daysInCurrentSeason");
        }
        if (nbt.contains("lastDayTime")) {
            state.lastDayTime = nbt.getLong("lastDayTime");
        }
        if (nbt.contains("seasonPaused")) {
            state.seasonPaused = nbt.getBoolean("seasonPaused");
        }
        return state;
    }

    public static SeasonState get(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager().getOrCreate(SeasonState::readNbt, SeasonState::new, "seasonState");
    }
}
