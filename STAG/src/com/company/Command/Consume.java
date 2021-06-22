package com.company.Command;

import com.company.Action;
import com.company.StagExceptions.SubjectDoesNotExist;
import com.company.Subject.Artefact;
import com.company.Subject.Location;
import com.company.Subject.Player;
import com.company.Subject.Subject;

import java.util.ArrayList;

public class Consume implements Command{

    Action action;
    Player player;
    Location playerLocation;
    ArrayList<Location> locations;

    public Consume(Action action, ArrayList<Location> locations){
        this.action = action;
        this.locations = locations;
    }

    @Override
    public String run(Player player) throws SubjectDoesNotExist {
        String message = null;
        this.player = player;
        playerLocation = player.getLocation();
        //check if subject is in player inventory or current player location
        ArrayList<String> consumed = action.getConsumed();
        //if there is nothing to consume, move on
        if (consumed == null) {
            return null;
        }
        for(String c : consumed) {
            if(c.equals("health")){
                message = consumeHealth();
            }
            //we need to check if the artefact, furniture or character is there
            else if (getSubject(c, new ArrayList<>(playerLocation.getArtefacts()))!=null) {
                //delete artefact from location
                //playerLocation.removeArtefact(c);
                playerLocation.removeSubject(c, playerLocation.getArtefacts());
            } else if (getSubject(c, new ArrayList<>(player.getInventory()))!=null){
                //delete artefact from player inventory
                player.removeFromInventory(c);
            } else if (getSubject(c, new ArrayList<>(playerLocation.getFurniture()))!=null){
                //delete furniture from location
                //playerLocation.removeFurniture(c);
                playerLocation.removeSubject(c, playerLocation.getFurniture());
            } else if (getSubject(c, new ArrayList<>(playerLocation.getCharacters()))!=null){
                //delete character from location
                //playerLocation.removeCharacter(c);
                playerLocation.removeSubject(c, playerLocation.getCharacters());
            } else if(getSubject(c, new ArrayList<>(locations))==null){
                //delete paths to location -> not the actual location
                locations.removeIf(l -> l.getName().equals(c));
            }else{
                //should be unreachable as we have already checked for this, but just in case
                //should we be checking for this twice..?
                throw new SubjectDoesNotExist();
            }
        }
        return message;
    }

    private String consumeHealth(){
        String message = null;
        player.changeHealth(false);
        //if health is zero, need to drop all items in inventory
        if(player.getHealth()==0){
            //get player inventory
            ArrayList<Subject> inventory = player.getInventory();
            //if there are items in inventory, place them in the current location
            emptyInventory(inventory);
            //return player to start
            player.setLocation(locations.get(0));
            playerLocation = player.getLocation();
            player.resetHealth();
            //return message
            message = "You ran out of health! You have lost your inventory and been returned to the start.";
        }
        return message;
    }

    private void emptyInventory(ArrayList<Subject> inventory){
        while (!inventory.isEmpty()) {
            Subject s = inventory.get(0);
            //put each artefact in the location
            playerLocation.setArtefact(s.getName(), s.getDescription());
            //remove from the player's inventory
            player.removeFromInventory(s);
        }
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
