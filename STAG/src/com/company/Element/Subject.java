package com.company.Element;

public class Subject implements Element {

    private final String name;
    private final String description;
    private final String type;

    public Subject(String name, String description,
                   String type){
        this.name = name;
        this.description = description;
        //type will be artefact, character, or furniture
        this.type = type;
    }

    public String getType(){ return type; }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }
}
