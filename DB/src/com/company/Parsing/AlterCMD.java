package com.company.Parsing;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;
import com.company.DBExceptions.StorageType;

import java.util.ArrayList;

public class AlterCMD extends Parser implements DBCommand{

    private final ArrayList<String> command;
    private int index;
    private final StorageType type;
    private String tableName;
    private String attributeName;

    public AlterCMD(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        this.index = index;
        type = StorageType.TABLE;
        if(command != null) {
            if (!parseAlter()) {
                throw new CommandException(
                        command.get(index), index, "database or table name");
            }
        }else{
            throw new EmptyData("DROP command");
        }
    }

    //ALTER TABLE <TableName> <AlterationType> <AttributeName>
    //AlterationType <ADD> <DROP>
    private boolean parseAlter() throws DBException{
        try {
            index++;
            String nextToken = command.get(index);
            if (!nextToken.equals("table")) {
                throw new CommandException(nextToken, index, "table");
            }
            index++;
            nextToken = command.get(index);
            tableName = parseTableName(command, index);
            index++;
            nextToken = command.get(index);
            switch (nextToken) {
                case ("add"):
                case ("drop"):
                    attributeName = parseAttributeName(command, index);
                    index += 2;
                    break;
                default:
                    return false;
            }
            return true;
        } catch(DBException e){
            throw new CommandException(command.get(index), index, "alter");
        }
    }

    public String getAttributeName() throws EmptyData{
        if(attributeName!=null){
            return attributeName;
        }
        throw new EmptyData("attribute name");
    }

    public StorageType getType(){
        return type;
    }

    public String getTableName() throws EmptyData {
        if(tableName!=null) {
            return tableName;
        }
        throw new EmptyData("table name");
    }

    public void execute(){}

    public int getIndex(){
        return index;
    }
}
