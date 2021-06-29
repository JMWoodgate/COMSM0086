package com.company.Command;

import com.company.Element.Location;
import com.company.Element.Player;
import com.company.Element.Subject;
import com.company.Element.SubjectUtility;

import java.util.ArrayList;

public class DropOrGet implements Command {

    private final String command;
    private ArrayList<Subject> listToAddTo;
    private ArrayList<Subject> listToRemoveFrom;
    private final Location location;

    public DropOrGet(String command, Location location){
        this.command = command;
        this.location = location;
        listToAddTo = new ArrayList<>();
        listToRemoveFrom = new ArrayList<>();
    }

    @Override
    public String runCommand(Player currentPlayer) throws Exception {
        SubjectUtility subjectUtility = new SubjectUtility();
        //set which list we are adding to/removing from
        setLists(currentPlayer);
        for(Subject s : listToRemoveFrom){
            if(command.contains(s.getName()) ||
                    command.contains(s.getDescription())){
                subjectUtility.setSubject(s, listToAddTo);
                subjectUtility.removeSubject(s, listToRemoveFrom);
                //make drop or get message to return
                return getMessage(s);
            }
        } throw new Exception("Artefact in '"+command+"' does not exist");
    }

    private void setLists(Player currentPlayer){
        //location will be null if command is get
        if(location == null){
            //if command is get we add to inventory & remove from location
            listToAddTo = currentPlayer.getInventory();
            listToRemoveFrom = currentPlayer.getLocation().getArtefacts();
        } else {
            //we do the opposite to get if command is drop
            listToAddTo = currentPlayer.getLocation().getArtefacts();
            listToRemoveFrom = currentPlayer.getInventory();
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
