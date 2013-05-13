package org.projii.client.commons.spaceship.equipment;

public class EnergyGeneratorModel {
    public final int id;
    public final String name;
    public final int maxEnergyLevel;
    public final int regenerationSpeed;

    public EnergyGeneratorModel(int id, String name, int maxEnergyLevel, int regenerationSpeed) {
        this.id = id;
        this.name = name;
        this.maxEnergyLevel = maxEnergyLevel;
        this.regenerationSpeed = regenerationSpeed;
    }
}

