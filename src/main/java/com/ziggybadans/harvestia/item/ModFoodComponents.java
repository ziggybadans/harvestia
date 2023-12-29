package com.ziggybadans.harvestia.item;

import net.minecraft.item.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent TOMATO = new FoodComponent.Builder().hunger(3).saturationModifier(0.25f).build();
    public static final FoodComponent CORN = new FoodComponent.Builder().hunger(3).saturationModifier(0.5f).build();
    public static final FoodComponent SWEET_POTATO = new FoodComponent.Builder().hunger(2).saturationModifier(0.15f).build();
    public static final FoodComponent BAKED_SWEET_POTATO = new FoodComponent.Builder().hunger(6).saturationModifier(0.4f).build();
}
