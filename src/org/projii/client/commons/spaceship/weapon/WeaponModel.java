package org.projii.client.commons.spaceship.weapon;

public class WeaponModel {
    private final int id;
    private final int type;
    private final String name;
    private int rate;
    private int projectileSpeed;
    private int damage;
    private int energyConsumption;
    private int distance;
    private int range;
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
