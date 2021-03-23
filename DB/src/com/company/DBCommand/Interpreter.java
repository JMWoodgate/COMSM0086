package com.company.DBCommand;

import com.company.DBExceptions.*;
import com.company.Database;
import com.company.FileIO;
import com.company.Table;
import com.company.Tokenizer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
    private ArrayList<Database> databaseList;
    private Table table;

    public Interpreter(String homeDirectory){
        this.homeDirectory = homeDirectory;
        database = new Database(homeDirectory);
        databaseList = new ArrayList<>();
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
            System.out.println("in interpretDrop type Database");
            databaseName = parser.getDatabaseName();
            currentFolder = parser.getCurrentFolder();
            FileIO fileToDelete = new FileIO(currentFolder);
            fileToDelete.deleteFolder();
        }
        else if(type==StorageType.TABLE) {
            tableName = parser.getTableName();
        }
    }

    private void interpretUse(String command, Parser parser) throws DBException {
        //gets the name of the database
        databaseName = parser.getDatabaseName();
        //gets the relative pathname to the database
        currentFolder = parser.getCurrentFolder();
    }

    private void interpretCreate(String command, Parser parser ) throws DBException{
        StorageType type = parser.getType();
        if(type==StorageType.DATABASE) {
            databaseName = parser.getDatabaseName();
            try {
                FileIO fileIO = new FileIO(databaseName);
                //creates new folder and returns an empty database object
                database = fileIO.makeFolder(homeDirectory,databaseName);
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
                database.addTable(table);
                //writes to file (creates file if it doesn't exist)
                fileIO.writeFile(currentFolder, tableName, table);
            } catch (DBException | IOException e) {
                throw new FileException(e);
            }
        }
    }
}
