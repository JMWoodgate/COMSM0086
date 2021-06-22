package com.company.Command;

import com.company.Action;
import com.company.StagExceptions.SubjectDoesNotExist;
import com.company.Subject.*;

import java.util.ArrayList;

public class Produce implements Command{

    Action action;
    Location playerLocation;
    ArrayList<Element> locations;
    Subject subjectUtility;

    public Produce(Action action, ArrayList<Element> locations){
        this.action = action;
        this.locations = locations;
        subjectUtility = new Subject(null, null);
    }

    @Override
    public String run(Player player) throws SubjectDoesNotExist {
        playerLocation = player.getLocation();
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
        Location subjectLocation = (Location) subjectUtility.getElement(
                subject, locations);
        //if subjectLocation returns null, the subject is itself a location
        if(subjectLocation!=null){
            //need to add a path to the new location from the current
            playerLocation.setPath(subject);
            return;
        }
        for(Element l : locations){
            //check if subject is an artefact & move if so
            if(moveArtefact((Location)l, subject)) {
                return;
            }
            //check if it is furniture & move if so
            if(moveFurniture((Location)l, subject)) {
                return;
            }
            //check if it is a character & move if so
            if(moveCharacter((Location)l, subject)){
                return;
            }
        }
        throw new SubjectDoesNotExist();
    }

    private boolean moveCharacter(Location locationToCheck, String subject){
        //get character from location
        Subject character = subjectUtility.getSubject(
                subject, new ArrayList<>(locationToCheck.getCharacters()));
        if(character!=null){
            subjectUtility.setSubject(character.getName(),
                    character.getDescription(), playerLocation.getCharacters());
            //locationToCheck.removeCharacter(character.getName());
            subjectUtility.removeSubject(character.getName(), locationToCheck.getCharacters());
            return true;
        }
        return false;
    }

    private boolean moveFurniture(Location locationToCheck, String subject){
        //get furniture from location
        Subject furniture = subjectUtility.getSubject(
                subject, new ArrayList<>(locationToCheck.getFurniture()));
        if(furniture!=null){
            subjectUtility.setSubject(furniture.getName(),
                    furniture.getDescription(), playerLocation.getFurniture());
            //locationToCheck.removeFurniture(furniture.getName());
            subjectUtility.removeSubject(furniture.getName(), locationToCheck.getFurniture());
            return true;
        }
        return false;
    }

    private boolean moveArtefact(Location locationToCheck, String subject){
        //get artefact from location
        Subject artefact = subjectUtility.getSubject(
                subject, new ArrayList<>(locationToCheck.getArtefacts()));
        //check if the artefact to produce exists in this location
        if(artefact!=null){
            //create new artefact in the current location
            subjectUtility.setSubject(artefact.getName(),
                    artefact.getDescription(), playerLocation.getArtefacts());
            //remove artefact from old location
            //locationToCheck.removeArtefact(artefact.getName());
            subjectUtility.removeSubject(artefact.getName(), locationToCheck.getArtefacts());
            return true;
        }
        return false;
    }
}
