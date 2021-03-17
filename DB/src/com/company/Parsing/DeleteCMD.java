package com.company.Parsing;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;
import com.company.DBExceptions.StorageType;

import java.util.ArrayList;

public class DeleteCMD extends Parser implements DBCommand{

    private final ArrayList<String> command;
    private int index;
    private final StorageType type;
    private String tableName;

    public DeleteCMD(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        this.index = index;
        type = StorageType.TABLE;
        if(command != null) {
            if (!parseDelete()) {
                throw new CommandException(
                        command.get(index), index, "table name");
            }
        }else{
            throw new EmptyData("DELETE command");
        }
    }

    //DELETE FROM <TableName> WHERE <Condition>
    private boolean parseDelete() throws DBException{
        try {
            index++;
            String nextToken = command.get(index);
            checkNextToken(nextToken, "from", index);
            tableName = parseTableName(command, index);
            //increasing index to point to after the table name
            index+=2;
            nextToken = command.get(index);
            checkNextToken(nextToken, "where", index);
            index++;
            //condition
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
