package com.company;

import com.company.Element.Artefact;
import com.company.Element.Location;

import java.util.ArrayList;

public class Player {
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

    public String getName(){
        return name;
    }
}
