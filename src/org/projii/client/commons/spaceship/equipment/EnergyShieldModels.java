package org.projii.client.commons.spaceship.equipment;

public class EnergyShieldModels {
    public final int id;
    public final String name;
    public final int maxEnergyLevel;
    public final int regenerationSpeed;
    public final int regenerationDelay;

    public EnergyShieldModels(int id, String name, int maxEnergyLevel, int regenerationSpeed, int regenerationDelay) {
        this.id = id;
        this.name = name;
        this.maxEnergyLevel = maxEnergyLevel;
        this.regenerationSpeed = regenerationSpeed;
        this.regenerationDelay = regenerationDelay;
    }
}
