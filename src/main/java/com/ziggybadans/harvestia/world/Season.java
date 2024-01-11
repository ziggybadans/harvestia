package com.ziggybadans.harvestia.world;

public enum Season {
    SPRING("Spring", 0.9f, 1.1f, 0.9f, 0.0f, 1.0f, 1.0f),
    SUMMER("Summer", 1f, 1f, 1f, 0.5f, 0.25f, 0.5f),
    AUTUMN("Autumn", 1.2f, 0.8f, 0.4f, -0.2f, 1.0f, 1.0f),
    WINTER("Winter", 0.8f, 0.7f, 1.1f, -1.0f, 1.0f, 1.0f);

    private final String name;
    private final float redTint;
    private final float greenTint;
    private final float blueTint;
    private final float temperatureModifier;
    private final float clearWeather;
    private final float rainyWeather;

    Season(String name, float redTint, float greenTint, float blueTint, float temperatureModifier, float clearWeather, float rainyWeather) {
        this.name = name;
        this.redTint = redTint;
        this.greenTint = greenTint;
        this.blueTint = blueTint;
        this.temperatureModifier = temperatureModifier;
        this.clearWeather = clearWeather;
        this.rainyWeather = rainyWeather;
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
    public float getTemperatureModifier() {
        return temperatureModifier;
    }
    public boolean changeToSnow() {
        return temperatureModifier <= 0.15f;
    }
    public float getClearWeather() {
        return clearWeather;
    }
    public float getRainyWeather() {
        return rainyWeather;
    }
}