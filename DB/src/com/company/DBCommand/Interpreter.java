package com.company.DBCommand;

import com.company.DBExceptions.*;
import com.company.Database;
import com.company.FileIO;
import com.company.Table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Interpreter {

    protected int index;
    private String tableName;
    private String databaseName;
    private String attributeName;
    private ArrayList<String> attributeList;
    private ArrayList<Condition> conditionListArray;
    private ConditionList conditionListObject;
    private String currentFolder;
    private String parentFolder;
    private final String homeDirectory;
    private Database database;
    private HashMap<String, Database> databaseMap;
    private Table table;

    public Interpreter(String homeDirectory){
        this.homeDirectory = homeDirectory;
        database = new Database(homeDirectory);
        databaseMap = new HashMap<>();
    }

    public void interpretCommand(String command, Parser parser) throws DBException {
        try {
            switch (command) {
                case "use":
                    interpretUse(command, parser);
                    break;
                case "create":
                    interpretCreate(command, parser);
                    break;
                case "drop":
                    interpretDrop(command, parser);
                    break;
                case "alter":
                    break;
                case "insert":
                    break;
                case "select":
                    break;
                case "update":
                    break;
                case "delete":
                    break;
                case "join":
                    break;
                default:
            }
        } catch(DBException e){
            throw new CommandException(command, 0, "interpreting command", e);
        }
    }

    private void interpretDrop(String command, Parser parser) throws DBException {
        StorageType type = parser.getType();
        System.out.println("entered interpretDrop");
        if(type==StorageType.DATABASE){
            //get database name from parser
            databaseName = parser.getDatabaseName();
            //get the pathname for the folder
            currentFolder = parser.getCurrentFolder();
            //delete the file system for the database
            FileIO fileToDelete = new FileIO(currentFolder);
            fileToDelete.deleteFolder();
            //delete database from memory
            System.out.println("deleting database "+databaseName);
            if(databaseMap.containsKey(databaseName)) {
                System.out.println("found database "+databaseName);
                databaseMap.remove(databaseName);
            }else{
                throw new EmptyData("database does not exist in memory");
            }
        }
        else if(type==StorageType.TABLE) {
            tableName = parser.getTableName();
            File fileToDelete = new File(currentFolder+File.separator+tableName+".tab");
            if(fileToDelete.exists()){
                if(!fileToDelete.delete()){
                    throw new FileException("couldn't delete table "+tableName);
                }
            }
            System.out.println("deleting table "+tableName);
            System.out.println("from database "+databaseName+" "+database.getDatabaseName());
            if(database.getTables().containsKey(tableName)) {
                System.out.println("found table "+tableName);
                database.removeTable(tableName);
            }else{
                throw new EmptyData("table does not exist in memory");
            }
        }
    }

    private void interpretUse(String command, Parser parser) throws DBException {
        //gets the name of the database
        databaseName = parser.getDatabaseName();
        //gets the relative pathname to the database
        currentFolder = parser.getCurrentFolder();
        database = databaseMap.get(databaseName);
    }

    private void interpretCreate(String command, Parser parser ) throws DBException{
        StorageType type = parser.getType();
        if(type==StorageType.DATABASE) {
            databaseName = parser.getDatabaseName();
            try {
                FileIO fileIO = new FileIO(databaseName);
                //creates new folder and returns an empty database object
                database = fileIO.makeFolder(homeDirectory,databaseName);
                databaseMap.put(databaseName, database);
            } catch(DBException e){
                throw new FileException(e);
            }
        }else if(type==StorageType.TABLE) {
            tableName = parser.getTableName();
            attributeList = parser.getAttributeList();
            try{
                //make a new file within specified database
                FileIO fileIO = new FileIO(databaseName);
                //creates a new table within a specified folder (returns error if file already exists)
                File newTableFile = fileIO.makeFile(currentFolder, tableName);
                table = new Table(databaseName, tableName);
                table.fillTableFromMemory(attributeList, null);
                System.out.println("adding table to "+database.getDatabaseName());
                database.addTable(table);
                //writes to file (creates file if it doesn't exist)
                fileIO.writeFile(currentFolder, tableName, table);
            } catch (DBException | IOException e) {
                throw new FileException(e);
            }
        }
    }
}
