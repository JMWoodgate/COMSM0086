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
    private ArrayList<String> valueListString;
    private ArrayList<Value> valueListObject;
    private ArrayList<Condition> conditionListArray;
    private ConditionList conditionListObject;
    private String currentFolder;
    private String parentFolder;
    private final String homeDirectory;
    private Database database;
    private final HashMap<String, Database> databaseMap;
    private Table table;
    private Table resultsTable;
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
                    interpretUpdate(parser);
                    break;
                case "delete":
                    interpretDelete(parser);
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

    private void interpretDelete(Parser parser) throws DBException, IOException{
        getTableFromMemory(parser);
        conditionListArray = parser.getConditionListArray();
        conditionListObject = parser.getConditionListObject();
        //get condition
        Condition currentCondition = conditionListObject.getConditionList().get(0);
        String conditionAttribute = currentCondition.getAttribute();
        String conditionOp = currentCondition.getOp();
        String conditionValue = currentCondition.getValueString();
        ArrayList<Integer> rowIndexes = findRowIndexes(
                conditionAttribute, conditionValue, conditionOp);
        //delete all rows in the rowIndexes
        for (int i=rowIndexes.size()-1; i>=0; i--) {
            table.deleteRow(rowIndexes.get(i));
        }
        updateFile();
    }

    private void interpretUpdate(Parser parser) throws DBException, IOException {
        getTableFromMemory(parser);
        //valueList and attributeList can be paired by index
        valueListObject = parser.getValueListObject();
        valueListString = parser.getValueListString();
        attributeList = parser.getAttributeList();
        conditionListObject = parser.getConditionListObject();
        conditionListArray = parser.getConditionListArray();
        //get condition
        Condition currentCondition = conditionListObject.getConditionList().get(0);
        String conditionAttribute = currentCondition.getAttribute();
        String conditionOp = currentCondition.getOp();
        String conditionValue = currentCondition.getValueString();
        //iterate through the attributes to change, changing those that meet the condition
        for(int i=0;i<attributeList.size();i++){
            changeValues(conditionAttribute,  conditionOp,
                    conditionValue, valueListString.get(i),
                    attributeList.get(i));
        }
        updateFile();
    }

    private void changeValues(String conditionAttribute, String conditionOp,
                              String conditionValue, String newValue,
                              String attributeToChange)
            throws DBException {
        //get all the row indexes that match the condition
        ArrayList<Integer> rowIndexes = findRowIndexes(
                conditionAttribute, conditionValue, conditionOp);
        //get the column index we're changing
        int columnIndex = table.getColumnIndex(attributeToChange);
        //for each row that matches condition, change the column value
        for (Integer rowIndex : rowIndexes) {
            table.changeElement(newValue, rowIndex, columnIndex);
        }
    }

    private ArrayList<Integer> findRowIndexes(
            String conditionAttribute, String conditionValue, String conditionOp)
            throws DBException {
        ArrayList<Integer> rowIndexes = new ArrayList<>();
        //iterate through columns until we find the target one, then iterate through rows
        for(int i=0; i<table.getNumberOfColumns();i++) {
            if (table.getColumns().get(i).equals(conditionAttribute)) {
                //get row indexes of rows that match the condition
                rowIndexes = rowsMatchingCondition(conditionValue, i, conditionOp);
            }
        }return rowIndexes;
    }

    private ArrayList<Integer> rowsMatchingCondition(
            String conditionValue, int columnIndex, String op)
            throws DBException {
        ArrayList<Integer> rowIndexes = new ArrayList<>();
        //iterates through one row at a time
        for(int i=0; i<table.getNumberOfRows();i++){
            ArrayList<String> currentRow = table.getSpecificRow(i);
            //if the value we're looking for exists at the column index of the current row,
            // return the row index
            if(op.equals("==")) {
                if (currentRow.get(columnIndex).equals(conditionValue)) {
                    rowIndexes.add(i);
                }
            }else if(op.equals("!=")) {
                if (!currentRow.get(columnIndex).equals(conditionValue)) {
                    rowIndexes.add(i);
                }
            }
        }return rowIndexes;
    }

    private String interpretSelect(Parser parser) throws DBException, IOException {
        String results = null;
        getTableFromMemory(parser);
        attributeList = parser.getAttributeList();
        //if * and no condition, return the whole table
        if(attributeList.get(0).equals("*")&&!parser.getHasCondition()){
            results = table.getTable();
            return results;
        }
        initResultsTable();
        //if condition, need to throw out rows not matching
        if(parser.getHasCondition()) {
            results = selectWithCondition(parser);
            return results;
        }else{
            //fill table with relevant columns
            resultsTable.fillTableFromMemory(attributeList, null, false);
            //get results from relevant columns
            results = selectColumns(attributeList, false);
            return results;
        }
    }

    private String selectWithCondition(Parser parser) throws DBException {
        String results;
        //create table with all columns
        resultsTable.fillTableFromMemory(table.getColumns(), null, false);
        //populate table with results from all columns
        selectColumns(resultsTable.getColumns(), true);
        //first get the rows that don't match the condition
        executeConditions(parser);
        //then remove the unselected columns
        results = removeUnselectedColumns(attributeList, resultsTable);
        //then change the values
        return results;
    }

    private String removeUnselectedColumns(ArrayList<String> selectedAttributes,
                                           Table currentTable) throws EmptyData {
        ArrayList<String> existingColumns = currentTable.getColumns();
        int i=0;
        while(i<existingColumns.size()&&i>=0){
            //checking each column in our current table to see if it is in our selected attributes
            if(!attributeIsIn(existingColumns.get(i), selectedAttributes)){
                deleteColumn(existingColumns.get(i), currentTable);
            }
            i++;
        }
        return currentTable.getTable();
    }

    private void deleteColumn(String columnName, Table currentTable) throws EmptyData {
        //deletes each element in the chosen column row by row
        for(int i=0; i<currentTable.getNumberOfRows();i++){
            currentTable.deleteElement(i, columnName);
        }
        currentTable.deleteColumn(columnName);
    }

    private void executeConditions(Parser parser) throws DBException{
        conditionListArray = parser.getConditionListArray();
        conditionListObject = parser.getConditionListObject();
        //do conditions need to be stored in a tree-like structure so that we can
        //evaluate them in the correct order?
        for(int i=0; i<conditionListObject.getConditionList().size();i++){
            //get one condition at a time
            Condition currentCondition = conditionListObject.getConditionList().get(i);
            //get the attribute name
            attributeName = currentCondition.getAttribute();
            //get the condition variables
            String op = currentCondition.getOp();
            Value value = currentCondition.getValueObject();
            conditionSwitch(op, value);
        }
    }

    private void conditionSwitch(String op, Value value)
            throws DBException{
        switch(op){
            case("=="):
            case("!="):
                equalOrUnequal(value, op);
                break;
            case("<"):
            case(">"):
            case("<="):
            case(">="):
                greaterOrLess(value, op);
                break;
            case("LIKE"):
            default:
                throw new EmptyData("problem with condition");
        }
    }

    private void greaterOrLess(Value value, String op) throws DBException{
        int columnIndex = resultsTable.getColumnIndex(attributeName);
        LiteralType type = value.getLiteralType();
        if(type==LiteralType.INTEGER||type==LiteralType.FLOAT){
            for(int i=0; i<resultsTable.getNumberOfRows();i++){
                ArrayList<String> currentRow = resultsTable.getSpecificRow(i);
                int currentInt = Integer.parseUnsignedInt(currentRow.get(columnIndex));
                i = greaterOrLessSwitch(value, currentInt, op, i);
            }
        }else{
            throw new EmptyData("not a valid condition for value type");
        }
    }

    private int greaterOrLessSwitch(Value value, int currentInt,
                                     String op, int rowIndex) throws DBException{
        switch(op){
            case("<"):
                if(currentInt>=value.getIntLiteral()){
                    resultsTable.deleteRow(rowIndex);
                    rowIndex--;
                }
                break;
            case(">"):
                if(currentInt<=value.getIntLiteral()){
                    resultsTable.deleteRow(rowIndex);
                    rowIndex--;
                }
                break;
            case("<="):
                if(currentInt>value.getIntLiteral()){
                    resultsTable.deleteRow(rowIndex);
                    rowIndex--;
                }
                break;
            case(">="):
                if(currentInt<value.getIntLiteral()){
                    resultsTable.deleteRow(rowIndex);
                    rowIndex--;
                }
                break;
            default:
                throw new EmptyData("did not expect operator "+op);
        }
        return rowIndex;
    }

    private void equalOrUnequal(Value value, String op) throws DBException{
        System.out.println("entered equalOrUnequal");
        int columnIndex = resultsTable.getColumnIndex(attributeName);
        String valueString = value.getValue();
        //for each row of the table, need to check the relevant column
        for(int i=0; i<resultsTable.getNumberOfRows();i++){
            ArrayList<String> currentRow = resultsTable.getSpecificRow(i);
            i = equalOrUnequalSwitch(currentRow, valueString, op, columnIndex, i);
            System.out.println("table now: "+resultsTable.getTable());
        }
    }

    private int equalOrUnequalSwitch(ArrayList<String> currentRow, String valueString,
                                      String op, int columnIndex, int rowIndex) throws DBException{
        switch(op){
            case("=="):
                if(!currentRow.get(columnIndex).equals(valueString)){
                    System.out.println("deleting row "+resultsTable.getSpecificRow(rowIndex));
                    resultsTable.deleteRow(rowIndex);
                    rowIndex--;
                }
                break;
            case("!="):
                if(currentRow.get(columnIndex).equals(valueString)){
                    resultsTable.deleteRow(rowIndex);
                    rowIndex--;
                }
                break;
            default:
                throw new EmptyData("did not expect op "+op);
        }
        return rowIndex;
    }

    private String selectColumns(ArrayList<String> selectedAttributes, boolean hasID)
            throws DBException {
        String results;
        //get the first column's values so that we know how many rows to make
        ArrayList<String> columnValues = table.getColumnValues(0);
        //add the number of rows to our list that we have values
        resultsTable.addEmptyRows(columnValues.size(), selectedAttributes.size(), false);
        //for each column in our selected list, get the values
        for (int i=0; i<selectedAttributes.size(); i++) {
            //need to get the selected column's index from our table
            int columnIndex = table.getColumnIndex(selectedAttributes.get(i));
            //get the relevant column values
            columnValues = table.getColumnValues(columnIndex);
            populateRows(columnValues, i);
        }
        if(hasID){
            setIDs(columnValues);
        }
        results = resultsTable.getTable();
        return results;
    }

    //for each column value, populate the rows
    private void populateRows(ArrayList<String> columnValues, int columnIndex)
            throws DBException {
        for(int i=0; i<columnValues.size();i++){
            resultsTable.changeElement(columnValues.get(i), i, columnIndex);
        }
    }

    //if there is an ID column in the table, need to set the id in each row for later reference
    private void setIDs(ArrayList<String> columnValues) throws DBException {
        ArrayList<String> idColumn = table.getColumnValues(0);
        for(int i=0;i<columnValues.size();i++){
            resultsTable.setRowID(Integer.parseInt(idColumn.get(i)), i);
        }
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
        for (String tableAttribute : tableAttributes) {
            if (attribute.equals(tableAttribute)) {
                return true;
            }
        }
        return false;
    }

    private void interpretInsert(Parser parser) throws DBException, IOException {
        valueListString = parser.getValueListString();
        getTableFromMemory(parser);
        table.addRow(valueListString);
        updateFile();
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

    private void updateFile() throws DBException, IOException{
        //make a new file within specified database
        FileIO fileIO = new FileIO(databaseName);
        //writes to file (creates file if it doesn't exist)
        fileIO.writeFile(currentFolder, tableName, table);
    }

    private void initResultsTable() throws DBException {
        //make a new results table and fill with column names
        resultsTable = new Table(databaseName, tableName);
        //if wild, table is the whole thing, otherwise need to fill with relevant attributes
        if(attributeList.get(0).equals("*")){
            //get every column name
            attributeList = table.getColumns();
        }else {
            //checking that all the attributes in the query exist
            checkAttributes();
        }
    }

    private void getTableFromMemory(Parser parser) throws DBException{
        tableName = parser.getTableName();
        if(!database.getTables().containsKey(tableName)){
            throw new EmptyData("table does not exist in memory");
        }
        table = database.getTable(tableName);
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
