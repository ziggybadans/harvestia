package com.ziggybadans.harvestia;

import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.registry.Registries;
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
            int x = 100;
            int y = scaledHeight - 125;
            MutableText cropInfo = Text.empty();

            // The individual lines of text to render
            String cropDisplayName = "Crop: " + Registries.BLOCK.getId(crop).getPath().toUpperCase().charAt(0) + (Registries.BLOCK.getId(crop).getPath().substring(1));
            String testInfo = "Test";

            int lineHeight = textRenderer.fontHeight;
            int backgroundPadding = 3;

            int maxTextWidth = Math.max(textRenderer.getWidth(cropDisplayName), textRenderer.getWidth(testInfo));
            int totalTextHeight = (lineHeight + backgroundPadding) * 2;

            int backgroundColor = 0x80A0A0A0;

            // Render background box
            drawContext.fill(
                    x - backgroundPadding,
                    y - backgroundPadding,
                    x + maxTextWidth + backgroundPadding * 2,
                    y + totalTextHeight + backgroundPadding,
                    backgroundColor
            );

            // Render text line by line with shadow
            drawContext.drawTextWithShadow(textRenderer, Text.literal(cropDisplayName), x, y, 0xFFFFFF);
            y += lineHeight + backgroundPadding; // Move to next line
            drawContext.drawTextWithShadow(textRenderer, Text.literal(testInfo), x, y, 0xFFFFFF);

            crop = null;
        }
    }
}
