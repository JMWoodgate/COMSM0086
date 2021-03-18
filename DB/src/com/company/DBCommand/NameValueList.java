package com.company.DBCommand;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;
import com.company.DBExceptions.StorageType;

import javax.naming.Name;
import java.util.ArrayList;

public class NameValueList extends Parser{

    private final ArrayList<String> command;
    private ArrayList<String> valueListString;
    private int index;
    private final StorageType type;
    private String attributeName;
    private String valueString;

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
            //need to check if the next token is an open bracket (i.e. if it is a valueList)
            if(command.get(index).equals("(")){
                ValueList valueList = new ValueList(command, index);
                index = valueList.getIndex();
                valueListString = valueList.getValueListString();
            }else {
                Value value = new Value(command, index);
                valueString = value.getValue();
                index = value.getIndex();
            }
        }catch(DBException e){
            throw new CommandException(command.get(index), index, "name value pair", e);
        }
    }

    public ArrayList<String> getListValueString() throws EmptyData {
        if(valueListString!=null){
            return valueListString;
        }
        throw new EmptyData("value list");
    }

    public String getValueString() throws EmptyData {
        if(valueString!=null){
            return valueString;
        }
        throw new EmptyData("value");
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
