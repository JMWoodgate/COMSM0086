package com.company.DBCommand;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;

import java.util.ArrayList;

public class ConditionList {

    private final ArrayList<String> command;
    private ArrayList<Condition> conditionList;
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
    public void parseConditions() throws DBException {
        System.out.println("entered parseConditions with "+command);
        System.out.println("looking at "+command.get(index));
        try{
            switch(command.get(index)){
                case ("("):
                    index++;
                    //check for nested brackets
                    if(command.get(index).equals("(")){
                        parseConditions();
                        System.out.println("breaking parseConditions from (");
                    }
                    else {
                        //get condition
                        Condition condition = new Condition(command, index);
                        conditionList.add(condition);
                        //update index
                        index = condition.getIndex();
                        System.out.println("breaking parseConditions from first condition");
                    }
                //check for end of condition
                case (")"):
                    System.out.println("got )");
                    index++;
                    //check for second condition or end of condition
                    endOfCondition();
                    break;
                //if no opening bracket, is a single condition
                default:
                    System.out.println("entered default switch in parseConditions with "+command.get(index));
                    Condition condition = new Condition(command, index);
                    System.out.println("got new condition "+condition.getConditionString());
                    conditionList.add(condition);
                    System.out.println("added condition to list");
                    index = condition.getIndex();
                    System.out.println("got index "+index);
                    break;
            }
            if(count < conditionList.size()) {
                System.out.println("condition list stored in parseConditions in ConditionList " + conditionList.get(count).getConditionString());
                count++;
            }
        }catch(DBException e){
            throw new CommandException(command.get(index), index, "conditions", e);
        }
    }

    private void endOfCondition() throws CommandException {
        try {
            System.out.println("checking endOfCondition with "+command.get(index));
            switch (command.get(index)) {
                case ("and"):
                case ("or"):
                    index++;
                    System.out.println("calling parseConditions from endOfCondition with "+command.get(index));
                    parseConditions();
                    System.out.println("returned from parseConditions call in endOfCondition");
                    break;
                case (";"):
                case (")"):
                    break;
                default:
                    throw new CommandException(command.get(index), index, "and, or, ; at end of condition");
            }
        }catch(DBException e){
            throw new CommandException(command.get(index), index, "and, or, ; at end of condition", e);
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
