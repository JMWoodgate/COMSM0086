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
        String nextCommand = command.get(index);
        if(!isAlphaNumerical(nextCommand)){
            return false;
        }
        databaseName = nextCommand;
        return true;
    }

    public String getDatabaseName() throws EmptyData {
        if(databaseName!=null) {
            return databaseName;
        }
        else{
            throw new EmptyData("getDatabaseName");
        }
    }

    public int getIndex(){
        return index;
    }
}
