package com.ziggybadans.harvestia.world;

public enum Season {
    SPRING("Spring"),
    SUMMER("Summer"),
    AUTUMN("Autumn"),
    WINTER("Winter");

    private final String name;

    Season(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
