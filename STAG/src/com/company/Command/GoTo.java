package com.company.Command;

import com.company.StagExceptions.LocationDoesNotExist;
import com.company.Subject.Location;
import com.company.Subject.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class GoTo implements Command{

    String command;
    ArrayList<Location> locations;
    HashMap<String, Player> players;

    public GoTo(String command, ArrayList<Location> locations,
                HashMap<String, Player> players){
        this.command = command;
        this.locations = locations;
        this.players = players;
    }

    @Override
    public String run(Player player) throws LocationDoesNotExist {
        String newLocation;
        //get location
        for(Location l : locations){
            if(command.contains(l.getName())
                    ||command.contains(l.getDescription())) {
                newLocation = l.getName();
                //get the object for new location & set to player's location
                player.setLocation(getLocation(newLocation));
                Look look = new Look(players);
                return "You have moved to " + newLocation + "\n"
                        + look.run(player);
            }
        }
        throw new LocationDoesNotExist(command);
    }

    private Location getLocation(String newLocation)
            throws LocationDoesNotExist {
        for(Location l : locations){
            if(l.getName().equals(newLocation)){
                return l;
            }
        } throw new LocationDoesNotExist(newLocation);
    }
}
