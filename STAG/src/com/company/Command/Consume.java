package com.company.Command;

import com.company.Action;
import com.company.StagExceptions.SubjectDoesNotExist;
import com.company.Element.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Consume implements Command{

    private final Action action;
    private Player player;
    private Location playerLocation;
    private final ArrayList<Element> locations;
    private final Subject subjectUtility;
    private final HashMap<String, Player> players;

    public Consume(Action action, ArrayList<Element> locations,
                   HashMap<String, Player> players){
        this.action = action;
        this.locations = locations;
        this.players = players;
        //this can be used to access the utility functions inside Subject
        subjectUtility = new Subject();
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
            //if the item is health, we
            if(c.equals("health")){
                message = consumeHealth();
            }
            //we need to check if the artefact, furniture or character is there
            else if (subjectUtility.getSubject(c, new ArrayList<>(playerLocation.getArtefacts()))!=null) {
                //delete artefact from location
                //playerLocation.removeArtefact(c);
                subjectUtility.removeSubject(c, playerLocation.getArtefacts());
            } else if (subjectUtility.getSubject(c, player.getInventory())!=null){
                //delete artefact from player inventory
                subjectUtility.removeSubject(c, player.getInventory());
            } else if (subjectUtility.getSubject(c, playerLocation.getFurniture())!=null){
                //delete furniture from location
                //playerLocation.removeFurniture(c);
                subjectUtility.removeSubject(c, playerLocation.getFurniture());
            } else if (subjectUtility.getSubject(c, playerLocation.getCharacters())!=null){
                //delete character from location
                //playerLocation.removeCharacter(c);
                subjectUtility.removeSubject(c, playerLocation.getCharacters());
            } else if(subjectUtility.getElement(c, new ArrayList<>(locations))==null){
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
            player.setLocation((Location)locations.get(0));
            playerLocation = player.getLocation();
            player.resetHealth();
            //return message
            message = "You ran out of health! You have lost your " +
                    "inventory and been returned to the start.\n";
            Look look = new Look(players);
            message += look.run(player);
        }
        return message;
    }

    private void emptyInventory(ArrayList<Subject> inventory){
        while (!inventory.isEmpty()) {
            Subject s = inventory.get(0);
            //put each artefact in the location
            s.setSubject(s.getName(), s.getDescription(),
                    playerLocation.getArtefacts());
            //remove from the player's inventory
            subjectUtility.removeSubject(s, player.getInventory());
        }
    }
}
