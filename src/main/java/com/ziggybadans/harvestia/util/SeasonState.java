package com.ziggybadans.harvestia.util;

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
    private static final int SEASON_LENGTH = 7;
    private long lastDayTime = 0;

    public void tick(MinecraftServer server) {
        WorldProperties properties = server.getOverworld().getLevelProperties();
        long currentTime = properties.getTimeOfDay();

        if (currentTime >= lastDayTime + 24000 && currentTime < lastDayTime + 24000 * 2) {
            lastDayTime = currentTime - (currentTime % 24000);
            daysInCurrentSeason++;

            if (daysInCurrentSeason >= SEASON_LENGTH) {
                transitionToNextSeason(server);
            }
        }
    }

    private void transitionToNextSeason(MinecraftServer server) {
        // Move to the next season, and wrap around at the end
        currentSeason = Season.values()[(currentSeason.ordinal() + 1) % Season.values().length];
        daysInCurrentSeason = 0;

        // Mark state as dirty to ensure it's saved
        setDirty(true);

        sendSeasonUpdateToAllPlayers(server);
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(Season currentSeason, MinecraftServer server) {
        this.currentSeason = currentSeason;
        this.setDirty(true);
        sendSeasonUpdateToAllPlayers(server);
    }

    private void sendSeasonUpdateToAllPlayers(MinecraftServer server) {
        PacketByteBuf packetByteBuf = SeasonUpdatePacket.createPacket(this);
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            ServerPlayNetworking.send(player, SeasonUpdatePacket.CHANNEL_NAME, packetByteBuf);
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putString("currentSeason", currentSeason.name());
        nbt.putInt("daysInCurrentSeason", daysInCurrentSeason);
        nbt.putLong("lastDayTime", lastDayTime);
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
        return state;
    }

    public static SeasonState get(MinecraftServer server) {
        return server.getOverworld().getPersistentStateManager().getOrCreate(SeasonState::readNbt, SeasonState::new, "seasonState");
    }
}
