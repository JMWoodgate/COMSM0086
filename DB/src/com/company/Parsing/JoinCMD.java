package com.company.Parsing;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;
import com.company.DBExceptions.StorageType;

import java.util.ArrayList;

public class JoinCMD extends Parser implements DBCommand{

    private final ArrayList<String> command;
    private int index;
    private final StorageType type;
    private String firstTableName;
    private String secondTableName;

    public JoinCMD(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        this.index = index;
        type = StorageType.TABLE;
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
            firstTableName = parseTableName(command, index);
            //point index to after tableName
            index+=2;
            String nextToken = command.get(index);
            checkNextToken(nextToken, "and", index);
            secondTableName = parseTableName(command, index);
            //point index to after tableName
            index+=2;
            nextToken = command.get(index);
            checkNextToken(nextToken, "on", index);
            //attributeName
            index++;
            nextToken = command.get(index);
            checkNextToken(nextToken, "and", index);
            //attributeName
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

    public String getSecondTableName() throws EmptyData {
        if(secondTableName!=null) {
            return secondTableName;
        }
        throw new EmptyData("second table name");
    }

    public String getFirstTableName() throws EmptyData {
        if(firstTableName!=null) {
            return firstTableName;
        }
        throw new EmptyData("first table name");
    }

    public void execute(){}

    public int getIndex(){
        return index;
    }
}

