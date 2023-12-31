package com.ziggybadans.harvestia;

import com.ziggybadans.harvestia.registry.ModBlocks;
import com.ziggybadans.harvestia.registry.ModItems;
import com.ziggybadans.harvestia.util.ModCommands;
import com.ziggybadans.harvestia.util.ScytheBlockBreakHandler;
import com.ziggybadans.harvestia.util.SeasonState;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Harvestia implements ModInitializer {
	public static final String MOD_ID = "harvestia";
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("Harvestia");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Setting up Harvestia...");

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();

		ModCommands.register();

		ScytheBlockBreakHandler.register();

		ServerWorldEvents.LOAD.register((MinecraftServer server, ServerWorld world) -> {
			SeasonState.get(server);
		});
		ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarting);
		ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStopping);
	}

	private void onServerStarting(MinecraftServer server) {
		SeasonState.get(server);
	}

	private void onServerStopping(MinecraftServer server) {
		SeasonState.get(server).markDirty();
		server.getOverworld().getPersistentStateManager().save();
	}
}