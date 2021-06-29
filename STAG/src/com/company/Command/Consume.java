package com.company.Command;

import com.company.Action;
import com.company.Element.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Consume implements Command{

    private final Action action;
    private Player currentPlayer;
    private Location playerLocation;
    private final ArrayList<Element> locations;
    private final SubjectUtility subjectUtility;
    private final HashMap<String, Player> players;

    public Consume(Action action, ArrayList<Element> locations,
                   HashMap<String, Player> players){
        this.action = action;
        this.locations = locations;
        this.players = players;
        subjectUtility = new SubjectUtility();
    }

    @Override
    public String runCommand(Player currentPlayer) throws Exception {
        String message = null;
        this.currentPlayer = currentPlayer;
        playerLocation = currentPlayer.getLocation();
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

    private void consumeSubject(String consumed) throws Exception {
        //look for subject in locations
        Subject subject = subjectUtility
                .getSubjectFromLocation(consumed, playerLocation);
        if (subject != null) {
            //delete subject from location
            subjectUtility
                    .removeSubjectFromLocation(subject, playerLocation);
        } //look for subject in player inventory
        else if (subjectUtility
                .getSubject(consumed, currentPlayer.getInventory()) != null) {
            //delete subject from player inventory
            subjectUtility
                    .removeSubject(consumed, currentPlayer.getInventory());
        } //look for subject in locations
        else if (subjectUtility
                .getElement(consumed, new ArrayList<>(locations)) != null) {
            //delete paths to location -> not the actual location
            consumePath(consumed);
        }
        else {
            throw new Exception("Subject "+consumed+" does not exist.");
        }
    }

    private void consumePath(String consumed){
        //loop through all locations
        for(Element l : locations){
            Location location = (Location)l;
            //get the paths from each location
            ArrayList<String> paths = location.getPaths();
            //if equivalent to consumed, remove path to it
            paths.removeIf(p -> p.equals(consumed));
        }
    }

    private String consumeHealth(){
        String message = null;
        currentPlayer.changeHealth(false);
        //if health is zero, drop all items in inventory
        if(currentPlayer.getHealth()==0){
            ArrayList<Subject> inventory = currentPlayer.getInventory();
            //if items in inventory, place in current location
            emptyInventory(inventory);
            //return player to start
            currentPlayer.setLocation((Location)locations.get(0));
            playerLocation = currentPlayer.getLocation();
            currentPlayer.resetHealth();
            //return message
            message = "You ran out of health! You have lost your " +
                    "inventory and been returned to the start.\n";
            Look look = new Look(players);
            message += look.runCommand(currentPlayer);
        }
        return message;
    }

    private void emptyInventory(ArrayList<Subject> inventory){
        while (!inventory.isEmpty()) {
            Subject s = inventory.get(0);
            //put each artefact in current location
            subjectUtility.setSubject(s, playerLocation.getArtefacts());
            //remove from the player's inventory
            subjectUtility.removeSubject(s, currentPlayer.getInventory());
        }
    }
}
