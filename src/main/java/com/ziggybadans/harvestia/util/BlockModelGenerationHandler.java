package com.ziggybadans.harvestia.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ziggybadans.harvestia.block.FailingCropBlock;
import com.ziggybadans.harvestia.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class BlockModelGenerationHandler {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Block block;
    private final Map<Property<?>, Comparable<?>> properties;
    private final Identifier modelName;
    private final Path outputFolder;

    public BlockModelGenerationHandler(Block block, Map<Property<?>, Comparable<?>> properties, Identifier modelName, Path outputFolder) {
        this.block = block;
        this.properties = properties;
        this.modelName = modelName;
        this.outputFolder = outputFolder;
    }

    public void generateModel() {
        JsonObject root = new JsonObject();
        root.addProperty("parent", "minecraft:block/crop");

        JsonObject textures = new JsonObject();

        int age = (Integer) properties.get(FailingCropBlock.AGE);
        int failure = (Integer) properties.get(FailingCropBlock.FAIL_STATE);
        String textureName = "harvestia:block/corn_crop_stage_" + age + "_failure_" + failure;
        textures.addProperty("crop", textureName);

        root.add("textures", textures);

        Path modelPath = outputFolder.resolve("assets/harvestia/models/block/" + modelName.getPath() + ".json");

        try {
            Files.createDirectories(modelPath.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(modelPath)) {
                writer.write(GSON.toJson(root));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
