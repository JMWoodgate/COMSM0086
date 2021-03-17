package com.company.Parsing;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;
import com.company.DBExceptions.StorageType;

import java.util.ArrayList;

public class SelectCMD extends Parser implements DBCommand{

    private final ArrayList<String> command;
    private int index;
    private final StorageType type;
    private String tableName;

    public SelectCMD(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        this.index = index;
        type = StorageType.TABLE;
        if(command != null) {
            if (!parseSelect()) {
                throw new CommandException(
                        command.get(index), index, "table name");
            }
        }else{
            throw new EmptyData("ALTER command");
        }
    }

    //INSERT INTO <TableName> VALUES (<ValueList>)
    private boolean parseSelect() throws DBException{
        try {
            index++;
            String nextToken = command.get(index);
            if (!nextToken.equals("into")) {
                throw new CommandException(nextToken, index, "into");
            }
            return true;
        } catch(DBException e){
            throw new CommandException(command.get(index), index, "insert");
        }
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