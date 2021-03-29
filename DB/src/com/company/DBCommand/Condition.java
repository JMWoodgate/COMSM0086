package com.company.DBCommand;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;

import java.util.ArrayList;

public class Condition extends Parser{

    private final ArrayList<String> command;
    private String attributeName;
    private String op;
    private Value valueObject;
    private String valueString;
    private int index;

    public Condition(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        this.index = index;
        if(command==null){
            throw new EmptyData("command in condition");
        }
        try{
            parseCondition();
        }catch(DBException e){
            throw new CommandException(
                    command.get(index), index, "condition", e);
        }
    }

    // <attributeName> <operator> <value>
    private void parseCondition() throws DBException{
        //get attribute name
        attributeName = parseAttributeName(command, index);
        index++;
        //get operator
        op = parseOp();
        index++;
        //get value and store as object as well as string
        valueObject = new Value(command, index);
        valueString = valueObject.getValue();
        //update index
        index = valueObject.getIndex()+1;
    }

    private String parseOp() throws DBException{
        switch (command.get(index)) {
            case (">"):
                index++;
                if(command.get(index).equals("=")){
                    return ">=";
                }
                index--;
                return ">";
            case ("<"):
                index++;
                if(command.get(index).equals("=")){
                    return "<=";
                }
                index--;
                return "<";
            case ("!"):
                index++;
                if(command.get(index).equals("=")){
                    return "!=";
                }
                index--;
            case ("="):
                index++;
                if(command.get(index).equals("=")){
                    return "==";
                }
                index--;
            case ("like"):
                return "like";
            default:
                throw new CommandException(command.get(index), index, "op");
            }
    }

    public Value getValueObject()throws EmptyData{
        if(valueObject!=null){
            return valueObject;
        }
        throw new EmptyData("value object in condition");
    }

    public String getConditionString() throws EmptyData{
        if(op!=null){
            if(attributeName!=null) {
                if (valueString != null) {
                    return attributeName + op + valueString;
                }
            }
        }
        throw new EmptyData("value string in condition");
    }

    public String getValueString() throws EmptyData{
        if(valueString!=null){
            return valueString;
        }
        throw new EmptyData("value string in condition");
    }

    public int getIndex(){
        return index;
    }

    public String getAttribute() throws EmptyData{
        if(attributeName!=null){
            return attributeName;
        }
        throw new EmptyData("attribute list in condition");
    }

    public String getOp() throws EmptyData{
        if(op!=null){
            return op;
        }
        throw new EmptyData("op");
    }
}
