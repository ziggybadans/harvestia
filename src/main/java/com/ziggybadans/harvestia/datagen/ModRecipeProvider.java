package com.ziggybadans.harvestia.datagen;

import com.ziggybadans.harvestia.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.ItemConvertible;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    private static final List<ItemConvertible> POTATO_SMELTABLES = List.of(ModItems.SWEET_POTATO);
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        //offerShapelessRecipe(exporter, ModItems.TOMATO_SEEDS, ModItems.TOMATO, "tomato", 2);
        offerShapelessRecipe(exporter, ModItems.CORN_KERNALS, ModItems.CORN, "corn", 3);

        offerSmelting(exporter, POTATO_SMELTABLES, RecipeCategory.FOOD, ModItems.BAKED_SWEET_POTATO,
                0.35f, 200, "potato");
        CookingRecipeJsonBuilder.createCampfireCooking(Ingredient.ofItems(ModItems.SWEET_POTATO), RecipeCategory.FOOD, ModItems.BAKED_SWEET_POTATO,
                0.35f, 600);
        CookingRecipeJsonBuilder.createSmoking(Ingredient.ofItems(ModItems.SWEET_POTATO), RecipeCategory.FOOD, ModItems.BAKED_SWEET_POTATO,
                0.35f, 100);
    }
}
