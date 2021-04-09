package com.company.DBCommand;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;

import java.util.ArrayList;

public class ValueList {

    private final ArrayList<String> command;
    private int index;
    private ArrayList<String> valueListString;

    public ValueList(ArrayList<String> command, int index)
            throws DBException{
        this.command = command;
        this.index = index;
        parseValueList();
    }

    public ArrayList<String> getValueListString()
            throws DBException{
        if(valueListString!=null){
            return valueListString;
        }
        throw new EmptyData("get value list string");
    }

    private void parseValueList() throws DBException {
        ArrayList<Value> valueArrayList = new ArrayList<>();
        valueListString = new ArrayList<>();
        //move past first bracket
        index++;
        while (!command.get(index).equals(")")) {
            //getting first value & storing in list
            Value value = new Value(command, index);
            valueArrayList.add(value);
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
    }

    public int getIndex(){
        return index;
    }
}
