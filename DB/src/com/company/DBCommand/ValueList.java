package com.company.DBCommand;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;

import java.util.ArrayList;

public class ValueList {

    private final ArrayList<String> command;
    private int index;
    private ArrayList<Value> valueList;
    private ArrayList<String> valueListString;

    public ValueList(ArrayList<String> command, int index) throws DBException{
        this.command = command;
        this.index = index;
        setValueList();
    }

    public ArrayList<String> getValueListString() throws DBException{
        if(valueListString!=null){
            return valueListString;
        }
        throw new EmptyData("get value list string");
    }

    public ArrayList<Value> getValueList() throws DBException{
        if(valueList!=null){
            return valueList;
        }
        throw new EmptyData("get value list");
    }

    private void setValueList() throws DBException {
        valueList = new ArrayList<Value>();
        valueListString = new ArrayList<String>();
        try {
            //move past first bracket
            index++;
            //looping until the end of valueList
            while (!command.get(index).equals(")")&&index<command.size()) {
                //getting first value & storing in list
                Value value = new Value(command, index);
                valueList.add(value);
                //also storing string in a list for ease of access
                valueListString.add(value.getValue());
                index = value.getIndex()+1;
                //if no comma and haven't reached end of list, syntax error
                if(!command.get(index).equals(",")&&!command.get(index).equals(")")){
                    throw new CommandException(command.get(index), index, ", in value list");
                }
                //exit if we've reached end of command
                if(command.get(index).equals(")")){
                    return;
                }
                index++;
            }
        } catch(DBException e){
            throw new CommandException(command.get(index), index, "value list");
        }
    }

    public int getIndex(){
        return index;
    }
}
