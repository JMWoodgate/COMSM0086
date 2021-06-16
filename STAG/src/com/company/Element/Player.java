package com.company.Element;

import com.company.Visitor.Visitor;

import java.util.ArrayList;

public class Player implements Element{
    private String name;
    private ArrayList<Artefact> inventory;
    private Location location;

    public Player(String name){
        this.name = name;
        inventory = new ArrayList<>();
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
