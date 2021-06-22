package com.company.Element;

import java.util.ArrayList;

public class Player implements Element {

    private final String name;
    private final ArrayList<Subject> inventory = new ArrayList<>();
    private Location location;
    private int health;

    public Player(String name){
        this.name = name;
        health = 3;
    }

    public void changeHealth(boolean minusOrPlus){
        if(minusOrPlus){
            health++;
        }else{
            health--;
        }
    }

    public void resetHealth(){
        health = 3;
    }

    public int getHealth(){
        return health;
    }

    public Location getLocation(){
        return location;
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public ArrayList<Subject> getInventory(){
        return inventory;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return null;
    }
}
