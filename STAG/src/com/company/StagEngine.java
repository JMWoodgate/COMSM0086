package com.company;

import com.company.Element.*;
import com.company.Element.Character;
import com.company.Parsing.ActionsParser;
import com.company.Parsing.EntitiesParser;
import com.company.StagExceptions.*;

import java.util.*;

public class StagEngine {
    private final HashMap<String, Player> players;
    private final ArrayList<Location> locations;
    private final ArrayList<Action> actions;
    private Player currentPlayer;

    public StagEngine(String entityFilename, String actionFilename){
        //parse files and get data
        EntitiesParser entitiesParser = new EntitiesParser(entityFilename);
        locations = entitiesParser.getLocations();
        ActionsParser actionsParser = new ActionsParser(actionFilename);
        actions = actionsParser.getActions();
        players = new HashMap<>();
    }

    public String interpretCommand(String command) throws StagException {
        command = command.toLowerCase(Locale.ROOT);
        String[] splitString = command.split(" ", 3);
        System.out.println("command: "+splitString[1]);
        System.out.println("full: "+command);
        switch(splitString[1]){
            case "inv":
            case "inventory":
                return listInventory();
            case "get":
                return getCommand(splitString[2]);
            case "drop":
                return dropCommand(splitString[2]);
            case "goto":
                return gotoCommand(splitString[2]);
            case "look":
                return lookCommand();
            default:
                return customCommand(splitString);
        }
    }

    private String customCommand(String[] command) throws StagException{
        //loop through all of the actions we have read in from file
        for(Action a : actions){
            //get the action's triggers
            ArrayList<String> triggers = a.getTriggers();
            //if the command matches a trigger word of the action, check subjects, consume, produce, then return narration
            if(triggers.contains(command[1])){
                //check all subjects exist in game and in command
                if(!checkSubjects(a)){
                    throw new SubjectDoesNotExist();
                }
                //check if anything to consume, if there is, consume it (remove from location or inventory)
                consume(a);
                //check if anything to produce, if there is, add to location
                produce(a);
                //return the action's narration
                return a.getNarration();
            }
        }
        //if we get through all the actions and haven't found it, invalid command
        throw new InvalidCommand(Arrays.toString(command));
    }

    private void produce(Action action) throws SubjectDoesNotExist {
        ArrayList<String> produced = action.getProduced();
        //if there is nothing to produce by the action, we can skip this
        if(produced == null){
            return;
        }
        //loop through items produced and add them to the location
        for(String p : produced){
            moveSubject(p);
        }
    }

    private void moveSubject(String subject)
            throws SubjectDoesNotExist {
        Location playerLocation = currentPlayer.getLocation();
        //need to check all locations for the artefact (not just unplaced)
        Location subjectLocation = (Location) getElement(subject, new ArrayList<>(locations));
        if(subjectLocation!=null){
            //need to add a path to the new location
            playerLocation.setPath(subject);
            return;
        }
        for(Location l : locations){
            //check if it is an artefact & move if so (function will return true)
            if(moveArtefact(l, playerLocation, subject)) {
                return;
            }
            //check if it is furniture & move if so
            if(moveFurniture(l, playerLocation, subject)) {
                return;
            }
            //check if it is a character & move if so
            if(moveCharacter(l, playerLocation, subject)){
                return;
            }
        }
        throw new SubjectDoesNotExist();
    }

