package com.company.Element;

public class Subject implements Element {

    private final String subjectName;
    private final String description;
    private final String type;

    public Subject(String subjectName, String description,
                   String type){
        this.subjectName = subjectName;
        this.description = description;
        //type will be artefact, character, or furniture
        this.type = type;
    }

    public String getType(){ return type; }

    public String getName(){
        return subjectName;
    }

    public String getDescription(){
        return description;
    }
}
