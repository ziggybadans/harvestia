package com.ziggybadans.harvestia;

import com.mojang.logging.LogUtils;
import com.ziggybadans.harvestia.registry.ModBlocks;
import com.ziggybadans.harvestia.registry.ModItems;
import com.ziggybadans.harvestia.util.SeasonEventHandler;
import com.ziggybadans.harvestia.world.SeasonalBlockColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.Logging;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.logging.LogManager;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(HarvestiaMod.MOD_ID)
public class HarvestiaMod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "harvestia";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public HarvestiaMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        LOGGER.info("Registering items");
        ModBlocks.register(modEventBus);
        LOGGER.info("Registering blocks");

        // Register the commonSetup method for mod-loading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(SeasonEventHandler.class);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        LOGGER.info("Adding items to creative tabs");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModNetwork.registerMessages();
        LOGGER.info("Registering networking tools");
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModItems.CORN);
            event.accept(ModItems.SWEET_POTATO);
            event.accept(ModItems.BAKED_SWEET_POTATO);
        } else if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
           event.accept(ModItems.WOODEN_SCYTHE);
           event.accept(ModItems.STONE_SCYTHE);
           event.accept(ModItems.IRON_SCYTHE);
           event.accept(ModItems.GOLDEN_SCYTHE);
           event.accept(ModItems.DIAMOND_SCYTHE);
           event.accept(ModItems.NETHERITE_SCYTHE);
        } else if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModBlocks.ROCKY_STONE);
            event.accept(ModItems.CORN_KERNALS);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            Minecraft minecraft = Minecraft.getInstance();
            BlockColors blockColors = minecraft.getBlockColors();

            SeasonalBlockColor seasonalBlockColor = new SeasonalBlockColor();

            Block[] affectedBlocks = new Block[] {
                    Blocks.GRASS_BLOCK,
                    Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.BIRCH_LEAVES,
                    Blocks.GRASS, Blocks.TALL_GRASS, Blocks.FERN, Blocks.VINE
            };

            for (Block block : affectedBlocks) {
                blockColors.register(seasonalBlockColor, block);
            }
        }
    }
}
