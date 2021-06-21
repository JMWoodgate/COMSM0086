package com.company.Command;

import com.company.Action;
import com.company.StagExceptions.InvalidCommand;
import com.company.StagExceptions.StagException;
import com.company.StagExceptions.SubjectDoesNotExist;
import com.company.Subject.*;

import java.util.ArrayList;

public class Custom implements Command{

    String command;
    ArrayList<Action> actions;
    Location playerLocation;
    Player player;
    ArrayList<Location> locations;

    public Custom(String command, ArrayList<Action> actions, ArrayList<Location> locations){
        this.command = command;
        this.actions = actions;
        this.locations = locations;
    }

    @Override
    public String execute(Player player) throws StagException {
        playerLocation = player.getLocation();
        this.player = player;
        //loop through all of the actions we have read in from file
        for(Action a : actions){
            //get the action's triggers
            ArrayList<String> triggers = a.getTriggers();
            //if the command matches a trigger word of the action, check subjects,
            // consume, produce, then return narration
            for(String t : triggers) {
                if (command.contains(t)) {
                    //check all subjects exist in command and in game
                    checkCommand(command, a);
                    checkSubjects(a);
                    //check if anything to consume, if there is, remove from location/inventory
                    Consume consume = new Consume(a);
                    String message = consume.execute(player);
                    //check if anything to produce, if there is, add to location
                    Produce produce = new Produce(a, locations);
                    produce.execute(player);
                    //return the action's narration - with message if health ran out
                    if(message!=null){
                        return a.getNarration()+"\n"+message;
                    } else{
                        return a.getNarration();
                    }
                }
            }
        }
        //if we get through all the actions and haven't found it, invalid command
        throw new InvalidCommand(command);
    }

    /*private void produce(Action action) throws SubjectDoesNotExist {
        ArrayList<String> produced = action.getProduced();
        //if there is nothing to produce by the action, we can skip this
        if(produced == null){
            return;
        }
        //loop through items produced and add them to the location
        for(String p : produced){
            if(p.equals("health")){
                player.changeHealth(true);
            }else{
                moveSubject(p);
            }
        }
    }

    private void moveSubject(String subject)
            throws SubjectDoesNotExist {
        //need to check all locations for the artefact (not just unplaced)
        Location subjectLocation = (Location) getElement(subject, new ArrayList<>(locations));
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
    }*/

    /*private boolean moveCharacter(Location locationToCheck, String subject){
        //get character from location
        Character character = (Character) getElement(
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
        Furniture furniture = (Furniture) getElement(
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
        Artefact artefact = (Artefact) getElement(
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
    }*/

    /*private String consume(Action action) throws SubjectDoesNotExist {
        String message = null;
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
            //we need to check if the artefact, furniture or character is there -- not just artefact
            else if (getElement(c, new ArrayList<>(playerLocation.getArtefacts()))!=null) {
                //delete artefact from location
                playerLocation.removeArtefact(c);
            } else if (getElement(c, new ArrayList<>(player.getInventory()))!=null){
                //delete artefact from player inventory
                player.removeFromInventory(c);
            } else if (getElement(c, new ArrayList<>(playerLocation.getFurniture()))!=null){
                //delete furniture from location
                playerLocation.removeFurniture(c);
            } else if (getElement(c, new ArrayList<>(playerLocation.getCharacters()))!=null){
                //delete character from location
                playerLocation.removeCharacter(c);
            } else if(getElement(c, new ArrayList<>(locations))==null){
                //delete paths to location -> not the actual location
                locations.removeIf(l -> l.getName().equals(c));
            }else{
                //should be unreachable as we have already checked for this, but just in case
                //should we be checking for this twice..?
                throw new SubjectDoesNotExist();
            }
        }
        return message;
    }*/

    /*private String consumeHealth(){
        String message = null;
        player.changeHealth(false);
        //if health is zero, need to drop all items in inventory
        if(player.getHealth()==0){
            //get player inventory
            ArrayList<Artefact> inventory = player.getInventory();
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
    }*/

    /*private void emptyInventory(ArrayList<Artefact> inventory){
        while (!inventory.isEmpty()) {
            Artefact a = inventory.get(0);
            //put each artefact in the location
            playerLocation.setArtefact(a.getName(), a.getDescription());
            //remove from the player's inventory
            player.removeFromInventory(a);
        }
    }*/

    private void checkCommand(String command, Action action) throws SubjectDoesNotExist {
        //need to check that at least one subject is present in the command (if required)
        if(action.getSubjects()!=null) {
            if (!checkCommandSubjects(command, action)) {
                throw new SubjectDoesNotExist();
            }
        }else{
            throw new SubjectDoesNotExist();
        }
    }

    private boolean checkCommandSubjects(String command, Action a){
        ArrayList<String> subjects = a.getSubjects();
        for(String s : subjects){
            if(command.contains(s)){
                return true;
            }
        }
        return false;
    }

    private void checkSubjects(Action action) throws SubjectDoesNotExist {
        ArrayList<String> subjects = action.getSubjects();
        for(String s : subjects){
            //check in play inventory
            if(getElement(s, new ArrayList<>(player.getInventory()))==null){
                //check in location artefacts
                if(getElement(s, new ArrayList<>(playerLocation.getArtefacts()))==null){
                    //check in location furniture
                    if(getElement(s, new ArrayList<>(playerLocation.getFurniture()))==null){
                        //check in location characters
                        if(getElement(s, new ArrayList<>(playerLocation.getCharacters()))==null){
                            //check in locations
                            if(getElement(s, new ArrayList<>(locations))==null){
                                //if the subject is in none of these, throw error
                                throw new SubjectDoesNotExist();
                            }
                        }
                    }
                }
            }
        }
    }

    public Subject getElement(String elementName, ArrayList<Subject> subjectList){
        if(subjectList == null){
            return null;
        }
        for(Subject e : subjectList){
            if(e!=null && e.getName().equals(elementName)){
                return e;
            }
        }
        return null;
    }

}
