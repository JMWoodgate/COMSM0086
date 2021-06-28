package com.company.Command;

import com.company.Element.Player;

public interface Command {
    String run(Player player) throws Exception;
}
