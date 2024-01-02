package com.ziggybadans.harvestia;

import com.ziggybadans.harvestia.network.SeasonUpdatePacket;
import com.ziggybadans.harvestia.registry.ModBlocks;
import com.ziggybadans.harvestia.world.Season;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.networking.v1.S2CPlayChannelEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

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

        HudRenderCallback.EVENT.register(((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            PlayerEntity player = client.player;

            if (player != null && client.currentScreen == null) {
                // Check if the player is holding a hoe
                ItemStack itemStack = player.getMainHandStack();
                if (itemStack.getItem() instanceof HoeItem) {
                    // Check if the player is looking at a CropBlock
                    HitResult hitResult = client.crosshairTarget;
                    if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
                        BlockHitResult blockHitResult = (BlockHitResult)hitResult;
                        BlockPos blockPos = blockHitResult.getBlockPos();
                        BlockState blockState = player.getWorld().getBlockState(blockPos);
                        if (blockState.getBlock() instanceof CropBlock) {
                            // Set the current crop data
                            OverlayRenderer.setTarget(blockState.getBlock());

                            // Render the HUD overlay
                            TextRenderer textRenderer = client.textRenderer;
                            OverlayRenderer.renderHud(drawContext, textRenderer, drawContext.getScaledWindowWidth(), drawContext.getScaledWindowHeight());
                        }
                    }
                }
            }
        }));
    }

    public static Season getCurrentClientSeason(MinecraftClient client) {
        //Harvestia.LOGGER.info("getCurrentClientSeason called, returning " + currentClientSeason);
        return currentClientSeason != null ? currentClientSeason : Season.SPRING;
    }
}
