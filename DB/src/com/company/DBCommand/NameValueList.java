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
    private String attributeName;
    private final ArrayList<String> attributeList;
    private String valueString;
    private final ArrayList<String> valueList;

    public NameValueList(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        this.index = index;
        valueList = new ArrayList<String>();
        attributeList = new ArrayList<String>();
        try{
            parseNameValueList();
        }catch(DBException e){
            throw new CommandException(command.get(index), index, "name value list", e);
        }
    }

    private void parseNameValueList() throws DBException{
        try{
            String nextToken = command.get(index);
            while(!nextToken.equals(";")&&!nextToken.equals("where")) {
                parseNameValuePair();
                nextToken = command.get(index);
                if (nextToken.equals(",")) {
                    index++;
                    //if comma, it's a list so need to call recursively
                    parseNameValueList();
                    return;
                }
            }
        }catch(DBException e){
            throw new CommandException(command.get(index), index, "name value list", e);
        }
    }

    private void parseNameValuePair() throws DBException{
        try{
            //have to decrease index because parseAttributeName increases it again
            index--;
            attributeName = parseAttributeName(command, index);
            attributeList.add(attributeName);
            //now have to skip past attribute name
            index+=2;
            String nextToken = command.get(index);
            checkNextToken(nextToken, "=", index);
            index++;
            //get the value
            Value value = new Value(command, index);
            valueString = value.getValue();
            valueList.add(valueString);
            index = value.getIndex()+1;
        }catch(DBException e){
            throw new CommandException(command.get(index), index, "name value pair", e);
        }
    }

    public ArrayList<String> getAttributeList() throws EmptyData {
        if(attributeList!=null){
            return attributeList;
        }
        throw new EmptyData("attribute list");
    }

    public ArrayList<String> getValueList() throws EmptyData {
        if(valueList!=null){
            return valueList;
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
