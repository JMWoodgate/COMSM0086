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

    //CREATE <create Database> || <create Table>
    private boolean parseCreate() throws CommandException {
        index++;
        String nextToken = command.get(index);
        switch(nextToken){
            case ("database"):
                type = StorageType.DATABASE;
                if(!parseCreateDatabase()){
                    throw new CommandException(
                            command.get(index), index, "create database");
                }
                break;
            case ("table"):
                type = StorageType.TABLE;
                if(!parseCreateTable()){
                    throw new CommandException(
                            command.get(index), index, "create table");
                }
                break;
            default:
                return false;
        }
        return true;
    }

    //CREATE Database <Database Name>
    private boolean parseCreateDatabase(){
        index++;
        String nextToken = command.get(index);
        if(isAlphaNumerical(nextToken)) {
            //getting the database name
            databaseName = nextToken;
            return true;
        }
        return false;
    }

    //CREATE Table <Table Name> || <Table Name> (<Attribute List>)
    private boolean parseCreateTable(){
        index++;
        String nextToken = command.get(index);
        if(isAlphaNumerical(nextToken)) {
            //getting the table name
            tableName = nextToken;
            index++;
            if(nextToken.equals(";")){
                //end of statement
                return true;
            }
            else if(nextToken.equals("(")){
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
