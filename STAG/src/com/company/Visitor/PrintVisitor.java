package com.company.Visitor;

import com.company.Element.*;
import com.company.Element.Character;

import java.util.ArrayList;

public class PrintVisitor implements Visitor{

    @Override
    public void visit(Element element) {

    }

    @Override
    public void visit(Location location){
        System.out.println();
        System.out.println("Location: "+location.getName());
        System.out.println("Description: "+location.getDescription());
        System.out.println("Artefacts: ");
        ArrayList<Artefact> artefacts = location.getArtefacts();
        for(Artefact a : artefacts){
            System.out.println("    Name - "+a.getName());
            System.out.println("    Description - "+a.getDescription());
        }
        System.out.println("Furniture: ");
        ArrayList<Furniture> furniture = location.getFurniture();
        for(Furniture f : furniture){
            System.out.println("    Name - "+f.getName());
            System.out.println("    Description - "+f.getDescription());
        }
        System.out.println("Characters: ");
        ArrayList<Character> characters = location.getCharacters();
        for(Character c : characters){
            System.out.println("    Name - "+c.getName());
            System.out.println("    Description - "+c.getDescription());
        }
        System.out.println("Location "+location.getName()+" leads to:");
        for(String path : location.getPaths()){
            System.out.print(path+", ");
        }
        System.out.println();
    }


    @Override
    public void visit(Artefact artefact) {
        System.out.println("Artefact: "+artefact.getName());
        System.out.println("Description: "+artefact.getDescription());
    }

    @Override
    public void visit(Character character) {
        System.out.println("Character: "+ character.getName());
        System.out.println("Description: "+character.getDescription());
    }

    @Override
    public void visit(Furniture furniture) {
        System.out.println("Furniture: "+furniture.getName());
        System.out.println("Description: "+furniture.getDescription());
    }

    @Override
    public void visit(Player player) {
        System.out.println("Player: "+player.getName());
    }

}
