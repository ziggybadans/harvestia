package com.ziggybadans.harvestia.networking;

import com.ziggybadans.harvestia.world.Season;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SeasonUpdatePacket {
    private Season season;

    public SeasonUpdatePacket(Season season) {
        this.season = season;
    }

    public static void encode(SeasonUpdatePacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.season.ordinal());
    }

    public static SeasonUpdatePacket decode(FriendlyByteBuf buf) {
        return new SeasonUpdatePacket(Season.values()[buf.readInt()]);
    }

    public static class Handler {
        public static void handle(final SeasonUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                mc.levelRenderer.allChanged();
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
