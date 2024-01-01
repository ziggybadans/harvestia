package com.ziggybadans.harvestia;

import com.ziggybadans.harvestia.network.SeasonUpdatePacket;
import com.ziggybadans.harvestia.registry.ModBlocks;
import com.ziggybadans.harvestia.world.Season;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.S2CPlayChannelEvents;
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
            //Harvestia.LOGGER.info("(HarvestiaClient) Received season update: " + seasonName);

            client.execute(() -> {
                currentClientSeason = Season.valueOf(seasonName);
                //Harvestia.LOGGER.info("(HarvestiaClient) Updating client season to: " + currentClientSeason);
                client.worldRenderer.reload();
            });
        }));

        S2CPlayChannelEvents.REGISTER.register((handler, sender, server, channels) -> {
            if (channels.contains(SeasonUpdatePacket.CHANNEL_NAME)) {
                ClientPlayNetworking.registerReceiver(SeasonUpdatePacket.CHANNEL_NAME, (client, handler1, buf, responseSender) -> {
                    String seasonName = buf.readString(32767);
                    client.execute(() -> {
                        currentClientSeason = Season.valueOf(seasonName);
                        //Harvestia.LOGGER.info("(HarvestiaClient) Updating client season to: " + currentClientSeason);
                        client.worldRenderer.reload();
                    });
                });
            }
        });
    }

    public static Season getCurrentClientSeason(MinecraftClient client) {
        //Harvestia.LOGGER.info("getCurrentClientSeason called, returning " + currentClientSeason);
        return currentClientSeason != null ? currentClientSeason : Season.SPRING;
    }
}
