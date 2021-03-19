package com.company.DBCommand;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;

import java.util.ArrayList;

public class ConditionList {

    private final ArrayList<String> command;
    private final ArrayList<Condition> conditionList;
    private int index;
    private int count;

    public ConditionList(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        this.index = index;
        count = 0;
        conditionList = new ArrayList<>();
        if(command==null){
            throw new EmptyData("command in condition");
        }
        try{
            parseConditions();
        }catch(DBException e){
            throw new CommandException(command.get(index), index, "condition", e);
        }
    }

    //(<Condition>) AND (<Condition>) | (<Condition) OR (<Condition>)
    // | <attributeName> <operator> <value>
    private void parseConditions() throws DBException {
        try{
            switch(command.get(index)){
                case ("("):
                    index++;
                    //check for nested brackets
                    if(command.get(index).equals("(")){
                        parseConditions();
                    }
                    else {
                        //get condition
                        Condition condition = new Condition(command, index);
                        conditionList.add(condition);
                        //update index
                        index = condition.getIndex();
                    }
                //check for end of condition
                case (")"):
                    index++;
                    //check for second condition or end of condition
                    endOfCondition();
                    break;
                //if no opening bracket, is a single condition
                default:
                    Condition condition = new Condition(command, index);
                    conditionList.add(condition);
                    index = condition.getIndex();
                    break;
            }
            if(count < conditionList.size()) {
                count++;
            }
        }catch(DBException e){
            throw new CommandException(command.get(index), index, "conditions", e);
        }
    }

    private void endOfCondition() throws CommandException {
        try {
            switch (command.get(index)) {
                case ("and"):
                case ("or"):
                    index++;
                    parseConditions();
                    break;
                case (")"):
                case (";"):
                    break;
                default:
                    throw new CommandException(command.get(index), index, "and, or, ;, ) at end of condition");
            }
        }catch(DBException e){
            throw new CommandException(command.get(index), index, "and, or, ;, ) at end of condition", e);
        }
    }

    public ArrayList<Condition> getConditionList() throws EmptyData {
        if(conditionList!=null){
            return conditionList;
        }
        else{
            throw new EmptyData("condition list");
        }
    }

    public int getIndex(){
        return index;
    }
}
