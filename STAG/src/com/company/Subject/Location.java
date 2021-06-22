package com.company.Subject;

import java.util.ArrayList;

public class Location implements Subject {
    private String name;
    private String description;
    private final ArrayList<Subject> artefacts;
    private final ArrayList<Subject> furniture;
    private final ArrayList<Subject> characters;
    private final ArrayList<String> paths;

    public Location(){
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

    public void setArtefact(String name, String description){
        Subject artefact = new Artefact(name, description);
        artefacts.add(artefact);
    }

    public ArrayList<Subject> getArtefacts(){
        return artefacts;
    }

    //visitor? --> similar in Player
    public void removeArtefact(Artefact artefact){
        artefacts.remove(artefact);
    }

    //visitor? --> similar in Player  --> similar for Artefact, Furniture, and Character
    public void removeArtefact(String artefact){
        artefacts.removeIf(
                a -> a.getName().equals(artefact)
                || a.getDescription().equals(artefact));
    }

    public void setFurniture(String name, String description){
        Furniture newFurniture = new Furniture(name, description);
        furniture.add(newFurniture);
    }

    public ArrayList<Subject> getFurniture(){
        return furniture;
    }

    //visitor? --> similar for Artefact, Furniture, and Character
    public void removeFurniture(String furnitureName){
        furniture.removeIf(
                f -> f.getName().equals(furnitureName)
                || f.getDescription().equals(furnitureName));
    }

    public void setCharacter(String name, String description){
        Character newCharacter = new Character(name, description);
        characters.add(newCharacter);
    }

    public ArrayList<Subject> getCharacters(){
        return characters;
    }

    //visitor?  --> similar for Artefact, Furniture, and Character
    public void removeCharacter(String character){
        characters.removeIf(
                c -> c.getName().equals(character)
                        || c.getDescription().equals(character));
    }

    public void removeSubject(String subject, ArrayList<Subject> subjectList){
        subjectList.removeIf(
                s -> s.getName().equals(subject)
                || s.getDescription().equals(subject));
    }

    public void removeSubject(Subject subject, ArrayList<Subject> subjectList){
        subjectList.remove(subject);
    }

    //other locations accessible from the current location
    public void setPath(String target){
        paths.add(target);
    }

    public ArrayList<String> getPaths(){
        return paths;
    }
}
