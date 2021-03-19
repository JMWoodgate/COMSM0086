package com.company.DBCommand;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;
import com.company.DBExceptions.StorageType;

import java.util.ArrayList;

public class CreateCMD extends Parser implements DBCommand {

    private final ArrayList<String> command;
    private int index;
    private StorageType type;
    private String tableName;
    private String databaseName;
    private ArrayList<String> attributeList;

    public CreateCMD(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        this.index = index;
        attributeList = new ArrayList<String>();
        if(command != null) {
            if (!parseCreate()) {
                throw new CommandException(
                        command.get(index), index, "table, database or attribute list");
            }
        }else{
            throw new EmptyData("CREATE command");
        }
    }

    public void execute(){}

    //CREATE <create Database> || <create Table>
    private boolean parseCreate() throws CommandException {
        index++;
        String nextToken = command.get(index);
        try {
            switch (nextToken) {
                case ("database"):
                    type = StorageType.DATABASE;
                    databaseName = parseDatabaseName(command, index);
                    //increase index to be pointing to the ; after databaseName
                    index += 2;
                    break;
                case ("table"):
                    type = StorageType.TABLE;
                    tableName = parseTableName(command, index);
                    //skip to after tableName
                    index += 2;
                    nextToken = command.get(index);
                    if (nextToken.equals(";")) {
                        break;
                    } else if (nextToken.equals("(")) {
                        index++;
                        WildAttributeList wildAttributeList = new WildAttributeList(command, index);
                        //updating index and storing attributes
                        index = wildAttributeList.getIndex();
                        attributeList = wildAttributeList.getAttributeList();
                        break;
                    }
                    throw new CommandException(nextToken, index, "; or (<attribute list>)");
                default:
                    return false;
            }
        } catch (DBException e){
            throw new CommandException(command.get(index), index, "CREATE", e);
        }
        return true;
    }

    public StorageType getType(){
        return type;
    }

    public ArrayList<String> getAttributeList() throws EmptyData {
        if(attributeList!=null){
            return attributeList;
        }
        throw new EmptyData("wild attribute list");
    }

    public String getTableName() throws EmptyData {
        if(tableName!=null) {
            return tableName;
        }
        throw new EmptyData("table name");
    }

    public String getDatabaseName() throws EmptyData {
        if(databaseName!=null) {
            return databaseName;
        }
        throw new EmptyData("database name");
    }

    public int getIndex(){
        return index;
    }
}
