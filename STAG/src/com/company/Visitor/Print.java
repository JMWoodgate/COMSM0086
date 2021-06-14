package com.company.Visitor;

import com.company.Element.Element;
import com.company.Element.Location;

public class Print implements Visitor{

    @Override
    public void visit(Element element) {

    }

    @Override
    public void visit(Location location){
        System.out.println();
        System.out.println("Location: "+location.getName());
        System.out.println("Description: "+location.getDescription());
        System.out.println("Artefacts: ");
        System.out.println(location.getArtefacts().entrySet());
        System.out.println("Furniture: ");
        System.out.println(location.getFurniture().entrySet());
        System.out.println("Characters: ");
        System.out.println(location.getCharacters().entrySet());
        System.out.println("Location "+location.getName()+" leads to:");
        for(String path : location.getPaths()){
            System.out.print(path+", ");
        }
        System.out.println();
    }


}
