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
    private ArrayList<String> valueListString;
    private boolean interpretedOK;
    private Exception exception;

    public Interpreter(String homeDirectory) throws FileException {
        this.homeDirectory = homeDirectory;
        database = new Database(homeDirectory);
        FileIO fileIO = new FileIO(homeDirectory);
        databaseMap = fileIO.readAllFolders(homeDirectory);
        interpretedOK = true;
    }

    public String interpretCommand(String command, Parser parser) throws DBException {
        interpretedOK = true;
        String results = null;
        try {
            switch (command) {
                case "use":
                    interpretUse(parser);
                    break;
                case "create":
                    interpretCreate(parser);
                    break;
                case "drop":
                    interpretDrop(parser);
                    break;
                case "alter":
                    break;
                case "insert":
                    interpretInsert(parser);
                    break;
                case "select":
                    results = interpretSelect(parser);
                    System.out.println("results in switch: "+results);
                    break;
                case "update":
                    break;
                case "delete":
                    break;
                case "join":
                    break;
                default:
            }
        } catch(DBException | IOException e){
            interpretedOK = false;
            exception = e;
        }
        System.out.println("returning results from switch: "+results);
        return results;
    }

    private String interpretSelect(Parser parser) throws DBException, IOException {
        String results = null;
        tableName = parser.getTableName();
        if(!database.getTables().containsKey(tableName)){
            throw new EmptyData("table does not exist in memory");
        }
        table = database.getTable(tableName);
        attributeList = parser.getAttributeList();
        System.out.println("attribute list: "+attributeList);
        if(attributeList.get(0).equals("*")){
            results = table.getTable();
            System.out.println("returning results from * "+results);
            return results;
        }
        //checking that all the attributes in the query exist
        checkAttributes();
        conditionListArray = parser.getConditionListArray();
        if(conditionListArray!=null) {
            conditionListObject = parser.getConditionListObject();
        }
        //need to get wild attribute list out of table and print to terminal
        //if conditions set, need to do this based on conditions
        System.out.println("returning results from end of interpretSelect "+results);
        return results;
    }

    private void checkAttributes() throws EmptyData {
        ArrayList<String> tableAttributes = table.getColumns();
        for(String attribute : attributeList){
            if(!isIn(attribute, tableAttributes)){
                throw new EmptyData("column '"+attribute+"' does not exist");
            }
        }
    }

    private boolean isIn(String attribute, ArrayList<String> tableAttributes){
        for(int i = 0; i < tableAttributes.size(); i++){
            if(attribute.equals(tableAttributes.get(i))){
                return true;
            }
        }
        return false;
    }

    private void interpretInsert(Parser parser) throws DBException, IOException {
        tableName = parser.getTableName();
        valueListString = parser.getValueListString();
        if(!database.getTables().containsKey(tableName)) {
            throw new EmptyData("table does not exist in memory");
        }
        //need to put the values into the correct table, both in memory and on file
        table = database.getTable(tableName);
        table.addRow(valueListString);
        //make a new file within specified database
        FileIO fileIO = new FileIO(databaseName);
        //writes to file (creates file if it doesn't exist)
        fileIO.writeFile(currentFolder, tableName, table);
    }

    private void interpretDrop(Parser parser) throws DBException {
        StorageType type = parser.getType();
        if(type==StorageType.DATABASE){
            dropDatabase(parser);
        }
        else if(type==StorageType.TABLE) {
            dropTable(parser);
        }
    }

    private void dropTable(Parser parser) throws DBException{
        tableName = parser.getTableName();
        File fileToDelete = new File(currentFolder+File.separator+tableName+".tab");
        if(fileToDelete.exists()){
            if(!fileToDelete.delete()){
                throw new FileException("couldn't delete table "+tableName);
            }
        }
        if(database.getTables().containsKey(tableName)) {
            database.removeTable(tableName);
        }else{
            throw new EmptyData("table does not exist in memory");
        }
    }

    private void dropDatabase(Parser parser) throws DBException {
        databaseName = parser.getDatabaseName();
        currentFolder = parser.getCurrentFolder();
        //delete the file system for the database
        FileIO fileToDelete = new FileIO(currentFolder);
        fileToDelete.deleteFolder();
        //delete database from memory
        if(databaseMap.containsKey(databaseName)) {
            databaseMap.remove(databaseName);
        }else{
            throw new EmptyData("database does not exist in memory");
        }
    }

    private void interpretUse(Parser parser) throws DBException {
        //gets the name of the database
        databaseName = parser.getDatabaseName();
        //gets the relative pathname to the database
        currentFolder = parser.getCurrentFolder();
        if(!databaseMap.containsKey(databaseName)){
            throw new EmptyData("database does not exist");
        }
        database = databaseMap.get(databaseName);
    }

    private void interpretCreate(Parser parser) throws DBException{
        StorageType type = parser.getType();
        if(type==StorageType.DATABASE) {
            createDatabase(parser);
        }else if(type==StorageType.TABLE) {
            createTable(parser);
        }
    }

    private void createTable(Parser parser) throws DBException{
        tableName = parser.getTableName();
        attributeList = parser.getAttributeList();
        try{
            //make a new file within specified database
            FileIO fileIO = new FileIO(databaseName);
            //creates a new table within a specified folder (returns error if file already exists)
            File newTableFile = fileIO.makeFile(currentFolder, tableName);
            table = new Table(databaseName, tableName);
            if(attributeList.size()>1) {
                //writing column names to file
                table.fillTableFromMemory(attributeList, null);
            }
            database.addTable(table);
            //writes to file (creates file if it doesn't exist)
            fileIO.writeFile(currentFolder, tableName, table);
        } catch (DBException | IOException e) {
            throw new FileException(e);
        }
    }

    private void createDatabase(Parser parser) throws DBException{
        databaseName = parser.getDatabaseName();
        try {
            FileIO fileIO = new FileIO(databaseName);
            //creates new folder and returns an empty database object
            database = fileIO.makeFolder(homeDirectory,databaseName);
            databaseMap.put(databaseName, database);
        } catch(DBException e){
            throw new FileException(e);
        }
    }

    public Exception getException() {
        return exception;
    }

    public boolean getInterpretedOK(){
        return interpretedOK;
    }

    public void setInterpretedOK(boolean interpretedOK){
        this.interpretedOK = interpretedOK;
    }
}
