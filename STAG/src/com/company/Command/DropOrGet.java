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
    private final SubjectUtility subjectUtility;
    private final ArrayList<Subject> listToAddTo;
    private final ArrayList<Subject> listToRemoveFrom;
    private final Location location;

    public DropOrGet(String command, ArrayList<Subject> listToAddTo,
                     ArrayList<Subject> listToRemoveFrom,Location location){
        this.command = command;
        subjectUtility = new SubjectUtility();
        this.listToAddTo = listToAddTo;
        this.listToRemoveFrom = listToRemoveFrom;
        //if command is get, location will be null
        this.location = location;
    }

    @Override
    public String run(Player player) throws StagException {
        for(Subject s : listToRemoveFrom){
            if(command.contains(s.getName()) ||
                    command.contains(s.getDescription())){
                subjectUtility.setSubject(s, listToAddTo,
                        //location will be null if command is get
                        location);
                String message = s.getDescription()+" was moved.";
                subjectUtility.removeSubject(s, listToRemoveFrom);
                return message;
            }
        } throw new ArtefactDoesNotExist(command);
    }
}
