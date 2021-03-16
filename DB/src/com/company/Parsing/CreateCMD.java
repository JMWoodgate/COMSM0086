package com.company.Parsing;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.EmptyData;
import com.company.DBExceptions.StorageType;

import java.util.ArrayList;

public class CreateCMD extends Parser implements DBCommand {

    private final ArrayList<String> command;
    private int index;
    private StorageType type;
    private String tableName;
    private String databaseName;

    public CreateCMD(ArrayList<String> command, int index) throws CommandException {
        this.command = command;
        this.index = index;
        if(!parseCreate()){
            throw new CommandException(
                    command.get(index), index, "table, database or attribute list");
        }
    }

    public void execute(){}

    private boolean parseCreate() throws CommandException {
        index++;
        String nextCommand = command.get(index);
        switch(nextCommand){
            case ("database"):
                type = StorageType.DATABASE;
                //need to call Create Database
                break;
            case ("table"):
                type = StorageType.TABLE;
                if(!createTable()){
                    throw new CommandException(
                            command.get(index), index, "create table");
                }
                break;
            default:
                return false;
        }
        return true;
    }

    private boolean createDatabase(){
        index++;
        String nextCommand = command.get(index);
        if(isAlphaNumerical(nextCommand)) {
            databaseName = nextCommand;
            return true;
        }
        return false;
    }

    private boolean createTable(){
        index++;
        String nextCommand = command.get(index);
        if(isAlphaNumerical(nextCommand)) {
            tableName = nextCommand;
            index++;
            if(nextCommand.equals(";")){
                return true;
            }
            else if(nextCommand.equals("(")){
                //call attributeList
                return true;
            }
        }
        return false;
    }

    public StorageType getType(){
        return type;
    }

    public int getIndex(){
        return index;
    }
}
