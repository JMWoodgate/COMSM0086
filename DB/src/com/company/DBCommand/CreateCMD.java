package com.company.DBCommand;

import com.company.DBExceptions.*;
import java.util.ArrayList;

public class CreateCMD extends Parser implements DBCommand {

    private final ArrayList<String> command;
    private int index;
    private StorageType type;
    private String tableName;
    private String databaseName;
    private final String parentFolder;
    private ArrayList<String> attributeList;

    public CreateCMD(ArrayList<String> command, int index, String parentFolder)
            throws DBException {
        this.command = command;
        this.index = index;
        this.parentFolder = parentFolder;
        attributeList = new ArrayList<>();
        if(command != null) {
            if (!parseCreate()) {
                throw new CommandException(
                        command.get(index), index, "table, database or attribute list");
            }
        }else{
            throw new EmptyData("CREATE command");
        }
    }

    private boolean parseCreate() throws DBException {
        index++;
        String nextToken = command.get(index);
        switch (nextToken) {
            case ("database"):
                type = StorageType.DATABASE;
                databaseName = parseDatabaseName(command, index);
                //increase index to point to after databaseName
                index += 2;
                break;
            case ("table"):
                type = StorageType.TABLE;
                tableName = parseTableName(command, index);
                //store the database name (location for the new table file)
                databaseName = parentFolder;
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
