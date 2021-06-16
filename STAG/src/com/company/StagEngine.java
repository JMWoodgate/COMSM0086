package com.company;

import com.company.Element.Artefact;
import com.company.Element.Location;
import com.company.Element.Player;
import com.company.Parsing.ActionsParser;
import com.company.Parsing.EntitiesParser;
import com.company.StagExceptions.ArtefactDoesNotExist;
import com.company.StagExceptions.InvalidCommand;
import com.company.StagExceptions.LocationDoesNotExist;
import com.company.StagExceptions.StagException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class StagEngine {
    private final HashMap<String, Player> players;
    private final HashMap<String, Location> locations;
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
                throw new InvalidCommand(command);
        }
    }

    private String lookCommand(){
        //need to return a string that describes the whole location
        Location playerLocation = currentPlayer.getLocation();
        StringBuilder stringBuilder = new StringBuilder();
        //get location name/description
        stringBuilder.append("You are in "+playerLocation.getDescription()+". ");
        //list artefacts in the roomc
        stringBuilder.append("You can see:\n");
        ArrayList<Artefact> artefacts = playerLocation.getArtefacts();
        for(Artefact a : artefacts){
            stringBuilder.append(a.getDescription()+"\n");
        }
        stringBuilder.append("You can access from here:\n");
        ArrayList<String> paths = playerLocation.getPaths();
        for(String p : paths){
            stringBuilder.append(p+"\n");
        }
        return stringBuilder.toString();
    }

    private String gotoCommand(String newLocation) throws LocationDoesNotExist{
        Location playerLocation = currentPlayer.getLocation();
        if(locations.containsKey(newLocation) && playerLocation.validPath(newLocation)){
            //need to check path is valid
            currentPlayer.setLocation(locations.get(newLocation));
            String message = "You have moved to "+newLocation;
            return message;
        } else{
            throw new LocationDoesNotExist(newLocation);
        }
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
        return (artefact+" does not exist here.\n");
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
        newPlayer.setLocation(locations.get("start"));
        players.put(playerName, newPlayer);
        currentPlayer = newPlayer;
    }

    public HashMap<String, Player> getPlayers(){
        return players;
    }
}
