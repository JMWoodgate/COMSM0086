package com.company.DBCommand;

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
    private ArrayList<String> valueListString;

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

    private boolean parseInsert() throws DBException{
        index++;
        String nextToken = command.get(index);
        checkNextToken(nextToken, "into", index);
        tableName = parseTableName(command, index);
        //increasing index to point to after the table name
        index+=2;
        nextToken = command.get(index);
        checkNextToken(nextToken, "values", index);
        index++;
        nextToken = command.get(index);
        checkNextToken(nextToken, "(", index);
        ValueList valueList = new ValueList(command, index);
        valueListString = valueList.getValueListString();
        index = valueList.getIndex();
        return true;
    }

    public ArrayList<String> getValueListString() throws DBException{
        if(valueListString!=null){
            return valueListString;
        }
        throw new EmptyData("get value list string");
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

    public int getIndex(){
        return index;
    }
}
