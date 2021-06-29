package com.company.Command;

import com.company.Element.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Look implements Command{

    private final HashMap<String, Player> players;
    private Player currentPlayer;
    private Location playerLocation;

    public Look(HashMap<String, Player> players){
        this.players = players;
    }

    @Override
    public String runCommand(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        playerLocation = currentPlayer.getLocation();
        //need to return a string that describes the whole location
        StringBuilder stringBuilder = new StringBuilder();
        //get location name/description
        stringBuilder.append("You are in ")
                .append(playerLocation.getDescription()).append(". ");
        //list subjects (& players) in the location
        stringBuilder.append("You can see:\n");
        listSubjects(stringBuilder);
        listPlayers(stringBuilder);
        stringBuilder.append("You can access from here:\n");
        ArrayList<String> paths = playerLocation.getPaths();
        for(String p : paths){
            stringBuilder.append(p).append("\n");
        }
        return stringBuilder.toString();
    }

    private void listSubjects(StringBuilder stringBuilder){
        //list artefacts in location
        for(Subject s : playerLocation.getArtefacts()){
            stringBuilder.append(s.getDescription()).append("\n");
        }
        //list furniture in location
        for(Subject s : playerLocation.getFurniture()){
            stringBuilder.append(s.getDescription()).append("\n");
        }
        //list characters in location
        for(Subject s : playerLocation.getCharacters()){
            stringBuilder.append(s.getDescription()).append(("\n"));
        }
    }

    private void listPlayers(StringBuilder stringBuilder){
        //list other players in location
        for(Map.Entry<String, Player> set : players.entrySet()){
            Player comparePlayer = set.getValue();
            String compareName = comparePlayer.getName();
            String compareLocation = comparePlayer.getLocation().getName();
            String currentName = currentPlayer.getName();
            String currentLocation = playerLocation.getName();
            //make sure we are adding everyone but the current player
            if(compareLocation.equals(currentLocation)&&
                    !compareName.equals(currentName)){
                stringBuilder.append(comparePlayer.getName()).append("\n");
            }
        }
    }
}
