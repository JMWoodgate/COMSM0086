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
    private ConditionList conditionListObject;
    private boolean multipleConditions;

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

    private boolean parseDelete() throws DBException{
        index++;
        checkNextToken(command.get(index), "from", index);
        tableName = parseTableName(command, index);
        //increasing index to point to after the table name
        index+=2;
        checkNextToken(command.get(index), "where", index);
        index++;
        conditionListObject = new ConditionList(command, index);
        index = conditionListObject.getIndex();
        multipleConditions = conditionListObject.isMultipleConditions();
        return true;
    }

    public boolean isMultipleConditions(){
        return multipleConditions;
    }

    public ConditionList getConditionListObject()throws EmptyData {
        if(conditionListObject!=null){
            return conditionListObject;
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

    public int getIndex(){
        return index;
    }
}
