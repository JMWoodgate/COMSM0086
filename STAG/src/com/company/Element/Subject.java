package com.company.Element;

public class Subject implements Element {

    private final String name;
    private final String description;
    private final String type;
    //private final Location location;

    public Subject(String name, String description,
                   String type){
        this.name = name;
        this.description = description;
        //type will be artefact, character, or furniture
        this.type = type;
        //if location is null, it is in the player's inventory
        //this.location = location;
    }

    //public Location getLocation(){ return location; }

    public String getType(){ return type; }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }
}
