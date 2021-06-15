package com.company;

import com.company.Element.Location;
import com.company.Parsing.ActionsParser;
import com.company.Parsing.EntitiesParser;

import java.util.ArrayList;
import java.util.HashMap;

public class StagEngine {
    private ArrayList<Player> players;
    private HashMap<String, Location> locations;
    private ArrayList<Action> actions;

    public StagEngine(String entityFilename, String actionFilename){
        //parse files and get data
        EntitiesParser entitiesParser = new EntitiesParser(entityFilename);
        locations = entitiesParser.getLocations();
        ActionsParser actionsParser = new ActionsParser(actionFilename);
        actions = actionsParser.getActions();
    }

    public void addPlayer(String name){
        Player newPlayer = new Player(name);
        //set location to start
        newPlayer.setLocation(locations.get("start"));
        players.add(newPlayer);
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }
}
