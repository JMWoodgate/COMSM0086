package com.company.Element;

import com.company.Visitor.Visitor;

import java.util.ArrayList;
import java.util.HashMap;

public class Location implements Element{
    private String name;
    private String description;
    private final HashMap<String, String> artefacts;
    private final HashMap<String, String> furniture;
    private final HashMap<String, String> characters;
    private final ArrayList<String> paths;

    public Location(){
        artefacts = new HashMap<>();
        furniture = new HashMap<>();
        characters = new HashMap<>();
        paths = new ArrayList<>();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
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

    public void setArtefact(String key, String value){
        artefacts.put(key, value);
    }

    public HashMap<String, String> getArtefacts(){
        return artefacts;
    }

    public void setFurniture(String key, String value){
        furniture.put(key, value);
    }

    public HashMap<String, String> getFurniture(){
        return furniture;
    }

    public void setCharacter(String key, String value){
        characters.put(key, value);
    }

    public HashMap<String, String> getCharacters(){
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
