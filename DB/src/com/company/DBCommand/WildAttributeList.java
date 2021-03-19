package com.company.DBCommand;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;

import java.util.ArrayList;

public class WildAttributeList extends Parser{

    private int index;
    private ArrayList<String> command;
    private String attributeName;
    private ArrayList<String> attributeList;

    public WildAttributeList(ArrayList<String> command, int index) throws CommandException {
        this.index = index;
        this.command = command;
        attributeList = new ArrayList<String>();
        try{
            parseAttributeList();
        }catch(DBException e){
            throw new CommandException(command.get(index), index, "wild attribute list", e);
        }
    }

    private void parseAttributeList() throws DBException{
        try{
            if(command!=null){
                String nextToken = command.get(index);
                while(!nextToken.equals(";")&&!nextToken.equals("from")){
                    parseAttributeName(command, index);
                    attributeName = nextToken;
                    attributeList.add(attributeName);
                    index++;
                    nextToken = command.get(index);
                    if(nextToken.equals(",")){
                        index++;
                        parseAttributeList();
                        return;
                    }
                }
            }else{
                throw new EmptyData("command in attribute list");
            }
        }catch(DBException e){
            throw new CommandException(command.get(index), index, "wild attribute list", e);
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
