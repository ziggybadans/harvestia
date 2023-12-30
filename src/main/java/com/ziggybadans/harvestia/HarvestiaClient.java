package com.ziggybadans.harvestia;

import com.ziggybadans.harvestia.registry.ModBlocks;
import com.ziggybadans.harvestia.world.Season;
import com.ziggybadans.harvestia.world.SeasonSharedManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class HarvestiaClient implements ClientModInitializer {
    public static final Identifier SEASON_COLOR_TOGGLE_PACKET_ID = new Identifier(Harvestia.MOD_ID, "season_change");
    @Override
    public void onInitializeClient() {
        //BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TOMATO_CROP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.CORN_CROP, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SWEET_POTATO_CROP, RenderLayer.getCutout());

        // Register the packet
        ClientPlayNetworking.registerGlobalReceiver(SEASON_COLOR_TOGGLE_PACKET_ID, ((client, handler, buf, responseSender) -> {
            // Read the data from the packet
            int seasonOrdinal = buf.readInt();
            Season season = Season.values()[seasonOrdinal];

            // Execute on the main client thread
            client.execute(() -> {
                SeasonSharedManager.setCurrentSeason(season);
                client.worldRenderer.reload();
            });
        }));
    }
}
