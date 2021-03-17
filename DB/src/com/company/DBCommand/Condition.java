package com.company.DBCommand;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;

import java.util.ArrayList;

public class Condition extends Parser{

    private final ArrayList<String> command;

    public Condition(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        if(command==null){
            throw new EmptyData("command");
        }
        if(!isCondition()){
            throw new CommandException(command.get(index), index, "condition");
        }
    }

    //checking if condition is surrounded by brackets
    private boolean isCondition(){
        if(command.get(index).charAt(0)=='('){
            return command.get(index).charAt(command.get(index).length() - 1) == ')';
        }
        return false;
    }
}
