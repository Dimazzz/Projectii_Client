package org.projii.client.commons.spaceship.weapon;

import org.projii.client.commons.space.Point;

public class Weapon {
    private WeaponModel model;
    private int currentCooldown;

    public Weapon(WeaponModel model, int currentCooldown) {
        this.model = model;
        this.currentCooldown = currentCooldown;
    }

    public Weapon(WeaponModel model) {
        this.model = model;
        this.currentCooldown = model.getCooldown();
    }

    public Projectile fire(Point direction, int rotation) {
//        return new Bullet(bulletId, location, rotation, 1000, bulletSpeed,
//                damage, direction, range);
        return null;
    }

    public WeaponModel getModel() {
        return model;
    }
}
