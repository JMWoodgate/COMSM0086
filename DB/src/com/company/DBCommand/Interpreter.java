package com.company.DBCommand;

import com.company.DBExceptions.*;
import com.company.Database;
import com.company.FileIO;
import com.company.Row;
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
    private final HashMap<String, Database> databaseMap;
    private Table table;
    private Table resultsTable;
    private ArrayList<String> valueListString;
    private boolean interpretedOK;
    private Exception exception;

    public Interpreter(String homeDirectory) throws DBException, IOException {
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
        return results;
    }

    private String interpretSelect(Parser parser) throws DBException, IOException {
        String results = null;
        getTableFromMemory(parser);
        attributeList = parser.getAttributeList();
        System.out.println("attribute list at start is "+attributeList);
        //if * and no condition, return the whole table
        if(attributeList.get(0).equals("*")&&!parser.getHasCondition()){
            results = table.getTable();
            return results;
        }
        //make a new results table and fill with column names
        resultsTable = new Table(databaseName, tableName);
        //if wild, table is the whole thing, otherwise need to fill with relevant attributes
        if(attributeList.get(0).equals("*")){
            //get every column name, then delete id
            attributeList = table.getColumns();
            attributeList.remove(0);
        }else {
            //checking that all the attributes in the query exist
            checkAttributes();
        }
        //fill table with all columns
        resultsTable.fillTableFromMemory(table.getColumns(), null, false);
        System.out.println("results table after being filled from memory is "+resultsTable.getTable());
        //if condition, need to throw out rows not matching
        if(parser.getHasCondition()) {
            //first get all the columns
            results = getSelectResults(resultsTable.getColumns(), true);
            System.out.println("results table after being filled from getSelectResults is "+resultsTable.getTable());
            //first get the rows that don't match the condition
            results = conditionalSelectResults(parser, attributeName);
            System.out.println("results from conditionalSelect "+results);
            System.out.println("attributeList is "+attributeList);
            //then get results from relevant columns
            results = removeUnselectedColumns(attributeList, resultsTable);
            System.out.println("results from removeUnselectedColumns "+results);
            return results;
        }else{
            //get results from relevant columns
            results = getSelectResults(attributeList, false);
        }
        return results;
    }

    private String removeUnselectedColumns(ArrayList<String> selectedAttributes,
                                           Table currentTable) throws EmptyData {
        ArrayList<String> existingColumns = currentTable.getColumns();
        for(int i=0; i<existingColumns.size();i++){
            //checking each column in our current table to see if it is in our selected attributes
            if(!attributeIsIn(existingColumns.get(i), selectedAttributes)){
                deleteColumn(i, currentTable);
            }
        }
        return currentTable.getTable();
    }

    private void deleteColumn(int columnIndex, Table currentTable) throws EmptyData {
        for(int i=0; i<currentTable.getNumberOfRows();i++){
            currentTable.deleteElement(i, columnIndex);
        }
        currentTable.deleteColumn(columnIndex);
    }

    private String conditionalSelectResults(Parser parser, String attributeName) throws DBException{
        String results = null;
        conditionListArray = parser.getConditionListArray();
        conditionListObject = parser.getConditionListObject();
        //do conditions need to be stored in a tree-like structure so that we can
        //evaluate them in the correct order?
        for(int i=0; i<conditionListObject.getConditionList().size();i++){
            //get one condition at a time
            Condition currentCondition = conditionListObject.getConditionList().get(i);
            //get the attribute name
            attributeName = currentCondition.getAttribute();
            System.out.println("attribute name in conditionalSelectResults "+attributeName);
            //get the condition variables
            String op = currentCondition.getOp();
            Value value = currentCondition.getValueObject();
            conditionSwitch(op, value);
        }
        results = resultsTable.getTable();
        return results;
    }

    private void conditionSwitch(String op, Value value)
            throws DBException{
        switch(op){
            case("=="):
                equal(value);
                break;
            case("!="):
                unequal(value);
                break;
            case("<"):
            case(">"):
            case("<="):
            case(">="):
            case("LIKE"):
            default:
                throw new EmptyData("problem with condition");
        }
    }

    private void unequal(Value value) throws DBException{
        int columnIndex = resultsTable.getColumnIndex(attributeName);
        String valueString = value.getValue();
        //for each row of the table, need to check the relevant column
        for(int i=0; i<resultsTable.getNumberOfRows();i++){
            ArrayList<String> currentRow = resultsTable.getSpecificRow(i);
            if(currentRow.get(columnIndex).equals(valueString)){
                resultsTable.deleteRow(i);
                i--;
            }
        }
    }

    private void equal(Value value) throws DBException{
        int columnIndex = resultsTable.getColumnIndex(attributeName);
        String valueString = value.getValue();
        //for each row of the table, need to check the relevant column
        for(int i=0; i<resultsTable.getNumberOfRows();i++){
            ArrayList<String> currentRow = resultsTable.getSpecificRow(i);
            if(!currentRow.get(columnIndex).equals(valueString)){
                resultsTable.deleteRow(i);
                i--;
            }
        }
    }

    private String getSelectResults(ArrayList<String> selectedAttributes, boolean hasID) throws DBException {
        String results;
        //get the first column values so that we know how many rows to make
        ArrayList<String> columnValues = table.getColumnValues(0);
        //add the number of rows to our list that we have values
        resultsTable.addEmptyRows(columnValues.size(), selectedAttributes.size(), false);
        //for each column in our selected list, get the values
        for (int j=0; j<selectedAttributes.size(); j++) {
            //need to get the selected column index from our table
            int columnIndex = table.getColumnIndex(selectedAttributes.get(j));
            //get the relevant column values
            columnValues = table.getColumnValues(columnIndex);
            //for each column value, need to populate the rows
            for(int i=0; i<columnValues.size();i++){
                resultsTable.addElement(columnValues.get(i), i, j);
            }
        }
        //if there is an ID column in the table, need to set the id in each row
        if(hasID){
            for(int i=0; i<columnValues.size();i++){
                resultsTable.setRowID(Integer.parseInt(columnValues.get(i)), i);
            }
        }
        results = resultsTable.getTable();
        return results;
    }

    private void checkAttributes() throws EmptyData {
        ArrayList<String> tableAttributes = table.getColumns();
        for(String attribute : attributeList){
            if(!attributeIsIn(attribute, tableAttributes)){
                throw new EmptyData("column '"+attribute+"' does not exist");
            }
        }
    }

    private boolean attributeIsIn(String attribute, ArrayList<String> tableAttributes){
        for(int i = 0; i < tableAttributes.size(); i++){
            if(attribute.equals(tableAttributes.get(i))){
                return true;
            }
        }
        return false;
    }

    private void interpretInsert(Parser parser) throws DBException, IOException {
        valueListString = parser.getValueListString();
        getTableFromMemory(parser);
        table.addRow(valueListString);
        //make a new file within specified database
        FileIO fileIO = new FileIO(databaseName);
        //writes to file (creates file if it doesn't exist)
        fileIO.writeFile(currentFolder, tableName, table);
    }

    private void getTableFromMemory(Parser parser) throws DBException{
        tableName = parser.getTableName();
        if(!database.getTables().containsKey(tableName)){
            throw new EmptyData("table does not exist in memory");
        }
        table = database.getTable(tableName);
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
                table.fillTableFromMemory(attributeList, null, true);
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
