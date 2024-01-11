package com.ziggybadans.harvestia.world;

import com.ziggybadans.harvestia.networking.SeasonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class SeasonalBlockColor implements BlockColor {
    private final float factor = 0.5f;
    @Override
    public int getColor(BlockState blockState, @Nullable BlockAndTintGetter blockAndTintGetter, @Nullable BlockPos blockPos, int i) {
        Season currentSeason = SeasonHandler.getCurrentSeason();

        int originalColor = Minecraft.getInstance().getBlockColors().getColor(blockState, blockAndTintGetter, blockPos, i);
        int originalRed = (originalColor >> 16) & 0xFF;
        int originalGreen = (originalColor >> 8) & 0xFF;
        int originalBlue = originalColor & 0xFF;

        int newRed = (int) (originalRed * (1 - factor) + 255 * currentSeason.getRedTint() * factor);
        int newGreen = (int) (originalGreen * (1 - factor) + 255 * currentSeason.getGreenTint() * factor);
        int newBlue = (int) (originalBlue * (1 - factor) + 255 * currentSeason.getBlueTint() * factor);

        return (originalColor & 0xFF000000) | (newRed << 16) | (newGreen << 8) | newBlue;
    }
}
