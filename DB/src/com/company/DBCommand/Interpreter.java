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
    private Table table;

    public Interpreter(String homeDirectory){
        this.homeDirectory = homeDirectory;
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

    private void interpretUse(String command, Parser parser) throws DBException {
        databaseName = parser.getDatabaseName();
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
                //creates a new table within a specified folder
                //returns error if file already exists
                File newTableFile = fileIO.makeFile(homeDirectory+File.separator+databaseName, tableName);
                table = new Table(databaseName, tableName);
                table.fillTableFromMemory(attributeList, null);
                System.out.println("successfully filled table");
                database.addTable(table);
                System.out.println("successfully added table");
                //writes to file (creates file if it doesn't exist)
                fileIO.writeFile(homeDirectory+File.separator+databaseName, tableName, table);
                System.out.println("successfully written file");
            } catch (DBException | IOException e) {
                throw new FileException(e);
            }
        }
    }
}
