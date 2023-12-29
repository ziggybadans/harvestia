package com.ziggybadans.harvestia.registry;

import com.ziggybadans.harvestia.Harvestia;
import com.ziggybadans.harvestia.item.ModFoodComponents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    //public static final Item TOMATO_SEEDS = registerItem("tomato_seeds",
            //new AliasedBlockItem(ModBlocks.TOMATO_CROP, new FabricItemSettings()));
    //public static final Item TOMATO = registerItem("tomato",
        //new Item(new FabricItemSettings().food(ModFoodComponents.TOMATO)));
    public static final Item CORN_KERNALS = registerItem("corn_kernals",
            new AliasedBlockItem(ModBlocks.CORN_CROP, new FabricItemSettings()));
    public static final Item CORN = registerItem("corn",
            new Item(new FabricItemSettings().food(ModFoodComponents.CORN)));
    public static final Item SWEET_POTATO = registerItem("sweet_potato",
            new AliasedBlockItem(ModBlocks.SWEET_POTATO_CROP, new FabricItemSettings().food(ModFoodComponents.SWEET_POTATO)));
    public static final Item BAKED_SWEET_POTATO = registerItem("baked_sweet_potato",
            new Item(new FabricItemSettings().food(ModFoodComponents.BAKED_SWEET_POTATO)));

    private static void addItemsToNaturalItemGroup(FabricItemGroupEntries entries) {
        //entries.add(TOMATO_SEEDS);
        entries.add(CORN_KERNALS);
    }
    private static void addItemsToFoodItemGroup(FabricItemGroupEntries entries) {
        //entries.add(TOMATO);
        entries.add(CORN);
        entries.add(SWEET_POTATO);
        entries.add(BAKED_SWEET_POTATO);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Harvestia.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Harvestia.LOGGER.info("Registering Mod Items for " + Harvestia.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(ModItems::addItemsToNaturalItemGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(ModItems::addItemsToFoodItemGroup);
    }
}
