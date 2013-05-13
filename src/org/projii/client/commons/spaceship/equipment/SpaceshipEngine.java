package org.projii.client.commons.spaceship.equipment;

public class SpaceshipEngine {

    private final int id;
    private int maxSpeed;
    private int acceleration;
    private int maneuverability;
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
