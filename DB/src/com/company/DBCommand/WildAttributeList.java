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

    //WILD ATTRIBUTE LIST <attributeList> | <*>
    //ATTRIBUTE LIST <attributeName> | <attributeName>,<attributeList>
    private void parseAttributeList() throws DBException{
        try{
            System.out.println("parsing "+command);
            if(command!=null){
                String nextToken = command.get(index);
                while(!nextToken.equals(";")&&
                        !nextToken.equals("from")&&!nextToken.equals(")")){
                    System.out.println("nextToken1 in parseAttributeList "+nextToken);
                    parseAttributeName(command, index);
                    attributeName = nextToken;
                    attributeList.add(attributeName);
                    index++;
                    nextToken = command.get(index);
                    System.out.println("nextToken2 in parseAttributeList "+nextToken);
                    if(nextToken.equals(",")){
                        index++;
                        System.out.println("parsing attribute list recursively");
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
