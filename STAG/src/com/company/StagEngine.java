package com.company;

import com.company.Command.*;
import com.company.Subject.*;
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
        if(command.contains("inv")) {
            Inventory inventory = new Inventory();
            return inventory.execute(currentPlayer);
        } else if(command.contains("get")) {
            Get get = new Get(command);
            return get.execute(currentPlayer);
        } else if(command.contains("drop")) {
            Drop drop = new Drop(command);
            return drop.execute(currentPlayer);
        } else if(command.contains("goto")) {
            GoTo goTo = new GoTo(command, locations, players);
            return goTo.execute(currentPlayer);
        } else if(command.contains("look")) {
            Look look = new Look(players);
            return look.execute(currentPlayer);
        } else if(command.contains("health")){
            Health health = new Health();
            return health.execute(currentPlayer);
        } else {
            Custom custom = new Custom(command, actions, locations);
            return custom.execute(currentPlayer);
        }
    }

    public void changePlayer(String playerName){
        currentPlayer = players.get(playerName);
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
