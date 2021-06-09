package com.company.Element;

import com.company.Visitor.Visitor;
import java.util.HashMap;

public class Location implements Element{
    private String name;
    private final HashMap<String, String> artefacts;
    private final HashMap<String, String> furniture;

    public Location(){
        artefacts = new HashMap();
        furniture = new HashMap();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
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
}
