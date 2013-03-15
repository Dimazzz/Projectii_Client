package org.projii.client.commons.spaceship.weapon;

import org.projii.client.commons.space.Point;
import org.projii.client.commons.space.RealworldObject;

public class Projectile extends RealworldObject {
    private int speed;
    private int damage;
    private Point destination;
    private int range;     //радиус взрыва

    public Projectile(int id, Point location, int rotation, int health, int speed,
                  int damage, Point destination, int range) {

        super(id, 1, 1, location, rotation, health);
        this.speed = speed;
        this.damage = damage;
        this.destination = destination;
        this.range = range;
    }

    public void move() {
        int a = (int) (speed * Math.sin(getRotation()));
        int b = (int) (speed * Math.cos(getRotation()));
        getLocation().setX(this.getLocation().getX() + a);
        getLocation().setY(this.getLocation().getY() + b);
    }

    public int getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public Point getDestination() {
        return destination;
    }

    public int getRange() {
        return range;
    }

    @Override
    public void damage(int damage) {
        //Button cannot be destroyed
    }
}
