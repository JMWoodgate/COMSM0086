package com.company;

import com.company.Command.Get;
import com.company.Command.Inventory;
import com.company.Command.Look;
import com.company.Subject.*;
import com.company.Subject.Character;
import com.company.Parsing.ActionsParser;
import com.company.Parsing.EntitiesParser;
import com.company.StagExceptions.*;

import java.util.*;

public class StagEngine {
    private final HashMap<String, Player> players;
    private final ArrayList<Location> locations;
    private final ArrayList<Action> actions;
    private Player currentPlayer;
    private Location playerLocation;

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
            return dropCommand(command);
        } else if(command.contains("goto")) {
            return gotoCommand(command);
        } else if(command.contains("look")) {
            Look look = new Look(players);
            return look.execute(currentPlayer);
        } else if(command.contains("health")){
            return currentPlayer.getName() + "'s current health is: "
                    + currentPlayer.getHealth() + "\n";
        } else {
            return customCommand(command);
        }
    }

    private String customCommand(String command) throws StagException{
        //loop through all of the actions we have read in from file
        for(Action a : actions){
            //get the action's triggers
            ArrayList<String> triggers = a.getTriggers();
            //if the command matches a trigger word of the action, check subjects,
            // consume, produce, then return narration
            for(String t : triggers) {
                if (command.contains(t)) {
                    //check all subjects exist in command and in game
                    checkCommand(command, a);
                    checkSubjects(a);
                    //check if anything to consume, if there is, remove from location/inventory
                    String message = consume(a);
                    //check if anything to produce, if there is, add to location
                    produce(a);
                    //return the action's narration - with message if health ran out
                    if(message!=null){
                        return a.getNarration()+"\n"+message;
                    } else{
                        return a.getNarration();
                    }
                }
            }
        }
        //if we get through all the actions and haven't found it, invalid command
        throw new InvalidCommand(command);
    }

    private void produce(Action action) throws SubjectDoesNotExist {
        ArrayList<String> produced = action.getProduced();
        //if there is nothing to produce by the action, we can skip this
        if(produced == null){
            return;
        }
        //loop through items produced and add them to the location
        for(String p : produced){
            if(p.equals("health")){
                currentPlayer.changeHealth(true);
            }else{
                moveSubject(p);
            }
        }
    }

    private void moveSubject(String subject)
            throws SubjectDoesNotExist {
        //need to check all locations for the artefact (not just unplaced)
        Location subjectLocation = (Location) getElement(subject, new ArrayList<>(locations));
        if(subjectLocation!=null){
            //need to add a path to the new location
            playerLocation.setPath(subject);
            return;
        }
        for(Location l : locations){
            //check if it is an artefact & move if so (function will return true)
            if(moveArtefact(l, subject)) {
                return;
            }
            //check if it is furniture & move if so
            if(moveFurniture(l, subject)) {
                return;
            }
            //check if it is a character & move if so
            if(moveCharacter(l, subject)){
                return;
            }
        }
        throw new SubjectDoesNotExist();
    }

    private boolean moveCharacter(Location locationToCheck, String subject){
        //get character from location
        Character character = (Character) getElement(
                subject, new ArrayList<>(locationToCheck.getCharacters()));
        if(character!=null){
            playerLocation.setCharacter(character.getName(), character.getDescription());
            locationToCheck.removeCharacter(character.getName());
            return true;
        }
        return false;
    }

    private boolean moveFurniture(Location locationToCheck, String subject){
        //get furniture from location
        Furniture furniture = (Furniture) getElement(
                subject, new ArrayList<>(locationToCheck.getFurniture()));
        if(furniture!=null){
            playerLocation.setFurniture(furniture.getName(), furniture.getDescription());
            locationToCheck.removeFurniture(furniture.getName());
            return true;
        }
        return false;
    }

    private boolean moveArtefact(Location locationToCheck, String subject){
        //get artefact from location
        Artefact artefact = (Artefact) getElement(
                subject, new ArrayList<>(locationToCheck.getArtefacts()));
        //check if the artefact to produce exists in this location
        if(artefact!=null){
            //create new artefact in the current location
            playerLocation.setArtefact(artefact.getName(),
                    artefact.getDescription());
            //remove artefact from old location
            locationToCheck.removeArtefact(artefact.getName());
            return true;
        }
        return false;
    }

    private String consume(Action action) throws SubjectDoesNotExist {
        String message = null;
        //check if subject is in player inventory or current player location
        ArrayList<String> consumed = action.getConsumed();
        //if there is nothing to consume, move on
        if (consumed == null) {
            return null;
        }
        for(String c : consumed) {
            if(c.equals("health")){
                message = consumeHealth();
            }
            //we need to check if the artefact, furniture or character is there -- not just artefact
            else if (getElement(c, new ArrayList<>(playerLocation.getArtefacts()))!=null) {
                //delete artefact from location
                playerLocation.removeArtefact(c);
            } else if (getElement(c, new ArrayList<>(currentPlayer.getInventory()))!=null){
                //delete artefact from player inventory
                currentPlayer.removeFromInventory(c);
            } else if (getElement(c, new ArrayList<>(playerLocation.getFurniture()))!=null){
                //delete furniture from location
                playerLocation.removeFurniture(c);
            } else if (getElement(c, new ArrayList<>(playerLocation.getCharacters()))!=null){
                //delete character from location
                playerLocation.removeCharacter(c);
            } else if(getElement(c, new ArrayList<>(locations))==null){
                //delete paths to location -> not the actual location
                locations.removeIf(l -> l.getName().equals(c));
            }else{
                //should be unreachable as we have already checked for this, but just in case
                //should we be checking for this twice..?
                throw new SubjectDoesNotExist();
            }
        }
        return message;
    }

    private String consumeHealth(){
        String message = null;
        currentPlayer.changeHealth(false);
        //if health is zero, need to drop all items in inventory
        if(currentPlayer.getHealth()==0){
            //get player inventory
            ArrayList<Artefact> inventory = currentPlayer.getInventory();
            //if there are items in inventory, place them in the current location
            emptyInventory(inventory);
            //return player to start
            currentPlayer.setLocation(locations.get(0));
            playerLocation = currentPlayer.getLocation();
            currentPlayer.resetHealth();
            //return message
            message = "You ran out of health! You have lost your inventory and been returned to the start.";
        }
        return message;
    }

    private void emptyInventory(ArrayList<Artefact> inventory){
        while (!inventory.isEmpty()) {
            Artefact a = inventory.get(0);
            //put each artefact in the location
            playerLocation.setArtefact(a.getName(), a.getDescription());
            //remove from the player's inventory
            currentPlayer.removeFromInventory(a);
        }
    }

    private void checkCommand(String command, Action action) throws SubjectDoesNotExist {
        //need to check that at least one subject is present in the command (if required)
        if(action.getSubjects()!=null) {
            if (!checkCommandSubjects(command, action)) {
                throw new SubjectDoesNotExist();
            }
        }else{
            throw new SubjectDoesNotExist();
        }
    }

    private boolean checkCommandSubjects(String command, Action a){
        ArrayList<String> subjects = a.getSubjects();
        for(String s : subjects){
            if(command.contains(s)){
                return true;
            }
        }
        return false;
    }

    private void checkSubjects(Action action) throws SubjectDoesNotExist {
        ArrayList<String> subjects = action.getSubjects();
        for(String s : subjects){
            //check in play inventory
            if(getElement(s, new ArrayList<>(currentPlayer.getInventory()))==null){
                //check in location artefacts
                if(getElement(s, new ArrayList<>(playerLocation.getArtefacts()))==null){
                    //check in location furniture
                    if(getElement(s, new ArrayList<>(playerLocation.getFurniture()))==null){
                        //check in location characters
                        if(getElement(s, new ArrayList<>(playerLocation.getCharacters()))==null){
                            //check in locations
                            if(getElement(s, new ArrayList<>(locations))==null){
                                //if the subject is in none of these, throw error
                                throw new SubjectDoesNotExist();
                            }
                        }
                    }
                }
            }
        }
    }

    public Subject getElement(String elementName, ArrayList<Subject> subjectList){
        if(subjectList == null){
            return null;
        }
        for(Subject e : subjectList){
            if(e!=null && e.getName().equals(elementName)){
                return e;
            }
        }
        return null;
    }

    /*private String lookCommand(){
        //need to return a string that describes the whole location
        StringBuilder stringBuilder = new StringBuilder();
        //get location name/description
        stringBuilder.append("You are in ").append(playerLocation.getDescription()).append(". ");
        //list artefacts in the location
        stringBuilder.append("You can see:\n");
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
        //list other players in location
        for(Map.Entry<String, Player> set : players.entrySet()){
            Player checkPlayer = set.getValue();
            String checkName = checkPlayer.getName();
            String checkLocation = checkPlayer.getLocation().getName();
            String currentName = currentPlayer.getName();
            String currentLocation = playerLocation.getName();
            if(checkLocation.equals(currentLocation)&&!checkName.equals(currentName)){
                stringBuilder.append(checkPlayer.getName()).append("\n");
            }
        }
        stringBuilder.append("You can access from here:\n");
        ArrayList<String> paths = playerLocation.getPaths();
        for(String p : paths){
            stringBuilder.append(p).append("\n");
        }
        return stringBuilder.toString();
    }*/

    private String gotoCommand(String command) throws LocationDoesNotExist{
        String newLocation;
        //get location
        for(Location l : locations){
            if(command.contains(l.getName())||command.contains(l.getDescription())) {
                newLocation = l.getName();
                //get the object for the new location and set it to the current player's location
                currentPlayer.setLocation(getSpecificLocation(newLocation));
                playerLocation = currentPlayer.getLocation();
                Look look = new Look(players);
                return "You have moved to " + newLocation + "\n" + look.execute(currentPlayer);
            }
        }
        throw new LocationDoesNotExist(command);
    }

    private Location getSpecificLocation(String newLocation) throws LocationDoesNotExist {
        for(Location l : locations){
            if(l.getName().equals(newLocation)){
                return l;
            }
        } throw new LocationDoesNotExist(newLocation);
    }

    private String dropCommand(String command) throws ArtefactDoesNotExist{
        ArrayList<Artefact> inventory = currentPlayer.getInventory();
        for(Artefact a : inventory){
            if(command.contains(a.getName()) || command.contains(a.getDescription())){
                playerLocation.setArtefact(a.getName(), a.getDescription());
                String message = "You dropped "+a.getDescription()+" in "+playerLocation.getName();
                currentPlayer.removeFromInventory(a);
                return message;
            }
        } throw new ArtefactDoesNotExist(command);
    }

    /*private String getCommand(String command) throws ArtefactDoesNotExist {
        ArrayList<Artefact> locationArtefacts = playerLocation.getArtefacts();
        for(Artefact a : locationArtefacts){
            if(command.contains(a.getName()) || command.contains(a.getDescription())){
                currentPlayer.addToInventory(a);
                String message = "You picked up "+(a.getDescription());
                playerLocation.removeArtefact(a);
                return message;
            }
        }
        throw new ArtefactDoesNotExist(command);
    }*/

    /*private String listInventory(){
        ArrayList<Artefact> artefacts = currentPlayer.getInventory();
        StringBuilder inventory = new StringBuilder();
        for(Artefact a : artefacts){
            inventory.append(a.getDescription());
            inventory.append("\n");
        }
        return (inventory.toString());
    }*/

    public void changePlayer(String playerName){
        currentPlayer = players.get(playerName);
        playerLocation = currentPlayer.getLocation();
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
        playerLocation = currentPlayer.getLocation();
    }

}
