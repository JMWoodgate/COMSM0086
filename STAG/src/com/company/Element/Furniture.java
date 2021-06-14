package com.company.Element;

import com.company.Visitor.Visitor;

public class Furniture implements Element {
    String name;
    String description;

    public Furniture(String name, String description){
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
