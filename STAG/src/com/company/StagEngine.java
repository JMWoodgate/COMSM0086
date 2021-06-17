package com.company;

import com.company.Element.Artefact;
import com.company.Element.Character;
import com.company.Element.Furniture;
import com.company.Element.Location;
import com.company.Element.Player;
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

    private void produce(Action action) throws ArtefactDoesNotExist {
        ArrayList<String> produced = action.getProduced();
        //if there is nothing to produce by the action, we can skip this
        if(produced == null){
            return;
        }
        //loop through items produced and add them to the location
        for(String p : produced){
            moveArtefactToProduce(p);
        }
    }

    private void moveArtefactToProduce(String artefactToProduce)
            throws ArtefactDoesNotExist{
        Location playerLocation = currentPlayer.getLocation();
        //need to check all locations for the artefact (not just unplaced)
        //could produce an artefact or a location
        for(Location l : locations){
            ArrayList<Artefact> locationArtefacts = l.getArtefacts();
            //get artefact from location
            Artefact producedArtefact = getSpecificArtefact(
                    artefactToProduce, locationArtefacts);
            //check if the artefact to produce exists in this location,
            // otherwise move to next location
            if(producedArtefact!=null){
                //create new artefact in the current location
                playerLocation.setArtefact(producedArtefact.getName(),
                        producedArtefact.getDescription());
                //remove artefact from old location
                l.removeArtefact(producedArtefact);
                //exit
                return;
            }
        }
        throw new ArtefactDoesNotExist(artefactToProduce);
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
            if (getSpecificArtefact(c, playerLocation.getArtefacts())!=null) {
                //delete from location
                playerLocation.removeArtefact(c);
            } else if (getSpecificArtefact(c, currentPlayer.getInventory())!=null){
                //delete from player inventory
                currentPlayer.removeFromInventory(c);
            } else if (getSpecificFurniture(c, playerLocation.getFurniture())!=null){
                playerLocation.removeFurniture(c);
            } else if (getSpecificCharacter(c, playerLocation.getCharacters())!=null){
                playerLocation.removeCharacter(c);
            } else{
                //should be unreachable as we have already checked for this, but just in case
                throw new SubjectDoesNotExist();
            }
        }
    }

    private boolean checkSubjects(Action action) {
        ArrayList<String> subjects = action.getSubjects();
        ArrayList<Artefact> inventory = currentPlayer.getInventory();
        Location playerLocation = currentPlayer.getLocation();
        ArrayList<Artefact> locationArtefacts = playerLocation.getArtefacts();
        for(String s : subjects){
            //check if subject exists in artefacts
            if(getSpecificArtefact(s, inventory)==null
                && getSpecificArtefact(s, locationArtefacts)==null){
                //check in furniture
                if(getSpecificFurniture(s, playerLocation.getFurniture())==null){
                    //check in characters
                    if(getSpecificCharacter(s, playerLocation.getCharacters())==null){
                        return false;
                    }
                }
            }
        } return true;
    }

    //if furniture is in the list, return it, otherwise return null -- visitor method?
    public Character getSpecificCharacter(String characterName, ArrayList<Character> characterList){
        for(Character c : characterList){
            if(c.getName().equals(characterName) || c.getDescription().equals(characterName)){
                return c;
            }
        }
        return null;
    }

    //if furniture is in the list, return it, otherwise return null -- visitor method?
    public Furniture getSpecificFurniture(String furnitureName, ArrayList<Furniture> furnitureList){
        for(Furniture f : furnitureList){
            if(f.getDescription().equals(furnitureName) || f.getName().equals(furnitureName)){
                return f;
            }
        }
        return null;
    }

    //if the artefact is in the list, return it, otherwise return null -- visitor method?
    public Artefact getSpecificArtefact(String artefactName, ArrayList<Artefact> artefactList){
        for(Artefact a : artefactList){
            if(a.getDescription().equals(artefactName) || a.getName().equals(artefactName)){
                return a;
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