    private boolean moveCharacter(Location locationToCheck,
                                  Location playerLocation, String subject){
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

    private boolean moveFurniture(Location locationToCheck,
                                  Location playerLocation, String subject){
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

    private boolean moveArtefact(Location locationToCheck,
                                 Location playerLocation, String subject){
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
    }

    private void consume(Action action) throws SubjectDoesNotExist {
        Location playerLocation = currentPlayer.getLocation();
        ArrayList<String> consumed = action.getConsumed();
        //if there is nothing to consume, move on
        if (consumed == null) {
            return;
        }
        for(String c : consumed) {
            //we need to check if the artefact, furniture or character is there -- not just artefact
            if (getElement(c, new ArrayList<>(playerLocation.getArtefacts()))!=null) {
                //delete artefact from location
                playerLocation.removeArtefact(c);
            } else if (getElement(c, new ArrayList<>(currentPlayer.getInventory()))!=null){
                //delete artefact from player inventory
                currentPlayer.removeFromInventory(c);
            } else if (getElement(c, new ArrayList<>(playerLocation.getFurniture()))!=null){
                //delete furniture from location
                playerLocation.removeFurniture(c);
            } else if (getElement(c, new ArrayList<>(playerLocation.getCharacters()))!=null){
                //delete character from location
                playerLocation.removeCharacter(c);
            } else if(getElement(c, new ArrayList<>(locations))==null){
                //delete location
                locations.removeIf(l -> l.getName().equals(c));
            }else{
                //should be unreachable as we have already checked for this, but just in case
                //should we be checking for this twice..?
                throw new SubjectDoesNotExist();
            }
        }
    }

    private boolean checkSubjects(Action action) {
        ArrayList<String> subjects = action.getSubjects();
        Location playerLocation = currentPlayer.getLocation();
        for(String s : subjects){
            //check in play inventory
            if(getElement(s, new ArrayList<>(currentPlayer.getInventory()))==null){
                //check in location artefacts
                if(getElement(s, new ArrayList<>(playerLocation.getArtefacts()))==null){
                    //check in location furniture
                    if(getElement(s, new ArrayList<>(playerLocation.getFurniture()))==null){
                        //check in location characters
                        if(getElement(s, new ArrayList<>(playerLocation.getCharacters()))==null){
                            //check in locations
                            if(getElement(s, new ArrayList<>(locations))==null){
                                //if the subject is in none of these, return false
                                return false;
                            }
                        }
                    }
                }
            }
        } return true;
    }

    public Element getElement(String elementName, ArrayList<Element> elementList){
        if(elementList == null){
            return null;
        }
        for(Element e : elementList){
            if(e!=null && e.getName().equals(elementName)){
                return e;
            }
        }
        return null;
    }

    private String lookCommand(){
        //need to return a string that describes the whole location
        Location playerLocation = currentPlayer.getLocation();
        StringBuilder stringBuilder = new StringBuilder();
        //get location name/description
        stringBuilder.append("You are in ").append(playerLocation.getDescription()).append(". ");
        //list artefacts in the location
        stringBuilder.append("You can see:\n");
        //list artefacts in location
        for(Artefact a : playerLocation.getArtefacts()){
            stringBuilder.append(a.getDescription()).append("\n");
        }
        //list furniture in location
        for(Furniture f : playerLocation.getFurniture()){
            stringBuilder.append(f.getDescription()).append("\n");
        }
        //list characters in location
        for(Character c : playerLocation.getCharacters()){
            stringBuilder.append(c.getDescription()).append(("\n"));
        }
        stringBuilder.append("You can access from here:\n");
        ArrayList<String> paths = playerLocation.getPaths();
        for(String p : paths){
            stringBuilder.append(p).append("\n");
        }
        return stringBuilder.toString();
    }

    private String gotoCommand(String newLocation) throws LocationDoesNotExist{
        Location playerLocation = currentPlayer.getLocation();
        //need to check path is valid
        if(playerLocation.validPath(newLocation)){
            //get the object for the new location and set it to the current player's location
            currentPlayer.setLocation(getSpecificLocation(newLocation));
            return "You have moved to "+newLocation;
        } else{
            throw new LocationDoesNotExist(newLocation);
        }
    }

    private Location getSpecificLocation(String newLocation) throws LocationDoesNotExist {
        for(Location l : locations){
            if(l.getName().equals(newLocation)){
                return l;
            }
        } throw new LocationDoesNotExist(newLocation);
    }

    private String dropCommand(String artefact) throws ArtefactDoesNotExist{
        Location playerLocation = currentPlayer.getLocation();
        ArrayList<Artefact> inventory = currentPlayer.getInventory();
        for(Artefact a : inventory){
            if(a.getName().equals(artefact) || a.getDescription().equals(artefact)){
                playerLocation.setArtefact(a.getName(), a.getDescription());
                String message = "You dropped "+a.getDescription()+" in "+playerLocation.getName();
                currentPlayer.removeFromInventory(a);
                return message;
            }
        } throw new ArtefactDoesNotExist(artefact);
    }

    private String getCommand(String artefact) throws ArtefactDoesNotExist {
        Location playerLocation = currentPlayer.getLocation();
        ArrayList<Artefact> locationArtefacts = playerLocation.getArtefacts();
        for(Artefact a : locationArtefacts){
            if(a.getName().equals(artefact) || a.getDescription().equals(artefact)){
                currentPlayer.addToInventory(a);
                String message = "You picked up "+(a.getDescription());
                playerLocation.removeArtefact(a);
                return message;
            }
        }
        throw new ArtefactDoesNotExist(artefact);
    }

    private String listInventory(){
        ArrayList<Artefact> artefacts = currentPlayer.getInventory();
        StringBuilder inventory = new StringBuilder();
        for(Artefact a : artefacts){
            inventory.append(a.getDescription());
            inventory.append("\n");
        }
        return (inventory.toString());
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    public boolean playerExists(String playerName){
        return players.containsKey(playerName);
    }

    public void addPlayer(String playerName){
        Player newPlayer = new Player(playerName);
        //set location to start
        newPlayer.setLocation(locations.get(0));
        players.put(playerName, newPlayer);
        currentPlayer = newPlayer;
    }

}
