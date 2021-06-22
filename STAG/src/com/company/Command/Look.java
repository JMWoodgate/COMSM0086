package com.company.Command;

import com.company.Subject.*;
import com.company.Subject.Character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Look implements Command{

    HashMap<String, Player> players;
    Player player;
    Location playerLocation;

    public Look(HashMap<String, Player> players){
        this.players = players;
    }

    @Override
    public String run(Player player) {
        this.player = player;
        playerLocation = player.getLocation();
        //need to return a string that describes the whole location
        StringBuilder stringBuilder = new StringBuilder();
        //get location name/description
        stringBuilder.append("You are in ").append(playerLocation.getDescription()).append(". ");
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
    }

    private void listPlayers(StringBuilder stringBuilder){
        //list other players in location
        for(Map.Entry<String, Player> set : players.entrySet()){
            Player checkPlayer = set.getValue();
            String checkName = checkPlayer.getName();
            String checkLocation = checkPlayer.getLocation().getName();
            String currentName = player.getName();
            String currentLocation = playerLocation.getName();
            if(checkLocation.equals(currentLocation)&&!checkName.equals(currentName)){
                stringBuilder.append(checkPlayer.getName()).append("\n");
            }
        }
    }
}
