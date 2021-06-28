package com.company.Command;

import com.company.Action;
import com.company.StagExceptions.SubjectDoesNotExist;
import com.company.Element.*;

import java.util.ArrayList;

public class Produce implements Command{

    private final Action action;
    private Location playerLocation;
    private final ArrayList<Element> locations;
    private final SubjectUtility subjectUtility;

    public Produce(Action action, ArrayList<Element> locations){
        this.action = action;
        this.locations = locations;
        subjectUtility = new SubjectUtility();
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
            //if the item produced is health, we increase the player's health
            if(p.equals("health")){
                player.changeHealth(true);
            }else{
                //otherwise we move the item produce to the player's location
                produceSubject(p);
            }
        } return null;
    }

    private void produceSubject(String subject)
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
        //loop through locations until we find the subject
        for(Element l : locations){
            //check if subject is an artefact & move if so
            Location location = (Location)l;
            if(subjectUtility.moveSubject(subject, location.getArtefacts(),
                    playerLocation.getArtefacts())) {
                return;
            }
            //check if it is furniture & move if so
            if(subjectUtility.moveSubject(subject, location.getFurniture(),
                    playerLocation.getFurniture())) {
                return;
            }
            //check if it is a character & move if so
            if(subjectUtility.moveSubject(subject, location.getCharacters(),
                    playerLocation.getCharacters())) {
                return;
            }
        }
        throw new SubjectDoesNotExist();
    }
}
