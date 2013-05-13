package org.projii.client.commons.spaceship.equipment;

import org.jai.BSON.BSONSerializable;

public class SpaceshipEngine {
	@BSONSerializable
    private final int id;
	@BSONSerializable
    private int maxSpeed;
	@BSONSerializable
    private int acceleration;
	@BSONSerializable
    private int maneuverability;
	@BSONSerializable
    private String name;


    public int getId() {
        return id;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public int getManeuverability() {
        return maneuverability;
    }

    public SpaceshipEngine(int id, int maxSpeed, int acceleration, int maneuverability, String name) {
        this.id = id;
        this.maxSpeed = maxSpeed;
        this.acceleration = acceleration;
        this.maneuverability = maneuverability;
        this.name = name;
    }
}
