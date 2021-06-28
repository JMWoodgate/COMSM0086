package com.company.Command;

import com.company.Element.Player;

public interface Command {
    String runCommand(Player player) throws Exception;
}
