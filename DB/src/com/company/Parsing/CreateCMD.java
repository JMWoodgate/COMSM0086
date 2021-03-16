package com.company.Parsing;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.StorageType;

import java.util.ArrayList;

public class CreateCMD extends Parser implements DBCommand {

    private final ArrayList<String> command;
    private int index;
    private StorageType type;
    private String tableName;

    public CreateCMD(ArrayList<String> command, int index) throws CommandException {
        this.command = command;
        this.index = index;
        if(!parseCreate()){
            throw new CommandException(
                    command.get(index), index, "table, database or attribute list");
        }
    }

    public void execute(){}

    private boolean parseCreate(){
        index++;
        String nextCommand = command.get(index);
        switch(nextCommand){
            case ("database"):
                type = StorageType.DATABASE;
                //need to call Create Database
                break;
            case ("table"):
                type = StorageType.TABLE;
                //call Create Table
                break;
            default:
                return false;
        }
        index++;
        nextCommand = command.get(index);
        switch(nextCommand){
            case(";"):
                break;
            case("("):
                //need to call AttributeList
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean createTable(){
        index++;
        String nextCommand = command.get(index);
        if(isAlphaNumerical(nextCommand)) {
            return true;
        }
        return true;
    }

    public StorageType getType(){
        return type;
    }

    public int getIndex(){
        return index;
    }
}
