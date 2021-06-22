package com.company.Command;

import com.company.StagExceptions.ArtefactDoesNotExist;
import com.company.Subject.Artefact;
import com.company.Subject.Location;
import com.company.Subject.Player;

import java.util.ArrayList;

public class Get implements Command{

    String command;

    public Get(String command){
        this.command = command;
    }

    @Override
    public String run(Player player) throws ArtefactDoesNotExist {
        Location playerLocation = player.getLocation();
        ArrayList<Artefact> locationArtefacts = playerLocation.getArtefacts();
        for(Artefact a : locationArtefacts){
            if(command.contains(a.getName()) || command.contains(a.getDescription())){
                player.addToInventory(a);
                String message = "You picked up "+(a.getDescription());
                playerLocation.removeArtefact(a);
                return message;
            }
        }
        throw new ArtefactDoesNotExist(command);
    }
}
