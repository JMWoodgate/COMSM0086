package com.company.DBCommand;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;
import com.company.DBExceptions.StorageType;

import java.util.ArrayList;

public class JoinCMD extends Parser implements DBCommand{

    private final ArrayList<String> command;
    private int index;
    private final StorageType type;
    private ArrayList<String> tableNames;
    private ArrayList<String> attributeNames;


    public JoinCMD(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        this.index = index;
        type = StorageType.TABLE;
        tableNames = new ArrayList<String>();
        attributeNames = new ArrayList<String>();
        if(command != null) {
            if (!parseJoin()) {
                throw new CommandException(
                        command.get(index), index, "table name");
            }
        }else{
            throw new EmptyData("JOIN command");
        }
    }

    //JOIN <TableName> AND <TableName> ON <AttributeName> AND <AttributeName>
    private boolean parseJoin() throws DBException{
        try {
            //get table names
            String firstTableName = parseTableName(command, index);
            tableNames.add(firstTableName);
            //point index to after tableName
            index+=2;
            checkNextToken(command.get(index), "and", index);
            String secondTableName = parseTableName(command, index);
            tableNames.add(secondTableName);
            //point index to after tableName
            index+=2;
            checkNextToken(command.get(index), "on", index);
            index++;
            //get attribute names
            String firstAttributeName = parseAttributeName(command, index);
            attributeNames.add(firstAttributeName);
            index++;
            checkNextToken(command.get(index), "and", index);
            index++;
            String secondAttributeName = parseAttributeName(command, index);
            attributeNames.add(secondAttributeName);
            //point index to end of command
            index++;
            return true;
        } catch(DBException e){
            throw new CommandException(command.get(index), index, "JOIN", e);
        }
    }

    public StorageType getType(){
        return type;
    }

    public ArrayList<String> getTableNames() throws EmptyData {
        if(tableNames!=null) {
            return tableNames;
        }
        throw new EmptyData("table names");
    }

    public ArrayList<String> getAttributeNames() throws EmptyData {
        if(attributeNames!=null) {
            return attributeNames;
        }
        throw new EmptyData("attribute name");
    }

    public void execute(){}

    public int getIndex(){
        return index;
    }
}

