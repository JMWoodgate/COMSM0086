package com.company.Parsing;

import com.company.DBExceptions.CommandException;

import java.util.ArrayList;

public class UseCMD {

    private final ArrayList<String> command;
    private int index;
    private String databaseName;

    public UseCMD(ArrayList<String> command, int index) throws CommandException {
        this.command = command;
        this.index = index;
        if(!parseUse()){
            index++;
            throw new CommandException(
                    command.get(index), index, "database name");
        }
    }

    private boolean parseUse(){
        String nextCommand = command.get(index+1);
        for(int i = 0; i < nextCommand.length(); i++){
            if(!Character.isLetterOrDigit(nextCommand.charAt(i))){
                return false;
            }
        }
        index++;
        return true;
    }

    public int getIndex(){
        return index;
    }
}
