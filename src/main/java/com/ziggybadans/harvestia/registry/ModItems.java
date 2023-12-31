package com.ziggybadans.harvestia.registry;

import com.ziggybadans.harvestia.Harvestia;
import com.ziggybadans.harvestia.item.ModFoodComponents;
import com.ziggybadans.harvestia.item.ScytheItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item CORN_KERNALS = registerItem("corn_kernals",
            new AliasedBlockItem(ModBlocks.CORN_CROP, new FabricItemSettings()));
    public static final Item CORN = registerItem("corn",
            new Item(new FabricItemSettings().food(ModFoodComponents.CORN)));
    public static final Item SWEET_POTATO = registerItem("sweet_potato",
            new AliasedBlockItem(ModBlocks.SWEET_POTATO_CROP, new FabricItemSettings().food(ModFoodComponents.SWEET_POTATO)));
    public static final Item BAKED_SWEET_POTATO = registerItem("baked_sweet_potato",
            new Item(new FabricItemSettings().food(ModFoodComponents.BAKED_SWEET_POTATO)));

    public static final Item WOOD_SCYTHE = registerItem("wood_scythe",
            new ScytheItem(ToolMaterials.WOOD, new FabricItemSettings()));
    public static final Item STONE_SCYTHE = registerItem("stone_scythe",
            new ScytheItem(ToolMaterials.STONE, new FabricItemSettings()));
    public static final Item IRON_SCYTHE = registerItem("iron_scythe",
            new ScytheItem(ToolMaterials.IRON, new FabricItemSettings()));
        public static final Item GOLD_SCYTHE = registerItem("gold_scythe",
            new ScytheItem(ToolMaterials.GOLD, new FabricItemSettings()));
    public static final Item DIAMOND_SCYTHE = registerItem("diamond_scythe",
            new ScytheItem(ToolMaterials.DIAMOND, new FabricItemSettings()));
    public static final Item NETHERITE_SCYTHE = registerItem("netherite_scythe",
            new ScytheItem(ToolMaterials.NETHERITE, new FabricItemSettings()));

    private static void addItemsToNaturalItemGroup(FabricItemGroupEntries entries) {
        entries.add(CORN_KERNALS);
    }
    private static void addItemsToFoodItemGroup(FabricItemGroupEntries entries) {
        entries.add(CORN);
        entries.add(SWEET_POTATO);
        entries.add(BAKED_SWEET_POTATO);
    }

    private static void addItemsToToolItemGroup(FabricItemGroupEntries entries) {
        entries.add(WOOD_SCYTHE);
        entries.add(STONE_SCYTHE);
        entries.add(IRON_SCYTHE);
        entries.add(GOLD_SCYTHE);
        entries.add(DIAMOND_SCYTHE);
        entries.add(NETHERITE_SCYTHE);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Harvestia.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Harvestia.LOGGER.info("Registering Mod Items for " + Harvestia.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(ModItems::addItemsToNaturalItemGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(ModItems::addItemsToFoodItemGroup);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ModItems::addItemsToToolItemGroup);
    }
}
