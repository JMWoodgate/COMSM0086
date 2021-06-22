package com.company.Command;

import com.company.Action;
import com.company.StagExceptions.InvalidCommand;
import com.company.StagExceptions.StagException;
import com.company.StagExceptions.SubjectDoesNotExist;
import com.company.Subject.*;

import java.util.ArrayList;

public class Custom implements Command{

    String command;
    ArrayList<Action> actions;
    Location playerLocation;
    Player player;
    ArrayList<Location> locations;

    public Custom(String command, ArrayList<Action> actions, ArrayList<Location> locations){
        this.command = command;
        this.actions = actions;
        this.locations = locations;
    }

    @Override
    public String run(Player player) throws StagException {
        playerLocation = player.getLocation();
        this.player = player;
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
                    Consume consume = new Consume(a, locations);
                    String message = consume.run(player);
                    //check if anything to produce, if there is, add to location
                    Produce produce = new Produce(a, locations);
                    produce.run(player);
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
            if(getElement(s, new ArrayList<>(player.getInventory()))==null){
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

}
