package com.company.Command;

import com.company.StagExceptions.ArtefactDoesNotExist;
import com.company.Element.Location;
import com.company.Element.Player;
import com.company.Element.Subject;

import java.util.ArrayList;

public class Get implements Command{

    private final String command;
    private final Subject subjectUtility;

    public Get(String command){
        this.command = command;
        subjectUtility = new Subject();
    }

    @Override
    public String run(Player player) throws ArtefactDoesNotExist {
        Location playerLocation = player.getLocation();
        ArrayList<Subject> locationArtefacts = playerLocation.getArtefacts();
        for(Subject s : locationArtefacts){
            if(command.contains(s.getName())
                    || command.contains(s.getDescription())){
                subjectUtility.setSubject(
                        s.getName(), s.getDescription(), player.getInventory());
                String message = "You picked up "+(s.getDescription());
                subjectUtility.removeSubject(s, player.getLocation().getArtefacts());
                return message;
            }
        }
        throw new ArtefactDoesNotExist(command);
    }
}
