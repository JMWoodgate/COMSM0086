package com.company.Element;

import com.company.Visitor.Visitor;

public class Character implements Element{
    String name;
    String description;

    public Character(String name, String description){
        this.name = name;
        this.description = description;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
