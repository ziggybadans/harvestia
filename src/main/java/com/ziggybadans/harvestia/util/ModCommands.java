package com.ziggybadans.harvestia.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.ziggybadans.harvestia.HarvestiaClient;
import com.ziggybadans.harvestia.world.SeasonColorManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ModCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> registerSeasonCommand(dispatcher)));
    }

    private static void registerSeasonCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("toggleSeasonColors")
                .requires(source -> source.hasPermissionLevel(2)) // Require OP level 2
                .executes(context -> {
                    SeasonColorManager.toggleSeasonColors(); // This method will toggle the season colors
                    boolean newStatus = SeasonColorManager.isSeasonColorsEnabled();
                    context.getSource().sendFeedback(() -> Text.literal("Season colors " + (newStatus ? "enabled" : "disabled")), false);

                    // Create a packet to send to clients
                    PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                    passedData.writeBoolean(newStatus);

                    // Send to all connected players
                    ServerCommandSource source = context.getSource();
                    MinecraftServer server = source.getServer();
                    if (server != null) {
                        server.getPlayerManager().getPlayerList().forEach(player -> ServerPlayNetworking.send(player, HarvestiaClient.SEASON_COLOR_TOGGLE_PACKET_ID, passedData));
                    }

                    return Command.SINGLE_SUCCESS;
                })
        );
    }
}
