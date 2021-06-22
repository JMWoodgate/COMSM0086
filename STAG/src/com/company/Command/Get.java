package com.company.Command;

import com.company.StagExceptions.ArtefactDoesNotExist;
import com.company.Subject.Artefact;
import com.company.Subject.Location;
import com.company.Subject.Player;
import com.company.Subject.Subject;

import java.util.ArrayList;

public class Get implements Command{

    String command;

    public Get(String command){
        this.command = command;
    }

    @Override
    public String run(Player player) throws ArtefactDoesNotExist {
        Location playerLocation = player.getLocation();
        ArrayList<Subject> locationArtefacts = playerLocation.getArtefacts();
        for(Subject s : locationArtefacts){
            if(command.contains(s.getName())
                    || command.contains(s.getDescription())){
                player.addToInventory(s);
                String message = "You picked up "+(s.getDescription());
                playerLocation.removeSubject(s, player.getLocation().getArtefacts());
                return message;
            }
        }
        throw new ArtefactDoesNotExist(command);
    }
}
