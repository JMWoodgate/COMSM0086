package com.company.Element;

import com.company.Visitor.Visitor;

import java.util.ArrayList;

public class Location implements Element{
    private String name;
    private String description;
    private final ArrayList<Artefact> artefacts;
    private final ArrayList<Furniture> furniture;
    private final ArrayList<Character> characters;
    private final ArrayList<String> paths;

    public Location(){
        artefacts = new ArrayList<>();
        furniture = new ArrayList<>();
        characters = new ArrayList<>();
        paths = new ArrayList<>();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public boolean validPath(String newLocation){
        if(paths.contains(newLocation)){
            return true;
        } else{
            return false;
        }
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

    public void setArtefact(String name, String description){
        Artefact artefact = new Artefact(name, description);
        artefacts.add(artefact);
    }

    public ArrayList<Artefact> getArtefacts(){
        return artefacts;
    }

    public void removeArtefact(Artefact artefact){
        artefacts.remove(artefact);
    }

    public void setFurniture(String name, String description){
        Furniture newFurniture = new Furniture(name, description);
        furniture.add(newFurniture);
    }

    public ArrayList<Furniture> getFurniture(){
        return furniture;
    }

    public void setCharacter(String name, String description){
        Character newCharacter = new Character(name, description);
        characters.add(newCharacter);
    }

    public ArrayList<Character> getCharacters(){
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
