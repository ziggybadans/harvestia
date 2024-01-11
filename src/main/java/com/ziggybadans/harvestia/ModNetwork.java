package com.ziggybadans.harvestia;

import com.ziggybadans.harvestia.networking.SeasonUpdatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(HarvestiaMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerMessages() {
        int id = 0;
        INSTANCE.registerMessage(id++,
                SeasonUpdatePacket.class,
                SeasonUpdatePacket::encode,
                SeasonUpdatePacket::decode,
                SeasonUpdatePacket.Handler::handle);
    }
}
