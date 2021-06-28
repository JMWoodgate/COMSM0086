package com.company.Command;

import com.company.Element.Player;

public class Health implements Command{

    public Health(){}

    @Override
    public String runCommand(Player player) {
        return player.getName() + "'s current health is: "
                + player.getHealth() + "\n";
    }
}
