package com.ziggybadans.harvestia.item;

import net.minecraft.item.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent TOMATO = new FoodComponent.Builder().hunger(3).saturationModifier(0.25f).build();
    public static final FoodComponent CORN = new FoodComponent.Builder().hunger(3).saturationModifier(0.5f).build();
}
