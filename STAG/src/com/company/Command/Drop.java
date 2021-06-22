package com.company.Command;

import com.company.StagExceptions.ArtefactDoesNotExist;
import com.company.Subject.Artefact;
import com.company.Subject.Location;
import com.company.Subject.Player;

import java.util.ArrayList;

public class Drop implements Command {

    String command;

    public Drop(String command){
        this.command = command;
    }

    @Override
    public String run(Player player) throws ArtefactDoesNotExist {
        Location playerLocation = player.getLocation();
        ArrayList<Artefact> inventory = player.getInventory();
        for(Artefact a : inventory){
            if(command.contains(a.getName()) ||
                    command.contains(a.getDescription())){
                playerLocation.setArtefact(a.getName(), a.getDescription());
                String message = "You dropped "+a.getDescription()
                        +" in "+playerLocation.getName();
                player.removeFromInventory(a);
                return message;
            }
        } throw new ArtefactDoesNotExist(command);
    }
}
