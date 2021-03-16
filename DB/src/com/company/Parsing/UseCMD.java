package com.company.Parsing;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.EmptyData;

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
                    //expected database name
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
        databaseName = nextCommand;
        index++;
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
