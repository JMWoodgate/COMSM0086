package com.company.Command;

import com.company.Subject.Artefact;
import com.company.Subject.Player;

import java.util.ArrayList;

public class Inventory implements Command{

    public Inventory(){}

    @Override
    public String run(Player player) {
        ArrayList<Artefact> artefacts = player.getInventory();
        StringBuilder inventory = new StringBuilder();
        for(Artefact a : artefacts){
            inventory.append(a.getDescription());
            inventory.append("\n");
        }
        return (inventory.toString());
    }
}
