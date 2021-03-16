package com.company.Parsing;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.StorageType;

import java.util.ArrayList;

public class CreateCMD {

    private final ArrayList<String> command;
    private int index;
    private StorageType type;

    public CreateCMD(ArrayList<String> command, int index) throws CommandException {
        this.command = command;
        this.index = index;
        if(!parseCreate()){
            index++;
            throw new CommandException(
                    command.get(index), index, "table or database");
        }
    }

    private boolean parseCreate(){
        String nextCommand = command.get(index+1);
        switch(nextCommand){
            case ("database"):
                type = StorageType.DATABASE;
                index++;
                break;
            case ("table"):
                type = StorageType.TABLE;
                index++;
                break;
            default:
                return false;
        }
        return true;
    }

}
