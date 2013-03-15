package org.projii.client.commons.space;

import org.projii.client.commons.spaceship.Spaceship;
import org.projii.client.commons.spaceship.weapon.Projectile;

import java.util.List;

public class Map {

    private List<Spaceship> shipList;
    private List<Projectile> projectileList;

    private int width;
    private int height;

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public List<Spaceship> getShipList() {
        return shipList;
    }
    public void setShipList(List<Spaceship> shipList) {
        this.shipList = shipList;
    }
    
    public List<Spaceship> getBulletList() {
        return shipList;
    }
    public void setBulletList(List<Spaceship> shipList) {
        this.shipList = shipList;
    }
    public void addBulletInList(Projectile projectile){
    	projectileList.add(projectile);
    }
    
    public Map(int width, int height, List<Spaceship> shipList) {
        this.width = width;
        this.height = height;
        this.shipList = shipList;
    }
}
