package com.company.Element;

import com.company.Visitor.Visitor;

import java.util.ArrayList;

public class Player implements Element{
    private final String name;
    private final ArrayList<Artefact> inventory = new ArrayList<>();
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

    public ArrayList<Artefact> getInventory(){
        return inventory;
    }

    public void removeFromInventory(Artefact artefact){
        inventory.remove(artefact);
    }

    public void removeFromInventory(String artefact){
        inventory.removeIf(
                a -> a.getName().equals(artefact)
                || a.getDescription().equals(artefact));
    }

    public void addToInventory(Artefact artefact){
        //making a deep copy of the artefact so it doesn't get deleted when we remove from location
        Artefact copy = new Artefact(artefact.getName(), artefact.getDescription());
        inventory.add(copy);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
