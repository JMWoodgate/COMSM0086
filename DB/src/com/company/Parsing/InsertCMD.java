package com.company.Parsing;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;
import com.company.DBExceptions.StorageType;

import java.util.ArrayList;

public class InsertCMD extends Parser implements DBCommand{

    private final ArrayList<String> command;
    private int index;
    private final StorageType type;
    private String tableName;

    public InsertCMD(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        this.index = index;
        type = StorageType.TABLE;
        if(command != null) {
            if (!parseInsert()) {
                throw new CommandException(
                        command.get(index), index, "table name");
            }
        }else{
            throw new EmptyData("INSERT command");
        }
    }

    //INSERT INTO <TableName> VALUES (<ValueList>)
    private boolean parseInsert() throws DBException{
        try {
            index++;
            String nextToken = command.get(index);
            if (!nextToken.equals("into")) {
                throw new CommandException(nextToken, index, "into");
            }
            tableName = parseTableName(command, index);
            //increasing index to point to after the table name
            index+=2;
            nextToken = command.get(index);
            if (!nextToken.equals("values")) {
                throw new CommandException(nextToken, index, "values");
            }
            index++;
            nextToken = command.get(index);
            if (!nextToken.equals("(")) {
                throw new CommandException(nextToken, index, "values");
            }
            //call ValueList
            //get index to end of command ; (hack fix)
            index+=2;
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
