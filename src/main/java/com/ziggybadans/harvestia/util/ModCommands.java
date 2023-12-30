package com.ziggybadans.harvestia.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.ziggybadans.harvestia.HarvestiaClient;
import com.ziggybadans.harvestia.world.Season;
import com.ziggybadans.harvestia.world.SeasonSharedManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.level.ServerWorldProperties;

public class ModCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> registerSeasonCommand(dispatcher)));
    }

    private static void registerSeasonCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("setseason")
                .requires(source -> source.hasPermissionLevel(2)) // Require OP level 2
                .then(CommandManager.argument("season", StringArgumentType.word())
                        .suggests((context, builder) -> {
                            for (Season season : Season.values()) {
                                builder.suggest(season.name().toLowerCase());
                            }
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            String seasonName = StringArgumentType.getString(context, "season");
                            Season season = SeasonSharedManager.setSeasonFromString(seasonName);

                            if (season != null) {
                                context.getSource().sendFeedback(() -> Text.literal("Season set to " + seasonName), false);

                                // Create a packet to send to all connected clients
                                PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                                passedData.writeInt(season.ordinal());

                                // Send to all connected players
                                ServerCommandSource source = context.getSource();
                                MinecraftServer server = source.getServer();
                                if (server != null) {
                                    server.getPlayerManager().getPlayerList().forEach(player -> ServerPlayNetworking.send(player, HarvestiaClient.SEASON_COLOR_TOGGLE_PACKET_ID, passedData));
                                }

                                return Command.SINGLE_SUCCESS;
                            } else {
                                context.getSource().sendError(Text.literal("Invalid season: \"" + seasonName + "\""));
                                return 0;
                            }
                        }))
        );

        dispatcher.register(CommandManager.literal("getseason")
                .executes(context -> {
                    Season currentSeason = SeasonSharedManager.getCurrentSeason();
                    context.getSource().sendFeedback(() -> Text.literal("Current season is " + currentSeason.name()), false);
                    return Command.SINGLE_SUCCESS;
                }));

        dispatcher.register(CommandManager.literal("getraintime")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ServerWorld world = source.getWorld();

                    // Obtain the ServerWorldProperties interface and get the rain time property
                    ServerWorldProperties worldProperties = (ServerWorldProperties) world.getLevelProperties();
                    int rainTime = worldProperties.getRainTime();

                    // Send feedback with the rain time to the player
                    source.sendFeedback(() -> Text.literal("Current rain time " + rainTime + " ticks"), false);
                    return Command.SINGLE_SUCCESS;
                }));
        dispatcher.register(CommandManager.literal("getcleartime")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ServerWorld world = source.getWorld();

                    ServerWorldProperties worldProperties = (ServerWorldProperties) world.getLevelProperties();
                    int clearTime = worldProperties.getClearWeatherTime();

                    source.sendFeedback(() -> Text.literal("Current clear time " + clearTime + " ticks"), false);
                    return Command.SINGLE_SUCCESS;
                }));
    }
}