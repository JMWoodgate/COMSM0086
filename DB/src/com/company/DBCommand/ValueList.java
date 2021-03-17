package com.company.DBCommand;

import com.company.DBExceptions.DBException;

import java.util.ArrayList;

public class ValueList extends Value{

    private int index;
    private ArrayList<String> command;

    public ValueList(ArrayList<String> command, int index) throws DBException {
        super(command, index);
        this.command = command;
        this.index = index;
    }


}
