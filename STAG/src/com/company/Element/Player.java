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
