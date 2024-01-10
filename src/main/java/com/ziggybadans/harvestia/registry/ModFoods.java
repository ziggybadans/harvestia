package com.ziggybadans.harvestia.registry;

import net.minecraft.world.food.FoodProperties;

public class ModFoods {
    public static final FoodProperties CORN = new FoodProperties.Builder().nutrition(3).saturationMod(0.5f).build();
    public static final FoodProperties SWEET_POTATO = new FoodProperties.Builder().nutrition(2).saturationMod(0.15f).build();
    public static final FoodProperties BAKED_SWEET_POTATO = new FoodProperties.Builder().nutrition(6).saturationMod(0.4f).build();
}
