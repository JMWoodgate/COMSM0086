package com.company.Element;

import com.company.Visitor.Visitor;

public class Artefact implements Element{
    public String name;
    public String description;

    public Artefact(String name, String description){
        this.name = name;
        this.description = description;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }
}
