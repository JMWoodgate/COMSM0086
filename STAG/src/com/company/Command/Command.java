package com.company.Command;

import com.company.StagExceptions.StagException;
import com.company.Subject.Player;

public interface Command {
    String execute(Player player) throws StagException;
}
