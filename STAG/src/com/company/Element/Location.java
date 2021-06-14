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

    public void printLocation(){
        System.out.println();
        System.out.println("Location: "+name);
        System.out.println("Description: "+description);
        System.out.println("Artefacts: ");
        System.out.println(artefacts.entrySet());
        System.out.println("Furniture: ");
        System.out.println(furniture.entrySet());
        System.out.println("Characters: ");
        System.out.println(characters.entrySet());
        System.out.println("Location "+name+" leads to:");
        for(String p : paths){
            System.out.print(p+", ");
        }
        System.out.println();
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

    public String getArtefact(String key){
        return artefacts.get(key);
    }

    public void setFurniture(String key, String value){
        furniture.put(key, value);
    }

    public String getFurniture(String key){
        return furniture.get(key);
    }

    public void setCharacter(String key, String value){
        characters.put(key, value);
    }

    public String getCharacter(String key){
        return characters.get(key);
    }

    //other locations accessible from the current location
    public void setPath(String target){
        paths.add(target);
    }
}
