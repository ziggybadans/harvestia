package com.ziggybadans.harvestia.world;

public enum Season {
    SPRING("Spring", 0.9f, 1.1f, 0.9f),
    SUMMER("Summer", 1f, 1f, 1f),
    AUTUMN("Autumn", 1.2f, 0.8f, 0.4f),
    WINTER("Winter", 0.8f, 0.7f, 1.1f);

    private final String name;
    private final float redTint;
    private final float greenTint;
    private final float blueTint;

    Season(String name, float redTint, float greenTint, float blueTint) {
        this.name = name;
        this.redTint = redTint;
        this.greenTint = greenTint;
        this.blueTint = blueTint;
    }

    public String getName() {
        return name;
    }
    public float getRedTint() {
        return redTint;
    }
    public float getGreenTint() {
        return greenTint;
    }
    public float getBlueTint() {
        return blueTint;
    }
}
