package com.company.DBCommand;

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
    private ArrayList<Condition> conditionListString;
    private ConditionList conditionListObject;

    public DeleteCMD(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        this.index = index;
        type = StorageType.TABLE;
        conditionListString = new ArrayList<>();
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
            //check for "from"
            checkNextToken(command.get(index), "from", index);
            //get table name
            tableName = parseTableName(command, index);
            //increasing index to point to after the table name
            index+=2;
            //check for "where"
            checkNextToken(command.get(index), "where", index);
            index++;
            //get conditions, store as object and string for testing
            conditionListObject = new ConditionList(command, index);
            conditionListString = conditionListObject.getConditionList();
            index = conditionListObject.getIndex();
            return true;
        } catch(DBException e){
            throw new CommandException(command.get(index), index, "DELETE", e);
        }
    }

    public ConditionList getConditionListObject()throws EmptyData {
        if(conditionListObject!=null){
            return conditionListObject;
        }
        throw new EmptyData("condition list in DeleteCMD");
    }

    public ArrayList<Condition> getConditionListString() throws EmptyData {
        if(conditionListString!=null){
            return conditionListString;
        }
        throw new EmptyData("condition list in DeleteCMD");
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
