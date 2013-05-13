package org.projii.client.commons.spaceship.weapon;

import org.jai.BSON.BSONSerializable;

public class WeaponModel {
	@BSONSerializable
    private final int id;
	@BSONSerializable
    private final int type;
	@BSONSerializable
    private final String name;
	@BSONSerializable
    private int rate;
	@BSONSerializable
    private int projectileSpeed;
	@BSONSerializable
    private int damage;
	@BSONSerializable
    private int energyConsumption;
	@BSONSerializable
    private int distance;
	@BSONSerializable
    private int range;
	@BSONSerializable
    private int cooldown;

    public WeaponModel(int id, String name, int rate, int type, int projectileSpeed, int damage,
                          int energyConsumption, int distance, int range, int cooldown) {
        this.name = name;
        this.rate = rate;
        this.id = id;
        this.type = type;
        this.projectileSpeed = projectileSpeed;
        this.damage = damage;
        this.energyConsumption = energyConsumption;
        this.distance = distance;
        this.range = range;
        this.cooldown = cooldown;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getRate() {
        return rate;
    }

    public int getProjectileSpeed() {
        return projectileSpeed;
    }

    public int getDamage() {
        return damage;
    }

    public int getEnergyConsumption() {
        return energyConsumption;
    }

    public int getDistance() {
        return distance;
    }

    public int getRange() {
        return range;
    }

    public int getCooldown() {
        return cooldown;
    }
}
