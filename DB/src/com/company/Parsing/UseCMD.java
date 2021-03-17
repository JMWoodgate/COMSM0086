package com.company.Parsing;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;
import com.company.DBExceptions.StorageType;

import java.util.ArrayList;

public class UseCMD extends Parser implements DBCommand{

    private final ArrayList<String> command;
    private int index;
    private final String databaseName;
    private final StorageType type;

    public UseCMD(ArrayList<String> command, int index) throws CommandException {
        try {
            this.command = command;
            this.index = index;
            type = StorageType.DATABASE;
            databaseName = parseDatabaseName(command, index);
            this.index += 2;
            //increase index to be pointing to the ; after databaseName
        } catch (DBException e){
            throw new CommandException(command.get(index), index, "USE", e);
        }
    }

    public void execute(){}

    public String getDatabaseName() throws EmptyData {
        if(databaseName!=null) {
            return databaseName;
        }
        throw new EmptyData("database name");
    }

    public int getIndex(){
        return index;
    }

    public StorageType getType(){
        return type;
    }
}
