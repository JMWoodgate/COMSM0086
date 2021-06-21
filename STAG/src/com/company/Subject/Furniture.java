package com.company.Subject;

public class Furniture implements Subject {
    String name;
    String description;

    public Furniture(String name, String description){
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
