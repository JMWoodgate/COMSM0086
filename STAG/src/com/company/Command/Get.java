package com.company.Command;

import com.company.Element.SubjectUtility;
import com.company.StagExceptions.ArtefactDoesNotExist;
import com.company.Element.Location;
import com.company.Element.Player;
import com.company.Element.Subject;

import java.util.ArrayList;

public class Get implements Command{

    private final String command;
    private final SubjectUtility subjectUtility;

    public Get(String command){
        this.command = command;
        subjectUtility = new SubjectUtility();
    }

    //very similar to drop
    @Override
    public String run(Player player) throws ArtefactDoesNotExist {
        Location playerLocation = player.getLocation();
        ArrayList<Subject> locationArtefacts = playerLocation.getArtefacts();
        for(Subject s : locationArtefacts){
            if(command.contains(s.getName())
                    || command.contains(s.getDescription())){
                subjectUtility.setSubject(s, player.getInventory(), null);
                String message = "You picked up "+(s.getDescription());
                subjectUtility.removeSubject(s, player.getLocation().getArtefacts());
                return message;
            }
        }
        throw new ArtefactDoesNotExist(command);
    }
}
