package com.company.Command;

import com.company.StagExceptions.ArtefactDoesNotExist;
import com.company.Subject.Artefact;
import com.company.Subject.Location;
import com.company.Subject.Player;
import com.company.Subject.Subject;

import java.util.ArrayList;

public class Drop implements Command {

    String command;

    public Drop(String command){
        this.command = command;
    }

    @Override
    public String run(Player player) throws ArtefactDoesNotExist {
        Location playerLocation = player.getLocation();
        ArrayList<Subject> inventory = player.getInventory();
        for(Subject s : inventory){
            if(command.contains(s.getName()) ||
                    command.contains(s.getDescription())){
                playerLocation.setArtefact(s.getName(), s.getDescription());
                String message = "You dropped "+s.getDescription()
                        +" in "+playerLocation.getName();
                player.removeFromInventory(s);
                return message;
            }
        } throw new ArtefactDoesNotExist(command);
    }
}
