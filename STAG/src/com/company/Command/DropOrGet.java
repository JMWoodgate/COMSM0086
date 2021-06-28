package com.company.Command;

import com.company.Element.Location;
import com.company.Element.Player;
import com.company.Element.Subject;
import com.company.Element.SubjectUtility;
import com.company.StagExceptions.ArtefactDoesNotExist;
import com.company.StagExceptions.StagException;

import java.util.ArrayList;

public class DropOrGet implements Command {

    private final String command;
    private ArrayList<Subject> listToAddTo;
    private ArrayList<Subject> listToRemoveFrom;
    private final Location location;

    public DropOrGet(String command, Location location){
        this.command = command;
        //if command is get, location will be null
        this.location = location;
        listToAddTo = new ArrayList<>();
        listToRemoveFrom = new ArrayList<>();
    }

    @Override
    public String run(Player player) throws StagException {
        SubjectUtility subjectUtility = new SubjectUtility();
        //set which list we are adding to/removing from
        setLists(player);
        for(Subject s : listToRemoveFrom){
            if(command.contains(s.getName()) ||
                    command.contains(s.getDescription())){
                subjectUtility.setSubject(s, listToAddTo);
                subjectUtility.removeSubject(s, listToRemoveFrom);
                //get drop or get message
                return getMessage(s);
            }
        } throw new ArtefactDoesNotExist(command);
    }

    private void setLists(Player player){
        //location will be null if command is get
        if(location == null){
            listToAddTo = player.getInventory();
            listToRemoveFrom = player.getLocation().getArtefacts();
        } else {
            listToAddTo = player.getLocation().getArtefacts();
            listToRemoveFrom = player.getInventory();
        }
    }

    private String getMessage(Subject subject){
        String message;
        if(location == null){
            message = "You picked up "+(subject.getDescription());
        }else{
            message = "You dropped "+subject.getDescription()
                    +" in "+location.getDescription();
        }
        return message;
    }
}
