package com.company.DBCommand;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;

import java.util.ArrayList;

public class WildAttributeList extends Parser{

    private int index;
    private final ArrayList<String> command;
    private final ArrayList<String> attributeList;

    public WildAttributeList(ArrayList<String> command, int index)
            throws DBException {
        this.index = index;
        this.command = command;
        if(command==null){
            throw new EmptyData("command in WildAttributeList");
        }
        attributeList = new ArrayList<>();
        parseAttributeList();
    }

    private void parseAttributeList() throws DBException{
        String nextToken = command.get(index);
        while(!nextToken.equals(";")&&
                !nextToken.equals("from")&&!nextToken.equals(")")){
            parseAttributeName(command, index);
            String attributeName = nextToken;
            attributeList.add(attributeName);
            index++;
            nextToken = command.get(index);
            if(nextToken.equals(",")){
                index++;
                parseAttributeList();
                return;
            }
            else if(!nextToken.equals("from")&&!nextToken.equals(")")){
                throw new CommandException(nextToken, index,
                        "missing comma in attribute list");
            }
        }
    }

    public ArrayList<String> getAttributeList() throws EmptyData {
        if(attributeList!=null){
            return attributeList;
        }
        throw new EmptyData("wild attribute list");
    }

    public int getIndex(){
        return index;
    }
}
