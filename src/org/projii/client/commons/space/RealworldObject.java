package org.projii.client.commons.space;

import org.jai.BSON.BSONSerializable;

public abstract class RealworldObject {
	@BSONSerializable
    private final int id;
	@BSONSerializable
    private final int length;
	@BSONSerializable
    private final int width;
	@BSONSerializable
    private Point location;
	@BSONSerializable
    private int rotation;
	@BSONSerializable
    private int health;

    protected RealworldObject(int id, int length, int width, Point location, int rotation, int health) {
        this.id = id;
        this.length = length;
        this.width = width;
        this.location = location;
        this.rotation = rotation;
        this.health = health;
    }

    public int getId() {
        return id;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public void move(Point location, int rotation) {
        setLocation(location);
        setRotation(rotation);
    }

    public int getHealth() {
        return health;
    }

    protected void setHealth(int health) {
        this.health = health;
    }

    public abstract void damage(int damage);




}
