package com.ziggybadans.harvestia.registry;

import com.ziggybadans.harvestia.world.Disease;
import net.minecraft.block.Blocks;
import org.apache.commons.lang3.Range;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DiseaseRegistry {
    private static final Map<String, Disease> diseases = new HashMap<>();

    public static void registerDisease(Disease disease) {
        diseases.put(disease.getName(), disease);
    }

    public static Disease getDisease(String name) {
        return diseases.get(name);
    }

    public static void setupRegistry() {
        registerDisease(new Disease("Blight", Set.of(Blocks.POTATOES ,Blocks.WHEAT, Blocks.BEETROOTS), Range.between(0, 15), Range.between(0.8f, 1.0f), Range.between(25.0f, 40.0f)));
    }
}
