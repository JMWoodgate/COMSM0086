package com.company.Command;

import com.company.Element.Player;

public class Health implements Command{

    public Health(){}

    @Override
    public String runCommand(Player currentPlayer) {
        return currentPlayer.getName() + "'s current health is: "
                + currentPlayer.getHealth() + "\n";
    }
}
