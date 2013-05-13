package org.projii.client.commons.spaceship.equipment;

import org.jai.BSON.BSONSerializable;

public class EnergyGeneratorModel {
	@BSONSerializable
    public final int id;
	@BSONSerializable
    public final String name;
	@BSONSerializable
    public final int maxEnergyLevel;
	@BSONSerializable
    public final int regenerationSpeed;

    public EnergyGeneratorModel(int id, String name, int maxEnergyLevel, int regenerationSpeed) {
        this.id = id;
        this.name = name;
        this.maxEnergyLevel = maxEnergyLevel;
        this.regenerationSpeed = regenerationSpeed;
    }
}

