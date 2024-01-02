package com.ziggybadans.harvestia;

import com.ziggybadans.harvestia.registry.CropConditionRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class OverlayRenderer {
    private static Block crop;
    public static void setTarget(Block block) {
        crop = block;
    }

    public static void renderHud(DrawContext drawContext, TextRenderer textRenderer, int scaledWidth, int scaledHeight) {
        if (crop != null) {
            MinecraftClient client = MinecraftClient.getInstance();

            // Set position and size of the overlay
            int x = 10;
            int y = scaledHeight - 100;
            MutableText cropInfo = Text.empty();

            // Append lines of text for each piece of information
            cropInfo.append("Crop: " + crop.getName()).append("\n");
            cropInfo.append("Moisture: " + CropConditionRegistry.getConditionsForCrop(crop));

            // Calculate background size
            int textWidth = textRenderer.getWidth(cropInfo);
            int textHeight = textRenderer.fontHeight * 2;
            int backgroundPadding = 3;

            // Render background box
            drawContext.fill(
                    x - backgroundPadding,
                    y - backgroundPadding,
                    x + textWidth + backgroundPadding,
                    y + textHeight + backgroundPadding,
                    client.options.getTextBackgroundColor(0)
            );

            // Render text with shadow
            drawContext.drawTextWithShadow(
                    textRenderer,
                    cropInfo,
                    x,
                    y,
                    0xFFFFFF
            );

            crop = null;
        }
    }
}
