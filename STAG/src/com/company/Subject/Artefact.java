package com.company.Subject;

public class Artefact implements Subject {
    public String name;
    public String description;

    public Artefact(String name, String description){
        this.name = name;
        this.description = description;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }
}
