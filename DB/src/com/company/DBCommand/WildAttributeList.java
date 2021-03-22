package com.company.DBCommand;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;

import java.util.ArrayList;

public class WildAttributeList extends Parser{

    private int index;
    private final ArrayList<String> command;
    private String attributeName;
    private final ArrayList<String> attributeList;

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

    //WILD ATTRIBUTE LIST <attributeList> | <*>
    //ATTRIBUTE LIST <attributeName> | <attributeName>,<attributeList>
    private void parseAttributeList() throws DBException{
        try{
            if(command!=null){
                String nextToken = command.get(index);
                while(!nextToken.equals(";")&&
                        !nextToken.equals("from")&&!nextToken.equals(")")){
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
                    else if(!nextToken.equals("from")&&!nextToken.equals(")")){
                        throw new CommandException(nextToken, index, "missing comma in attribute list");
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
