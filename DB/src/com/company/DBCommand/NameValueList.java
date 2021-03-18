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
    private ArrayList<String> attributeList;
    private String valueString;
    private ArrayList<String> valueList;
    private boolean isList;

    public NameValueList(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        this.index = index;
        type = StorageType.ATTRIBUTE;
        try{
            parseNameValueList();
        }catch(DBException e){
            throw new CommandException(command.get(index), index, "name value list", e);
        }
    }

    private void parseNameValueList() throws DBException{
        valueList = new ArrayList<String>();
        attributeList = new ArrayList<String>();
        try{
            System.out.println("entered parseNameValueList");
            parseNameValuePair();
            //storing the attribute name and value string;
            attributeList.add(attributeName);
            System.out.println("attributeName "+attributeName);
            System.out.println("attributeList "+attributeList);
            valueList.add(valueString);
            System.out.println("value "+valueString);
            System.out.println("valueList "+valueList);
            String nextToken = command.get(index);
            System.out.println("nextToken "+nextToken);
            if(nextToken.equals(";")||nextToken.equals("WHERE")){
                return;
            }
            if(nextToken.equals(",")){
                index++;
                parseNameValueList();
                return;
            }
            throw new CommandException(command.get(index), index, "name value list");
        }catch(DBException e){
            throw new CommandException(command.get(index), index, "name value list", e);
        }
    }

    private void parseNameValuePair() throws DBException{
        try{
            //have to decrease index because parseAttributeName increases it again
            index--;
            attributeName = parseAttributeName(command, index);
            //now have to skip past attribute name
            index+=2;
            String nextToken = command.get(index);
            checkNextToken(nextToken, "=", index);
            index++;
            //get the value
            Value value = new Value(command, index);
            valueString = value.getValue();
            index = value.getIndex()+1;
        }catch(DBException e){
            throw new CommandException(command.get(index), index, "name value pair", e);
        }
    }

    public boolean isList(){
        return isList;
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
