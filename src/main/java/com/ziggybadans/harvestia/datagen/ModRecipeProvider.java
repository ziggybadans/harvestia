package com.ziggybadans.harvestia.datagen;

import com.ziggybadans.harvestia.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        offerShapelessRecipe(exporter, ModItems.TOMATO_SEEDS, ModItems.TOMATO, "tomato", 2);
        offerShapelessRecipe(exporter, ModItems.CORN_KERNALS, ModItems.CORN, "corn", 3);
    }
}
