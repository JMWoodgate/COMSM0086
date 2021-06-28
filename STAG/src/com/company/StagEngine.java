package com.company;

import com.company.Command.*;
import com.company.Element.*;
import com.company.Parsing.ActionsParser;
import com.company.Parsing.EntitiesParser;
import com.company.StagExceptions.*;

import java.util.*;

public class StagEngine {
    private final HashMap<String, Player> players;
    private final ArrayList<Element> locations;
    private final ArrayList<Action> actions;
    private Player currentPlayer;

    public StagEngine(String entityFilename, String actionFilename){
        //parse files and get data
        EntitiesParser entitiesParser = new EntitiesParser(entityFilename);
        locations = new ArrayList<>(entitiesParser.getLocations());
        ActionsParser actionsParser = new ActionsParser(actionFilename);
        actions = actionsParser.getActions();
        players = new HashMap<>();
    }

    public String execute(String commandString) throws StagException {
        //get which type of command it is
        Command command = interpretCommand(commandString);
        //run the command & return the response to print to console
        return command.run(currentPlayer);
    }

    private Command interpretCommand(String command) {
        command = command.toLowerCase(Locale.ROOT);
        if(command.contains("inv")) {
            return new Inventory();
        } else if(command.contains("get")) {
            return new DropOrGet(command, null);
        } else if(command.contains("drop")) {
            return new DropOrGet(command, currentPlayer.getLocation());
        } else if(command.contains("goto")) {
            return new GoTo(command, locations, players);
        } else if(command.contains("look")) {
            return new Look(players);
        } else if(command.contains("health")){
            return new Health();
        } else {
            return new Custom(command, actions, locations, players);
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
        newPlayer.setLocation((Location)locations.get(0));
        players.put(playerName, newPlayer);
        currentPlayer = newPlayer;
    }

}
