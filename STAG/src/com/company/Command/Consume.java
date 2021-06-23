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
    private final SubjectUtility subjectUtility;
    private final HashMap<String, Player> players;

    public Consume(Action action, ArrayList<Element> locations,
                   HashMap<String, Player> players){
        this.action = action;
        this.locations = locations;
        this.players = players;
        //this can be used to access the utility functions inside Subject
        subjectUtility = new SubjectUtility();
    }

    @Override
    public String run(Player player) throws SubjectDoesNotExist {
        String message = null;
        this.player = player;
        playerLocation = player.getLocation();
        ArrayList<String> consumed = action.getConsumed();
        //if there is nothing to consume, move on
        if (consumed == null) {
            return null;
        }
        for(String c : consumed) {
            //loop through each item and consume health or subject
            if(c.equals("health")){
                message = consumeHealth();
            }else {
                consumeSubject(c);
            }
        }
        return message;
    }

    private void consumeSubject(String consumed) throws SubjectDoesNotExist {
        //look for subject in locations
        Subject subject = subjectUtility.getSubjectFromLocation(consumed, playerLocation);
        if (subject != null) {
            subjectUtility.removeSubjectFromLocation(subject);
        } //look for subject in player inventory
        else if (subjectUtility.getSubject(consumed, player.getInventory()) != null) {
            //delete subject from player inventory
            subjectUtility.removeSubject(consumed, player.getInventory());
        } //look for subject in location paths
        else if (subjectUtility.getElement(consumed, new ArrayList<>(locations)) == null) {
            //delete paths to location -> not the actual location
            consumePath(consumed);
        }
        else {
            throw new SubjectDoesNotExist();
        }
    }

    private void consumePath(String consumed){
        //loop through all locations
        for(Element l : locations){
            Location location = (Location)l;
            //get the paths from each location
            ArrayList<String> paths = location.getPaths();
            //if a path in that location is equivalent to that consumed,
            // remove the path to it (not the actual location)
            paths.removeIf(p -> p.equals(consumed));
        }
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
            subjectUtility.setSubject(s.getName(), s.getDescription(),
                    s.getType(), playerLocation.getArtefacts(),
                    playerLocation);
            //remove from the player's inventory
            subjectUtility.removeSubject(s, player.getInventory());
        }
    }
}
