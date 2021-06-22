package com.company.Element;

import java.util.ArrayList;

public class Location implements Element{
    private String name;
    private String description;
    private final ArrayList<Subject> artefacts;
    private final ArrayList<Subject> furniture;
    private final ArrayList<Subject> characters;
    private final ArrayList<String> paths;

    public Location() {
        artefacts = new ArrayList<>();
        furniture = new ArrayList<>();
        characters = new ArrayList<>();
        paths = new ArrayList<>();
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

    public ArrayList<Subject> getArtefacts(){
        return artefacts;
    }

    public ArrayList<Subject> getFurniture(){
        return furniture;
    }

    public ArrayList<Subject> getCharacters(){
        return characters;
    }

    //other locations accessible from the current location
    public void setPath(String target){
        paths.add(target);
    }

    public ArrayList<String> getPaths(){
        return paths;
    }
}
