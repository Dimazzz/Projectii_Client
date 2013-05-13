package org.projii.client.commons.spaceship;

import org.projii.client.commons.space.Point;
import org.projii.client.commons.space.RealworldObject;
import org.projii.client.commons.spaceship.equipment.EnergyGenerator;
import org.projii.client.commons.spaceship.equipment.EnergyShield;
import org.projii.client.commons.spaceship.equipment.SpaceshipEngine;
import org.projii.client.commons.spaceship.weapon.Weapon;

public class Spaceship extends RealworldObject {

    private final SpaceshipModel model;
    private final Weapon[] weapons;
    private final EnergyGenerator generator;
    private final SpaceshipEngine engine;
    private final EnergyShield energyShield;

    public Spaceship(int id, SpaceshipModel model, Weapon[] weapons, EnergyGenerator generator,
                     SpaceshipEngine engine, EnergyShield energyShield) {

        super(id, model.length, model.width, null, 0, model.armor);
        this.model = model;
        this.weapons = weapons;
        this.generator = generator;
        this.engine = engine;
        this.energyShield = energyShield;
    }

    public Spaceship(int id, SpaceshipModel model, Weapon[] weapons, EnergyGenerator generator,
                     SpaceshipEngine engine, EnergyShield energyShield,
                     Point location, int rotation, int health) {

        super(id, model.length, model.width, location, rotation, health);
        this.model = model;
        this.weapons = weapons;
        this.generator = generator;
        this.engine = engine;
        this.energyShield = energyShield;
    }

    @Override
    public void damage(int damage) {
        setHealth(getHealth() - energyShield.protect(damage));
    }


    public SpaceshipModel getModel() {
        return model;
    }

    public Weapon[] getWeapons() {
        return weapons;
    }

    public EnergyGenerator getGenerator() {
        return generator;
    }

    public SpaceshipEngine getEngine() {
        return engine;
    }

    public EnergyShield getEnergyShield() {
        return energyShield;
    }
}
