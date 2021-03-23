package com.company.DBCommand;

import com.company.DBExceptions.*;
import com.company.Database;
import com.company.FileIO;
import com.company.Table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CreateCMD extends Parser implements DBCommand {

    private final ArrayList<String> command;
    private int index;
    private StorageType type;
    private String tableName;
    private String databaseName;
    private final String parentFolder;
    private ArrayList<String> attributeList;
    private Database database;
    private Table table;

    public CreateCMD(ArrayList<String> command, int index, String parentFolder) throws DBException {
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

    public void execute() throws DBException {
        if(type==StorageType.DATABASE){
            try {
                System.out.println("creating database "+databaseName+" in "+parentFolder);
            FileIO fileIO = new FileIO(databaseName);
            //creates new folder and returns an empty database object
            database = fileIO.makeFolder(parentFolder,databaseName);
        } catch(DBException e){
            throw new FileException(e);
        }
        }else if(type==StorageType.TABLE){
            try{
                System.out.println("creating table "+tableName+ " in "+databaseName);
                //make a new file within specified database
                FileIO fileIO = new FileIO(databaseName);
                //creates a new table within a specified folder
                //returns error if file already exists
                File newTableFile = fileIO.makeFile(databaseName, tableName);
                System.out.println("attributeList: "+attributeList);
                table = new Table(databaseName, tableName);
                table.fillTableFromMemory(attributeList, null);
                //writes to file (creates file if it doesn't exist)
                fileIO.writeFile(databaseName, tableName, table);
            } catch (DBException | IOException e) {
                throw new FileException(e);
            }
        }else{
            throw new StorageTypeException(StorageType.DATABASE, index, null);
        }
    }

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
