package com.ziggybadans.harvestia.util;

import com.ziggybadans.harvestia.HarvestiaMod;
import com.ziggybadans.harvestia.networking.SeasonHandler;
import com.ziggybadans.harvestia.world.Season;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = HarvestiaMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SeasonColorHandler {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(SeasonColorHandler::registerBlockColorHandlers);
    }

    private static void registerBlockColorHandlers() {
        BlockColors blockColors = Minecraft.getInstance().getBlockColors();

        blockColors.register((state, world, pos, tintIndex) -> {
            Season season = SeasonHandler.getCurrentSeason();
            return getColorForSeason(season, world, pos, state);
        }, Blocks.OAK_LEAVES);
    }

    private static int getColorForSeason(Season season, BlockAndTintGetter world, BlockPos pos, BlockState state) {
        int originalColor;

        if (state.is(Blocks.OAK_LEAVES) || state.is(Blocks.BIRCH_LEAVES) || state.is(Blocks.SPRUCE_LEAVES)
        || state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.GRASS) || state.is(Blocks.TALL_GRASS) || state.is(Blocks.FERN) || state.is(Blocks.VINE)) {
            originalColor = BiomeColors.getAverageFoliageColor(world, pos);
        } else {
            originalColor = 0xFFFFFF;
        }

        int originalRed = (originalColor >> 16) & 0xFF;
        int originalGreen = (originalColor >> 8) & 0xFF;
        int originalBlue = originalColor & 0xFF;

        float redTint = season.getRedTint();
        float greenTint = season.getGreenTint();
        float blueTint = season.getBlueTint();

        int r = (int)(originalRed * redTint);
        int g = (int)(originalGreen * greenTint);
        int b = (int)(originalBlue * blueTint);

        return (0xFF000000) | (clamp(r) << 16) | (clamp(g) << 8) | clamp(b);
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
