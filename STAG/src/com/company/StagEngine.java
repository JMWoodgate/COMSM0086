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

    public String execute(String commandString) throws StagException{
        Command command = interpretCommand(commandString);
        return command.run(currentPlayer);
    }

    public Command interpretCommand(String command) throws StagException {
        command = command.toLowerCase(Locale.ROOT);
        if(command.contains("inv")) {
            return new Inventory();
        } else if(command.contains("get")) {
            return new Get(command);
        } else if(command.contains("drop")) {
            return new Drop(command);
        } else if(command.contains("goto")) {
            return new GoTo(command, locations, players);
        } else if(command.contains("look")) {
            return new Look(players);
        } else if(command.contains("health")){
            return new Health();
        } else {
            return new Custom(command, actions, locations);
        }
    }

    /*public String interpretCommand(String command) throws StagException {
        command = command.toLowerCase(Locale.ROOT);
        if(command.contains("inv")) {
            Inventory inventory = new Inventory();
            return inventory.run(currentPlayer);
        } else if(command.contains("get")) {
            Get get = new Get(command);
            return get.run(currentPlayer);
        } else if(command.contains("drop")) {
            Drop drop = new Drop(command);
            return drop.run(currentPlayer);
        } else if(command.contains("goto")) {
            GoTo goTo = new GoTo(command, locations, players);
            return goTo.run(currentPlayer);
        } else if(command.contains("look")) {
            Look look = new Look(players);
            return look.run(currentPlayer);
        } else if(command.contains("health")){
            Health health = new Health();
            return health.run(currentPlayer);
        } else {
            Custom custom = new Custom(command, actions, locations);
            return custom.run(currentPlayer);
        }
    }*/

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
