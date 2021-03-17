package com.company.Parsing;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;
import com.company.DBExceptions.StorageType;

import java.util.ArrayList;

public class UpdateCMD extends Parser implements DBCommand{

    private final ArrayList<String> command;
    private int index;
    private final StorageType type;
    private String tableName;

    public UpdateCMD(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        this.index = index;
        type = StorageType.TABLE;
        if(command != null) {
            if (!parseUpdate()) {
                throw new CommandException(
                        command.get(index), index, "table name");
            }
        }else{
            throw new EmptyData("UPDATE command");
        }
    }

    //UPDATE <TableName> SET <NameValueList> WHERE <Condition>
    private boolean parseUpdate() throws DBException{
        try {
            tableName = parseTableName(command, index);
            //point index to after tableName
            index+=2;
            String nextToken = command.get(index);
            checkNextToken(nextToken, "set", index);
            //nameValueList
            index++;
            nextToken = command.get(index);
            checkNextToken(nextToken, "where", index);
            //condition
            //point index to end of command
            index++;
            return true;
        } catch(DBException e){
            throw new CommandException(command.get(index), index, "UPDATE", e);
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
