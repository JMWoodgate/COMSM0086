package com.company.Command;

import com.company.Action;
import com.company.StagExceptions.SubjectDoesNotExist;
import com.company.Subject.*;
import com.company.Subject.Character;

import java.util.ArrayList;

public class Produce implements Command{

    Action action;
    Location playerLocation;
    ArrayList<Location> locations;

    public Produce(Action action, ArrayList<Location> locations){
        this.action = action;
        this.locations = locations;
    }

    @Override
    public String execute(Player player) throws SubjectDoesNotExist {
        ArrayList<String> produced = action.getProduced();
        //if there is nothing to produce by the action, we can skip this
        if(produced == null){
            return null;
        }
        //loop through items produced and add them to the location
        for(String p : produced){
            if(p.equals("health")){
                player.changeHealth(true);
            }else{
                moveSubject(p);
            }
        } return null;
    }

    private void moveSubject(String subject)
            throws SubjectDoesNotExist {
        //need to check all locations for the artefact (not just unplaced)
        Location subjectLocation = (Location) getSubject(subject, new ArrayList<>(locations));
        if(subjectLocation!=null){
            //need to add a path to the new location
            playerLocation.setPath(subject);
            return;
        }
        for(Location l : locations){
            //check if it is an artefact & move if so (function will return true)
            if(moveArtefact(l, subject)) {
                return;
            }
            //check if it is furniture & move if so
            if(moveFurniture(l, subject)) {
                return;
            }
            //check if it is a character & move if so
            if(moveCharacter(l, subject)){
                return;
            }
        }
        throw new SubjectDoesNotExist();
    }

    private boolean moveCharacter(Location locationToCheck, String subject){
        //get character from location
        Character character = (Character) getSubject(
                subject, new ArrayList<>(locationToCheck.getCharacters()));
        if(character!=null){
            playerLocation.setCharacter(character.getName(), character.getDescription());
            locationToCheck.removeCharacter(character.getName());
            return true;
        }
        return false;
    }

    private boolean moveFurniture(Location locationToCheck, String subject){
        //get furniture from location
        Furniture furniture = (Furniture) getSubject(
                subject, new ArrayList<>(locationToCheck.getFurniture()));
        if(furniture!=null){
            playerLocation.setFurniture(furniture.getName(), furniture.getDescription());
            locationToCheck.removeFurniture(furniture.getName());
            return true;
        }
        return false;
    }

    private boolean moveArtefact(Location locationToCheck, String subject){
        //get artefact from location
        Artefact artefact = (Artefact) getSubject(
                subject, new ArrayList<>(locationToCheck.getArtefacts()));
        //check if the artefact to produce exists in this location
        if(artefact!=null){
            //create new artefact in the current location
            playerLocation.setArtefact(artefact.getName(),
                    artefact.getDescription());
            //remove artefact from old location
            locationToCheck.removeArtefact(artefact.getName());
            return true;
        }
        return false;
    }

    public Subject getSubject(String subjectName, ArrayList<Subject> subjectList){
        if(subjectList == null){
            return null;
        }
        for(Subject e : subjectList){
            if(e!=null && e.getName().equals(subjectName)){
                return e;
            }
        }
        return null;
    }
}
