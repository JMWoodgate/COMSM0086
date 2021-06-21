package com.company.Command;

import com.company.StagExceptions.StagException;
import com.company.Subject.Player;

public class Health implements Command{

    public Health(){}

    @Override
    public String execute(Player player) throws StagException {
        return player.getName() + "'s current health is: "
                + player.getHealth() + "\n";
    }
}
