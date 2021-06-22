package com.company.Command;

import com.company.StagExceptions.StagException;
import com.company.Subject.Player;

public interface Command {
    String run(Player player) throws StagException;
}
