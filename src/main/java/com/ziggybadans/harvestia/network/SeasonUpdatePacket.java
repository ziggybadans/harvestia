package com.ziggybadans.harvestia.network;

import com.ziggybadans.harvestia.Harvestia;
import com.ziggybadans.harvestia.world.SeasonState;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class SeasonUpdatePacket {
    public static final Identifier CHANNEL_NAME = new Identifier(Harvestia.MOD_ID, "season_update");

    public static PacketByteBuf createPacket(SeasonState seasonState) {
        //Harvestia.LOGGER.info("(SeasonUpdatePacket) Creating packet...");
        PacketByteBuf byteBuf = PacketByteBufs.create();
        byteBuf.writeString(seasonState.getCurrentSeason().name());
        //Harvestia.LOGGER.info("(SeasonUpdatePacket) Packet string is: " + seasonState.getCurrentSeason().name());
        return byteBuf;
    }
}
