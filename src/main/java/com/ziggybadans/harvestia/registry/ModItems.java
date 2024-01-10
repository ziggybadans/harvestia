package com.ziggybadans.harvestia.registry;

import com.ziggybadans.harvestia.HarvestiaMod;
import com.ziggybadans.harvestia.item.ScytheItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, HarvestiaMod.MOD_ID);

    public static final RegistryObject<Item> CORN = ITEMS.register("corn",
            () -> new Item(new Item.Properties().food(ModFoods.CORN)));
    public static final RegistryObject<Item> SWEET_POTATO = ITEMS.register("sweet_potato",
            () -> new Item(new Item.Properties().food(ModFoods.SWEET_POTATO)));
    public static final RegistryObject<Item> BAKED_SWEET_POTATO = ITEMS.register("baked_sweet_potato",
            () -> new Item(new Item.Properties().food(ModFoods.BAKED_SWEET_POTATO)));

    public static final RegistryObject<Item> WOODEN_SCYTHE = ITEMS.register("wooden_scythe",
            () -> new ScytheItem(Tiers.WOOD, new Item.Properties()));
    public static final RegistryObject<Item> STONE_SCYTHE = ITEMS.register("stone_scythe",
            () -> new ScytheItem(Tiers.STONE, new Item.Properties()));
    public static final RegistryObject<Item> IRON_SCYTHE = ITEMS.register("iron_scythe",
            () -> new ScytheItem(Tiers.IRON, new Item.Properties()));
    public static final RegistryObject<Item> GOLDEN_SCYTHE = ITEMS.register("golden_scythe",
            () -> new ScytheItem(Tiers.GOLD, new Item.Properties()));
    public static final RegistryObject<Item> DIAMOND_SCYTHE = ITEMS.register("diamond_scythe",
            () -> new ScytheItem(Tiers.DIAMOND, new Item.Properties()));
    public static final RegistryObject<Item> NETHERITE_SCYTHE = ITEMS.register("netherite_scythe",
            () -> new ScytheItem(Tiers.NETHERITE, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
