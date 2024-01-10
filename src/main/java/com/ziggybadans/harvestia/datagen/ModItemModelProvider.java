package com.ziggybadans.harvestia.datagen;

import com.ziggybadans.harvestia.HarvestiaMod;
import com.ziggybadans.harvestia.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HarvestiaMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.CORN);

        handheldItem(ModItems.WOODEN_SCYTHE);
        handheldItem(ModItems.STONE_SCYTHE);
        handheldItem(ModItems.IRON_SCYTHE);
        handheldItem(ModItems.GOLDEN_SCYTHE);
        handheldItem(ModItems.DIAMOND_SCYTHE);
        handheldItem(ModItems.NETHERITE_SCYTHE);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(HarvestiaMod.MOD_ID, "item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(HarvestiaMod.MOD_ID, "item/" + item.getId().getPath()));
    }
}
