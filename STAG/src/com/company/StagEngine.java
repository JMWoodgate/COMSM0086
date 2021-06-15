package com.company;

import com.company.Element.Location;
import com.company.Parsing.ActionsParser;
import com.company.Parsing.EntitiesParser;

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
