package org.projii.client.commons.spaceship;

public class SpaceshipModel {

    public final String name;
    public final int id;
    public final int length;
    public final int width;
    public final int health;
    public final int weaponSlotCount;
    public final int armor;

    public SpaceshipModel(String name, int id, int length, int width, int health, int weaponSlotAmount, int armor) {
        this.name = name;
        this.id = id;
        this.length = length;
        this.width = width;
        this.health = health;
        this.weaponSlotCount = weaponSlotAmount;
        this.armor = armor;
    }

}
