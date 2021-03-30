package com.company.DBCommand;

import com.company.DBExceptions.*;
import com.company.Database;
import com.company.FileIO;
import com.company.Table;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {

    protected int index;
    private String tableName;
    private String databaseName;
    private String attributeName;
    private ArrayList<String> attributeList;
    private ArrayList<String> valueListString;
    private ConditionList conditionListObject;
    private String currentFolder;
    private final String homeDirectory;
    private Database database;
    private final HashMap<String, Database> databaseMap;
    private Table table;
    private Table resultsTable;
    private boolean interpretedOK;
    private Exception exception;
    private boolean multipleConditions;
    private int commandIndex;
    private int conditionIndex;

    public Interpreter(String homeDirectory) throws DBException, IOException {
        this.homeDirectory = homeDirectory;
        database = new Database(homeDirectory);
        FileIO fileIO = new FileIO(homeDirectory);
        databaseMap = fileIO.readAllFolders(homeDirectory);
        interpretedOK = true;
    }

    public String interpretCommand(String command, Parser parser) {
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
                    interpretAlter(parser);
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
                    results = interpretJoin(parser);
                    break;
                default:
            }
        } catch(DBException | IOException e){
            interpretedOK = false;
            exception = e;
        }
        return results;
    }

    private String interpretJoin(Parser parser) throws DBException{
        getTableFromMemory(parser);
        String secondTableName = parser.getSecondTableName();
        if(!database.getTables().containsKey(secondTableName)){
            throw new EmptyData("table does not exist in memory");
        }
        Table secondTable = database.getTable(secondTableName);
        String firstAttribute = parser.getAttributeList().get(0);
        String secondAttribute = parser.getAttributeList().get(1);
        return joinTables(table, secondTable, firstAttribute, secondAttribute);
    }

    private String joinTables(
            Table firstTable, Table secondTable, String firstAttribute, String secondAttribute)
            throws DBException{
        Table resultsTable = new Table(databaseName, "join table");
        //get the column names from each table, remove the index, and add on the table name
        ArrayList<String> table1Columns = formatColumnNames(firstTable);
        ArrayList<String> table2Columns = formatColumnNames(secondTable);
        //putting all column names together
        table1Columns.addAll(table2Columns);
        //creating an empty results table
        resultsTable.fillTableFromMemory(table1Columns, null, true);
        //get join column indexes
        int firstAttributeIndex = firstTable.getColumnIndex(firstAttribute);
        int secondAttributeIndex = secondTable.getColumnIndex(secondAttribute);
        //get indexes of matching rows
        ArrayList<ArrayList<Integer>> rowIndexes = findRowIndexes(
                firstAttributeIndex, secondAttributeIndex, firstTable, secondTable);
        //append rows from each table and add to results table
        for (ArrayList<Integer> rowIndex : rowIndexes) {
            ArrayList<String> currentRow = joinRows(
                    rowIndex, firstTable, secondTable);
            resultsTable.addRow(currentRow);
        }
        return resultsTable.getTable();
    }

    private ArrayList<String> joinRows(
            ArrayList<Integer> rowIndexes, Table firstTable, Table secondTable)
            throws DBException{
        ArrayList<String> temp = firstTable.getSpecificRow(rowIndexes.get(0));
        ArrayList<String> firstRow = new ArrayList<>();
        //copy from index 1 onwards as we don't want the id column
        for(int i=1; i<temp.size();i++){
            firstRow.add(temp.get(i));
        }
        temp = secondTable.getSpecificRow(rowIndexes.get(1));
        ArrayList<String> secondRow = new ArrayList<>();
        for(int i=1; i<temp.size();i++){
            firstRow.add(temp.get(i));
        }
        firstRow.addAll(secondRow);
        return firstRow;
    }

    private ArrayList<ArrayList<Integer>> findRowIndexes(
            int firstAttribute, int secondAttribute, Table firstTable, Table secondTable)
            throws DBException {
        ArrayList<String> firstColumn = firstTable.getColumnValues(firstAttribute);
        ArrayList<String> secondColumn = secondTable.getColumnValues(secondAttribute);
        ArrayList<ArrayList<Integer>> rowIndexes = new ArrayList<>();
        //for each row in the first table's column, loop through all the columns
        //in the second table to compare
        for(int i=0; i<firstColumn.size();i++){
            rowIndexes = getMatchingRows(firstColumn.get(i), secondColumn, rowIndexes, i);
        }
        return rowIndexes;
    }

    private ArrayList<ArrayList<Integer>> getMatchingRows(
            String currentValue, ArrayList<String> comparisonRow,
            ArrayList<ArrayList<Integer>> rowIndexes, int comparisonRowIndex){
        //for the current row in table 1, loop through the values in table 2
        //if there is a matching value, pair the indexes together
        // and store in a 2D arrayList
        for(int i=0;i<comparisonRow.size();i++){
            if(comparisonRow.get(i).equals(currentValue)) {
                ArrayList<Integer> currentIndex = new ArrayList<>();
                currentIndex.add(comparisonRowIndex);
                currentIndex.add(i);
                rowIndexes.add(currentIndex);
            }
        }
        return rowIndexes;
    }

    private ArrayList<String> formatColumnNames(Table currentTable){
        ArrayList<String> columnNames = currentTable.getColumns();
        ArrayList<String> formattedColumns = new ArrayList<>();
        //add table names not including id
        for (int i=1;i<columnNames.size();i++) {
            formattedColumns.add(currentTable.getTableName()+
                    "."+columnNames.get(i));
        }
        return formattedColumns;
    }

    private void interpretAlter(Parser parser)
            throws DBException, IOException{
        getTableFromMemory(parser);
        attributeName = parser.getAttributeName();
        AlterationType alterationType = parser.getAlterationType();
        if(alterationType==AlterationType.ADD){
            table.addColumn(attributeName);
        }else if(alterationType==AlterationType.DROP){
            deleteColumn(attributeName, table);
        }
        updateFile();
    }

    private void interpretDelete(Parser parser)
            throws DBException, IOException{
        getTableFromMemory(parser);
        conditionListObject = parser.getConditionListObject();
        //get condition
        Condition currentCondition = conditionListObject.getConditionList().get(0);
        Value value = currentCondition.getValueObject();
        String conditionAttribute = currentCondition.getAttribute();
        String conditionOp = currentCondition.getOp();
        ArrayList<Integer> rowIndexes = new ArrayList<>();
        Stack<Condition> conditionStack = new Stack<>();
        if(!multipleConditions){
            rowIndexes = findRowIndexes(
                    conditionAttribute, value, conditionOp);
        }else{
            rowIndexes = multipleConditions(parser, conditionStack, rowIndexes);
        }
        //delete all rows in the rowIndexes
        for (int i=rowIndexes.size()-1; i>=0; i--) {
            table.deleteRow(rowIndexes.get(i));
        }
        updateFile();
    }

    private void interpretUpdate(Parser parser)
            throws DBException, IOException {
        getTableFromMemory(parser);
        //valueList and attributeList can be paired by index
        valueListString = parser.getValueListString();
        attributeList = parser.getAttributeList();
        conditionListObject = parser.getConditionListObject();
        //iterate through the attributes to change, changing those that meet the condition
        for(int i=0;i<attributeList.size();i++){
            changeValues(valueListString.get(i),
                    attributeList.get(i), parser);
        }
        updateFile();
    }

    private void changeValues(String newValue,
                              String attributeToChange, Parser parser)
            throws DBException {
        Stack<Condition> conditionStack = new Stack<>();
        ArrayList<Integer> rowIndexes = new ArrayList<>();
        //get all the row indexes that match the condition
        if(!multipleConditions) {
            Condition currentCondition = conditionListObject.getConditionList().get(0);
            Value conditionValue = currentCondition.getValueObject();
            String conditionAttribute = currentCondition.getAttribute();
            String conditionOp = currentCondition.getOp();
            rowIndexes = findRowIndexes(
                    conditionAttribute, conditionValue, conditionOp);
        }else{
            rowIndexes = multipleConditions(parser, conditionStack, rowIndexes);
        }
        //get the column index we're changing
        int columnIndex = table.getColumnIndex(attributeToChange);
        //for each row that matches condition, change the column value
        for (Integer rowIndex : rowIndexes) {
            table.changeElement(newValue, rowIndex, columnIndex);
        }
    }

    private ArrayList<Integer> findRowIndexes(
            String attribute, Value value, String op)
            throws DBException {
        ArrayList<Integer> rowIndexes = new ArrayList<>();
        //iterate through columns until we find the target one, then iterate through rows
        for(int i=0; i<table.getNumberOfColumns();i++) {
            if (table.getColumns().get(i).equals(attribute)) {
                //get row indexes of rows that match the condition
                rowIndexes = rowsMatchingCondition(value, i, op);
            }
        }return rowIndexes;
    }

    private ArrayList<Integer> rowsMatchingCondition(
            Value value, int columnIndex, String op)
            throws DBException {
        ArrayList<Integer> rowIndexes = new ArrayList<>();
        //iterates through one row at a time
        for(int i=0; i<table.getNumberOfRows();i++){
            ArrayList<String> currentRow = table.getSpecificRow(i);
            //get indexes of each row that match condition
            rowIndexes = conditionSwitch(op, value, currentRow.get(columnIndex),
                    i, rowIndexes);
        }return rowIndexes;
    }

    private ArrayList<Integer> conditionSwitch(
            String op, Value value, String element, int rowIndex,
            ArrayList<Integer> rowIndexes)
            throws DBException{
        switch(op){
            case("=="):
            case("!="):
                rowIndexes=equalOrUnequal(op, value, element, rowIndex, rowIndexes);
                break;
            case("<"):
            case(">"):
            case("<="):
            case(">="):
                rowIndexes=greaterOrLess(value, op, element, rowIndex, rowIndexes);
                break;
            case("like"):
                rowIndexes=likeComparison(value, element, rowIndex, rowIndexes);
                break;
            default:
                throw new EmptyData("problem with condition");
        }
        return rowIndexes;
    }

    private ArrayList<Integer> likeComparison(
            Value value, String element, int rowIndex,
            ArrayList<Integer> rowIndexes)
            throws DBException {
        if(value.getLiteralType()!=LiteralType.STRING){
            throw new CommandException(value.getValue(), index, "string");
        }
        String valueString = removeQuotes(value);
        Pattern pattern = Pattern.compile(valueString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(element);
        boolean matchFound = matcher.find();
        if(matchFound){
            rowIndexes.add(rowIndex);
        }
        return rowIndexes;
    }

    private ArrayList<Integer> greaterOrLess(
            Value value, String op, String element, int rowIndex,
            ArrayList<Integer> rowIndexes)
            throws DBException{
        LiteralType type = value.getLiteralType();
        if(type==LiteralType.INTEGER||type==LiteralType.FLOAT){
            rowIndexes = parseNumber(value, op, element, rowIndex, rowIndexes);
        }
        return rowIndexes;
    }

    private ArrayList<Integer> parseNumber(
            Value value, String op, String element, int rowIndex,
            ArrayList<Integer> rowIndexes) throws DBException{
        if(element!=null){
            try {
                float currentNumber = Float.parseFloat(element);
                rowIndexes = greaterOrLessSwitch(value, currentNumber, op, rowIndex, rowIndexes);
            }catch(Exception e){
                throw new CommandException(value.toString(), rowIndex, "a number");
            }
        }
        return rowIndexes;
    }

    private ArrayList<Integer> greaterOrLessSwitch
            (Value value, float currentNumber, String op, int rowIndex,
             ArrayList<Integer> rowIndexes)
            throws DBException{
        switch(op){
            case("<"):
                if(currentNumber<value.getFloatLiteral()){
                    rowIndexes.add(rowIndex);
                }
                break;
            case(">"):
                if(currentNumber>value.getFloatLiteral()){
                    rowIndexes.add(rowIndex);
                }
                break;
            case("<="):
                if(currentNumber<=value.getFloatLiteral()){
                    rowIndexes.add(rowIndex);
                }
                break;
            case(">="):
                if(currentNumber>=value.getFloatLiteral()){
                    rowIndexes.add(rowIndex);
                }
                break;
            default:
                throw new EmptyData("did not expect operator "+op);
        }
        return rowIndexes;
    }

    private ArrayList<Integer> equalOrUnequal
            (String op, Value value, String element, int rowIndex,
             ArrayList<Integer> rowIndexes)
            throws DBException{
        switch(op){
            case("=="):
                rowIndexes = equal(value.getValue(), element, rowIndex, rowIndexes);
                break;
            case("!="):
                rowIndexes = unequal(value.getValue(), element, rowIndex, rowIndexes);
                break;
            default:
                throw new EmptyData("did not expect op "+op);
        }
        return rowIndexes;
    }

    private ArrayList<Integer> unequal
            (String value, String element, int rowIndex,
             ArrayList<Integer> rowIndexes) {
        if (!element.equals(value)) {
            rowIndexes.add(rowIndex);
        }
        return rowIndexes;
    }


    private ArrayList<Integer> equal
            (String value, String element, int rowIndex,
             ArrayList<Integer> rowIndexes) {
        if (element.equals(value)) {
            rowIndexes.add(rowIndex);
        }
        return rowIndexes;
    }


    private String interpretSelect(Parser parser)
            throws DBException, IOException {
        String results;
        getTableFromMemory(parser);
        attributeList = parser.getAttributeList();
        multipleConditions = parser.isMultipleConditions();
        //if * and no condition, return the whole table
        if(attributeList.get(0).equals("*")&&!parser.getHasCondition()){
            results = table.getTable();
            return results;
        }
        initResultsTable();
        //if condition, need to throw out rows not matching
        if(parser.getHasCondition()) {
            results = selectWithCondition(parser);
        }else{
            //fill table with relevant columns
            resultsTable.fillTableFromMemory(attributeList, null, false);
            //get results from relevant columns
            results = selectColumns(attributeList, false);
        }
        return results;
    }

    private String selectWithCondition(Parser parser)
            throws DBException {
        //create table with all columns
        resultsTable.fillTableFromMemory(table.getColumns(), null, false);
        if(!multipleConditions) {
            //populate table with results from all columns
            selectColumns(resultsTable.getColumns(), true);
            //first get the rows that don't match the condition
            singleCondition(parser);
        }else{
            conditionListObject = parser.getConditionListObject();
            commandIndex = 0;
            conditionIndex = 0;
            Stack<Condition> conditionStack = new Stack<>();
            ArrayList<Integer> rowIndexes = new ArrayList<>();
            rowIndexes = multipleConditions(parser, conditionStack, rowIndexes);
            for (Integer rowIndex : rowIndexes) {
                resultsTable.addRowWithID(table.getSpecificRow(rowIndex));
            }
        }
        //then remove the unselected columns
        return removeUnselectedColumns(attributeList, resultsTable);
    }

    private String removeUnselectedColumns(
            ArrayList<String> selectedAttributes,
            Table currentTable) throws EmptyData {
        ArrayList<String> existingColumns = currentTable.getColumns();
        int i=0;
        while(i<existingColumns.size()&&i>=0){
            //checking each column in our current table to see if it is in our selected attributes
            if(!selectedAttributes.contains(existingColumns.get(i))){
                deleteColumn(existingColumns.get(i), currentTable);
            }
            i++;
        }
        return currentTable.getTable();
    }

    private void deleteColumn(String columnName, Table currentTable)
            throws EmptyData {
        //deletes each element in the chosen column row by row
        for(int i=0; i<currentTable.getNumberOfRows();i++){
            currentTable.deleteElement(i, columnName);
        }
        currentTable.deleteColumn(columnName);
    }

    private void singleCondition(Parser parser) throws DBException{
        conditionListObject = parser.getConditionListObject();
        //get condition
        Condition currentCondition = conditionListObject.getConditionList().get(0);
        //get the attribute name
        attributeName = currentCondition.getAttribute();
        //get the condition variables
        String op = currentCondition.getOp();
        Value value = currentCondition.getValueObject();
        selectConditionSwitch(op, value);
    }

    private ArrayList<Integer> multipleConditions(
            Parser parser, Stack<Condition> conditionStack,
            ArrayList<Integer> rowIndexes)
            throws DBException{
        ArrayList<String> command = parser.getTokenizedCommand();
        while(commandIndex<command.size()&&!command.get(commandIndex).equals(";")){
            //iterate through command until next condition
            if(command.get(commandIndex).equals(")")){
                commandIndex++;
                //checking for AND or OR
                rowIndexes = andOr(command, parser, conditionStack, rowIndexes);
                if(commandIndex>=command.size()){
                    return rowIndexes;
                }
            }
            if(command.get(commandIndex).equals("(")){
                conditionStack = pushConditions(command, conditionStack);
                //pop conditions off the stack
                Condition currentCondition = conditionStack.pop();
                attributeName = currentCondition.getAttribute();
                //get the condition variables
                String op = currentCondition.getOp();
                Value value = currentCondition.getValueObject();
                rowIndexes = findRowIndexes(attributeName, value, op);
            }
            commandIndex++;
        }
        return rowIndexes;
    }

    private Stack<Condition> pushConditions(
            ArrayList<String> command, Stack<Condition> conditionStack)
            throws DBException{
        while(command.get(commandIndex).equals("(")&&
                conditionIndex<conditionListObject.getConditionList().size()){
            //push nested conditions onto the stack
            conditionStack.push(conditionListObject.getConditionList().get(conditionIndex));
            conditionIndex++;
            commandIndex++;
        }
        return conditionStack;
    }

    private ArrayList<Integer> andOr(
            ArrayList<String> command, Parser parser,
            Stack<Condition> conditionStack,
            ArrayList<Integer> rowIndexes)
            throws DBException{
        if(command.get(commandIndex).equals("and")){
            //call recursively, compare and update row indexes
            ArrayList<Integer> newIndexes = multipleConditions(
                    parser, conditionStack, rowIndexes);
            rowIndexes = andIndexes(rowIndexes, newIndexes);
        }else if(command.get(commandIndex).equals("or")){
            //compare row indexes, concat and get rid of duplicates
            ArrayList<Integer> newIndexes = multipleConditions(
                    parser, conditionStack, rowIndexes);
            rowIndexes = orIndexes(rowIndexes, newIndexes);
        }
        return rowIndexes;
    }

    private ArrayList<Integer> andIndexes(
            ArrayList<Integer> firstIndexes,
            ArrayList<Integer> secondIndexes){
        ArrayList<Integer> results = new ArrayList<>();
        if(firstIndexes==null&&secondIndexes==null){
            return results;
        }
        if(firstIndexes==null){
            return secondIndexes;
        }
        if(secondIndexes==null){
            return firstIndexes;
        }
        for (Integer firstIndex : firstIndexes) {
            if (secondIndexes.contains(firstIndex)&&!results.contains(firstIndex)) {
                results.add(firstIndex);
            }
        }
        return results;
    }

    private ArrayList<Integer> orIndexes(
            ArrayList<Integer> firstIndexes,
            ArrayList<Integer> secondIndexes){
        if(firstIndexes==null&&secondIndexes==null){
            return null;
        }
        if(firstIndexes==null){
            return secondIndexes;
        }
        if(secondIndexes==null){
            return firstIndexes;
        }
        for(int i=0;i<firstIndexes.size();i++){
            if(secondIndexes.contains(firstIndexes.get(i))){
                firstIndexes.remove(i);
            }
        }
        firstIndexes.addAll(secondIndexes);
        return firstIndexes;
    }

    private void selectConditionSwitch(String op, Value value)
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
            case("like"):
                likeComparison(value);
                break;
            default:
                throw new EmptyData("problem with condition");
        }
    }

    private void likeComparison(Value value)
            throws DBException {
        if(value.getLiteralType()!=LiteralType.STRING){
            throw new CommandException(value.getValue(), index, "string");
        }
        int columnIndex = resultsTable.getColumnIndex(attributeName);
        //get rid of quotes
        String valueString = removeQuotes(value);
        //for each row of the table, need to check the relevant column
        for(int i=0; i<resultsTable.getNumberOfRows();i++){
            ArrayList<String> currentRow = resultsTable.getSpecificRow(i);
            String element = currentRow.get(columnIndex);
            Pattern pattern = Pattern.compile(valueString, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(element);
            boolean matchFound = matcher.find();
            if(!matchFound){
                resultsTable.deleteRow(i);
                i--;
            }
        }
    }

    private String removeQuotes(Value value)
            throws EmptyData {
        StringBuilder formatString = new StringBuilder(value.getValue());
        formatString.deleteCharAt(0);
        formatString.deleteCharAt(formatString.length()-1);
        return formatString.toString();
    }

    private void greaterOrLess(Value value, String op)
            throws DBException{
        LiteralType type = value.getLiteralType();
        if(type==LiteralType.INTEGER||type==LiteralType.FLOAT){
            for(int i=0; i<resultsTable.getNumberOfRows();i++){
                i = parseNumber(i, value, op);
            }
        }else{
            throw new EmptyData("not a valid condition for value type");
        }
    }

    private int parseNumber(int rowIndex, Value value, String op)
            throws DBException{
        int columnIndex = resultsTable.getColumnIndex(attributeName);
        ArrayList<String> currentRow = resultsTable.getSpecificRow(rowIndex);
        if(currentRow.get(columnIndex)!=null){
            try {
                float currentNumber = Float.parseFloat(currentRow.get(columnIndex));
                rowIndex = greaterOrLessSwitch(value, currentNumber, op, rowIndex);
            }catch(Exception e){
                throw new CommandException(currentRow.get(columnIndex), index, "a number");
            }
        }
        return rowIndex;
    }

    private int greaterOrLessSwitch(Value value, float currentNumber,
                                     String op, int rowIndex)
            throws DBException{
        switch(op){
            case("<"):
                if(currentNumber>=value.getFloatLiteral()){
                    resultsTable.deleteRow(rowIndex);
                    rowIndex--;
                }
                break;
            case(">"):
                if(currentNumber<=value.getFloatLiteral()){
                    resultsTable.deleteRow(rowIndex);
                    rowIndex--;
                }
                break;
            case("<="):
                if(currentNumber>value.getFloatLiteral()){
                    resultsTable.deleteRow(rowIndex);
                    rowIndex--;
                }
                break;
            case(">="):
                if(currentNumber<value.getFloatLiteral()){
                    resultsTable.deleteRow(rowIndex);
                    rowIndex--;
                }
                break;
            default:
                throw new EmptyData("did not expect operator "+op);
        }
        return rowIndex;
    }

    private void equalOrUnequal(Value value, String op)
            throws DBException{
        int columnIndex = resultsTable.getColumnIndex(attributeName);
        String valueString = value.getValue();
        //for each row of the table, need to check the relevant column
        for(int i=0; i<resultsTable.getNumberOfRows();i++){
            ArrayList<String> currentRow = resultsTable.getSpecificRow(i);
            i = equalOrUnequalSwitch(currentRow, valueString, op, columnIndex, i);
        }
    }

    private int equalOrUnequalSwitch(
            ArrayList<String> currentRow, String valueString,
            String op, int columnIndex, int rowIndex)
            throws DBException{
        switch(op){
            case("=="):
                if(!currentRow.get(columnIndex).equals(valueString)){
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

    private String selectColumns(
            ArrayList<String> selectedAttributes, boolean hasID)
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
    private void populateRows(
            ArrayList<String> columnValues, int columnIndex)
            throws DBException {
        for(int i=0; i<columnValues.size();i++){
            resultsTable.changeElement(columnValues.get(i), i, columnIndex);
        }
    }

    //if there is an ID column in the table, need to set the id in each row
    // for later reference
    private void setIDs(
            ArrayList<String> columnValues)
            throws DBException {
        ArrayList<String> idColumn = table.getColumnValues(0);
        for(int i=0;i<columnValues.size();i++){
            resultsTable.setRowID(Integer.parseInt(idColumn.get(i)), i);
        }
    }

    private void checkAttributes() throws EmptyData {
        ArrayList<String> tableAttributes = table.getColumns();
        for(String attribute : attributeList){
            if(!tableAttributes.contains(attribute)){
                throw new EmptyData("column '"+attribute+"' does not exist");
            }
        }
    }

    private void interpretInsert(Parser parser)
            throws DBException, IOException {
        valueListString = parser.getValueListString();
        getTableFromMemory(parser);
        table.addRow(valueListString);
        updateFile();
    }

    private void interpretDrop(Parser parser)
            throws DBException {
        StorageType type = parser.getStorageType();
        if(type==StorageType.DATABASE){
            dropDatabase(parser);
        }
        else if(type==StorageType.TABLE) {
            dropTable(parser);
        }
    }

    private void dropTable(Parser parser)
            throws DBException{
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

    private void dropDatabase(Parser parser)
            throws DBException {
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

    private void interpretUse(Parser parser)
            throws DBException {
        //gets the name of the database
        databaseName = parser.getDatabaseName();
        //gets the relative pathname to the database
        currentFolder = parser.getCurrentFolder();
        if(!databaseMap.containsKey(databaseName)){
            throw new EmptyData("database does not exist");
        }
        database = databaseMap.get(databaseName);
    }

    private void interpretCreate(Parser parser)
            throws DBException{
        StorageType type = parser.getStorageType();
        if(type==StorageType.DATABASE) {
            createDatabase(parser);
        }else if(type==StorageType.TABLE) {
            createTable(parser);
        }
    }

    private void createTable(Parser parser)
            throws DBException{
        tableName = parser.getTableName();
        attributeList = parser.getAttributeList();
        try{
            //make a new file within specified database
            FileIO fileIO = new FileIO(databaseName);
            //creates a new table within a specified folder (returns error if file already exists)
            fileIO.makeFile(currentFolder, tableName);
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

    private void createDatabase(Parser parser)
            throws DBException{
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

    private void updateFile()
            throws DBException, IOException{
        //make a new file within specified database
        FileIO fileIO = new FileIO(databaseName);
        //writes to file (creates file if it doesn't exist)
        fileIO.writeFile(currentFolder, tableName, table);
    }

    private void initResultsTable()
            throws DBException {
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

    private void getTableFromMemory(Parser parser)
            throws DBException{
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
}
