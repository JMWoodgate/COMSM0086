package com.company.Parsing;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.EmptyData;

import java.util.ArrayList;

public class UseCMD extends Parser implements DBCommand{

    private final ArrayList<String> command;
    private int index;
    private String databaseName;

    public UseCMD(ArrayList<String> command, int index) throws CommandException {
        this.command = command;
        this.index = index;
        if(!parseUse()){
            throw new CommandException(
                    //expected database name
                    command.get(index), index, "database name");
        }
    }

    public void execute(){}

    private boolean parseUse(){
        index++;
        String nextToken = command.get(index);
        if(!isAlphaNumerical(nextToken)){
            return false;
        }
        databaseName = nextToken;
        return true;
    }

}
