package com.company.DBCommand;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;
import com.company.DBExceptions.StorageType;

import javax.naming.Name;
import java.util.ArrayList;

public class NameValueList extends Parser{

    private final ArrayList<String> command;
    private int index;
    private final StorageType type;
    private String attributeName;


    public NameValueList(ArrayList<String> command, int index){
        this.command = command;
        this.index = index;
        type = StorageType.ATTRIBUTE;
    }

    private void parseNameValuePair() throws DBException{
        try{
            attributeName = parseAttributeName(command, index);
            index++;
            String nextToken = command.get(index);
            checkNextToken(nextToken, "=", index);
            index++;
        }catch(DBException e){
            throw new CommandException(command.get(index), index, "name value pair", e);
        }
    }

    public String getAttributeName() throws EmptyData {
        if(attributeName!=null){
            return attributeName;
        }
        throw new EmptyData("attribute name");
    }

    public int getIndex(){
        return index;
    }
}
