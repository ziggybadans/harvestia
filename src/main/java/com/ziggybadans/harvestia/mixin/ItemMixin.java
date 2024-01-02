package com.ziggybadans.harvestia.mixin;

import com.ziggybadans.harvestia.registry.CropConditionRegistry;
import com.ziggybadans.harvestia.world.CropConditions;
import net.minecraft.block.Block;
import net.minecraft.block.CropBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "appendTooltip", at = @At("TAIL"))
    public void addCustomTooltip(ItemStack item, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
        Item self = (Item)(Object)this;

        if (self instanceof BlockItem) {
            Block block = ((BlockItem)self).getBlock();
            if (block instanceof CropBlock) {
                CropConditions cropConditions = CropConditionRegistry.getConditionsForCrop(block);
                if (cropConditions != null) {
                    tooltip.add(Text.literal("Seasonality: " + cropConditions.getSeason()));
                    tooltip.add(Text.literal("Moisture: " + cropConditions.getPreferredMoisture()));
                    tooltip.add(Text.literal("Temperature: " + cropConditions.getOptimalMinTemperature() + " to " + cropConditions.getOptimalMaxTemperature()));
                    tooltip.add(Text.literal("Light Exposure: " + cropConditions.getOptimalLightLevel()));
                    tooltip.add(Text.literal("Hardiness: " + cropConditions.getHardiness()));
                }
            }
        }
    }
}
