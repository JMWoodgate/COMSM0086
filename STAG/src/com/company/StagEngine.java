package com.company;

import com.company.Element.Artefact;
import com.company.Element.Location;
import com.company.Element.Player;
import com.company.Parsing.ActionsParser;
import com.company.Parsing.EntitiesParser;
import com.company.StagExceptions.InvalidCommand;

import java.util.ArrayList;
import java.util.HashMap;

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

    public String interpretCommand(String command) throws InvalidCommand {
        String[] splitString = command.split(" ", 2);
        String message = null;
        switch(splitString[0]){
            case "inv":
            case "inventory":
                message = getInventory();
            case "get":
            case "drop":
            case "goto":
            case "look":
                return message;
            default:
                throw new InvalidCommand(command);
        }
    }

    private String getInventory(){
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
