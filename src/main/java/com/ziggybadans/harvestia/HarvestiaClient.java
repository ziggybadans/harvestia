package com.ziggybadans.harvestia;

import com.ziggybadans.harvestia.network.SeasonUpdatePacket;
import com.ziggybadans.harvestia.registry.ModBlocks;
import com.ziggybadans.harvestia.world.Season;
import com.ziggybadans.harvestia.world.SeasonState;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;

public class HarvestiaClient implements ClientModInitializer {
    private static Season currentClientSeason = Season.SPRING;

    @Override
    public void onInitializeClient() {
        //BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TOMATO_CROP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CORN_CROP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SWEET_POTATO_CROP, RenderLayer.getCutout());

        ClientPlayNetworking.registerGlobalReceiver(SeasonUpdatePacket.CHANNEL_NAME, ((client, handler, buf, responseSender) -> {
            String seasonName = buf.readString(32767);

            client.execute(() -> {
                currentClientSeason = Season.valueOf(seasonName);
                client.worldRenderer.reload();
            });
        }));
    }

    public static Season getCurrentClientSeason(MinecraftClient client) {
        if (client.world == null) {
            return Season.SPRING;
        }
        return SeasonState.get(client.getServer()).getCurrentSeason();
    }
}
