package com.company.DBCommand;

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
    private ArrayList<String> attributeList;
    private ArrayList<String> valueList;

    public UpdateCMD(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        this.index = index;
        type = StorageType.TABLE;
        valueList = new ArrayList<String>();
        attributeList = new ArrayList<String>();
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
            index++;
            //make nameValueList and update index
            NameValueList nameValueList = new NameValueList(command, index);
            //get attribute names and values
            attributeList = nameValueList.getAttributeList();
            valueList = nameValueList.getValueList();
            index = nameValueList.getIndex();
            nextToken = command.get(index);
            //check for where
            checkNextToken(nextToken, "where", index);
            //condition
            //point index to end of command
            index++;
            return true;
        } catch(DBException e){
            throw new CommandException(command.get(index), index, "UPDATE", e);
        }
    }

    public ArrayList<String> getAttributeList() throws EmptyData {
        if(attributeList!=null){
            return attributeList;
        }
        throw new EmptyData("attribute list");
    }

    public ArrayList<String> getValueList() throws EmptyData {
        if(valueList!=null){
            return valueList;
        }
        throw new EmptyData("value list");
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
