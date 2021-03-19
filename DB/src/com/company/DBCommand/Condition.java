package com.company.DBCommand;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;

import java.util.ArrayList;

public class Condition extends Parser{

    private final ArrayList<String> command;
    private ArrayList<String> attributeList;
    private ArrayList<String> conditions;
    private int index;

    public Condition(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        this.index = index;
        attributeList = new ArrayList<String>();
        conditions = new ArrayList<String>();
        if(command==null){
            throw new EmptyData("command in condition");
        }
    }

    //(<Condition>) AND (<Condition>) | (<Condition) OR (<Condition>)
    // | <attributeName> <operator> <value>
    private void parseCondition()throws DBException{
        try{
            if(command.get(index).equals("("){

            }
            String attributeName = parseAttributeName(command, index);
            attributeList.add(attributeName);
            index++;

        }catch(DBException e){
            throw new CommandException(command.get(index), index, "condition");
        }
    }



    private String getOp() throws DBException{
        try {
            switch (command.get(index)) {
                case (">"):
                    index++;
                    if (command.get(index).equals("=")) {
                        index++;
                        return ">=";
                    }
                    index--;
                    return ">";
                case ("<"):
                    index++;
                    if (command.get(index).equals("=")) {
                        index++;
                        return "<=";
                    }
                    index--;
                    return "<";
                case ("="):
                    index++;
                    if (command.get(index).equals("=")) {
                        index++;
                        return "==";
                    }
                    index--;
                    return "=";
                case ("LIKE"):
                    return "LIKE";
                default:
                    throw new CommandException(command.get(index), index, "op");
            }
        }catch(DBException e){
            throw new CommandException(command.get(index), index, "op", e);
        }
    }
}
