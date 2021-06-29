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
    public String runCommand(Player currentPlayer) throws Exception {
        String locationName;
        //get location in command
        for(Element l : locations){
            if(command.contains(l.getName())
                    ||command.contains(l.getDescription())) {
                locationName = l.getName();
                //check if there is a path to the new location from the current
                checkPath(locationName, currentPlayer);
                //get the object for new location & set to player's location
                Location newLocation = (Location) subjectUtility.
                        getElement(locationName, locations);
                currentPlayer.setLocation(newLocation);
                Look look = new Look(players);
                return "You have moved to " + locationName + "\n"
                        + look.runCommand(currentPlayer);
            }
        }
        throw new Exception("Location in '"+command+"' does not exist");
    }

    private void checkPath(String location, Player player)
            throws Exception {
        ArrayList<String> paths = player.getLocation().getPaths();
        if(!paths.contains(location)){
            throw new Exception("There is not a path to '" +
                    location+"' from "+player.getLocation().getName());
        }
    }
}
