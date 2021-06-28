package com.company.Command;

import com.company.Element.*;

import java.util.ArrayList;
import java.util.HashMap;

public class GoTo implements Command{

    private final String command;
    private final ArrayList<Element> locations;
    private final HashMap<String, Player> players;
    private final SubjectUtility subjectUtility;

    public GoTo(String command, ArrayList<Element> locations,
                HashMap<String, Player> players){
        this.command = command;
        this.locations = locations;
        this.players = players;
        subjectUtility = new SubjectUtility();
    }

    @Override
    public String run(Player player) throws Exception {
        String locationName;
        //get location
        for(Element l : locations){
            if(command.contains(l.getName())
                    ||command.contains(l.getDescription())) {
                locationName = l.getName();
                //get the object for new location & set to player's location
                Location newLocation = (Location) subjectUtility.
                        getElement(locationName, locations);
                player.setLocation(newLocation);
                Look look = new Look(players);
                return "You have moved to " + locationName + "\n"
                        + look.run(player);
            }
        }
        throw new Exception("Location in '"+command+"' does not exist");
    }
}
