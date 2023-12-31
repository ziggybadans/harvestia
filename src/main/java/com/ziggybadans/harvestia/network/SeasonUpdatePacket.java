package com.ziggybadans.harvestia.network;

import com.ziggybadans.harvestia.Harvestia;
import com.ziggybadans.harvestia.util.SeasonState;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SeasonUpdatePacket {
    public static final Identifier CHANNEL_NAME = new Identifier(Harvestia.MOD_ID, "season_update");

    public static PacketByteBuf createPacket(SeasonState seasonState) {
        PacketByteBuf byteBuf = PacketByteBufs.create();
        byteBuf.writeString(seasonState.getCurrentSeason().name());
        return byteBuf;
    }
}
