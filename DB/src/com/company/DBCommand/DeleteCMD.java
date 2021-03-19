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
    private ArrayList<Condition> conditionList;

    public DeleteCMD(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        this.index = index;
        type = StorageType.TABLE;
        conditionList = new ArrayList<>();
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
            System.out.println("entered parseDelete in DeleteCMD");
            index++;
            String nextToken = command.get(index);
            checkNextToken(nextToken, "from", index);
            tableName = parseTableName(command, index);
            //increasing index to point to after the table name
            index+=2;
            nextToken = command.get(index);
            checkNextToken(nextToken, "where", index);
            index++;
            System.out.println("calling parseConditions from DeleteCMD with "+command.get(index));
            ConditionList conditionListObject = new ConditionList(command, index);
            conditionList = conditionListObject.getConditionList();
            System.out.println("conditionList in parseDelete DeleteCMD");
            index = conditionListObject.getIndex();
            System.out.println("returned from parseConditions to DeleteCMD");
            return true;
        } catch(DBException e){
            throw new CommandException(command.get(index), index, "DELETE", e);
        }
    }

    public ArrayList<Condition> getConditionList() throws EmptyData {
        if(conditionList!=null){
            return conditionList;
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
