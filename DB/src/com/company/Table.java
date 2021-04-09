package com.company;

import com.company.DBExceptions.*;

import java.util.ArrayList;
import java.util.Arrays;

public class Table {

    private final String databaseName;
    private String tableName;
    private int numberOfRows;
    private int numberOfColumns;
    private ArrayList<Row> rows;
    private ArrayList<Column> columns;

    public Table(String databaseName, String tableName)
            throws DBException {
        this.databaseName = databaseName;
        this.tableName = tableName;
    }

    public void addColumn(String columnName) throws EmptyData{
        if(columnName==null){
            throw new EmptyData(
                    "no column name passed to addColumn");
        }
        columns.add(new Column(tableName, columnName, numberOfColumns));
        numberOfColumns++;
        //loop through rows, add element to the end of each row
        for(int i=0; i<numberOfRows;i++){
            rows.get(i).addElement();
        }
    }

    public void deleteColumn(String columnName) throws EmptyData {
        if(columns==null || columnName==null){
            throw new EmptyData(
                    "couldn't delete column "+columnName);
        }
        int columnIndex = getColumnIndex(columnName);
        columns.remove(columnIndex);
        numberOfColumns--;
    }

    public void deleteElement(int rowIndex, String columnName)
            throws EmptyData {
        if(rows==null){
            throw new EmptyData("no rows to delete from");
        }
        int columnIndex = getColumnIndex(columnName);
        rows.get(rowIndex).deleteElement(columnIndex);
    }

    public void changeElement(String element,
                              int rowIndex, int columnIndex)
            throws DBException {
        if(rows==null){
            throw new EmptyData("no rows to add element to");
        }
        rows.get(rowIndex).setElement(element, columnIndex);
    }

    public void addEmptyRows(int rowsToAdd,
                             int columnsToAdd, boolean addID){
        if(rows==null){
            rows = new ArrayList<>();
        }
        if(addID) {
            while (numberOfRows < rowsToAdd) {
                numberOfRows++;
                rows.add(new Row(columnsToAdd, numberOfRows));
            }
        }else{
            while (numberOfRows < rowsToAdd) {
                numberOfRows++;
                rows.add(new Row(null, columnsToAdd));
            }
        }
    }

    public void deleteRow(int rowIndex) throws DBException{
        if(rows==null){
            throw new EmptyData("no rows to delete");
        }
        rows.remove(rowIndex);
        numberOfRows--;
    }

    //add a row that doesn't have an id in rowData
    public void addRow(ArrayList<String> rowData)
            throws DBException {
        int lastID;
        checkData(rowData);
        //checking if this is the first row
        if(rows.size()>0) {
            lastID = rows.get(numberOfRows-1).getID();
        }else{
            lastID = 0;
        }
        rows.add(new Row(rowData, numberOfColumns, lastID));
        numberOfRows++;
    }

    //add a row that already has the id in rowData
    public void addRowWithID(ArrayList<String> rowData) throws DBException {
        checkData(rowData);
        rows.add(new Row(rowData, numberOfColumns));
        numberOfRows++;
    }

    private void checkData(ArrayList<String> rowData)
            throws DBException{
        if(rowData==null) {
            throw new EmptyData("rowData in addRow in Table");
        }
        //check right number of values for columns
        if(rowData.size()>numberOfColumns){
            throw new CommandException(rowData.toString(),
                    rowData.size(), "check number of values/columns");
        }
        if(rows==null){
            rows = new ArrayList<>();
        }
    }

    public void fillTableFromMemory(
            ArrayList<String> columnNames,
            ArrayList<String> rowData, boolean addID){
        if(columnNames!=null){
            fillColumns(columnNames, addID);
        }
        if(rowData!=null){
            fillRows(rowData);
        }
    }

    private void fillRows(ArrayList<String> rowData){
        numberOfRows = rowData.size();
        rows = new ArrayList<>();
        int id = 0;
        for (int i = 1; i <= numberOfRows; i++) {
            ArrayList<String> currentRow = parseString(rowData.get(i));
            rows.add(new Row(currentRow, numberOfColumns, id));
            id++;
        }
    }

    private void fillColumns(
            ArrayList<String> columnNames, boolean addID){
        columns = new ArrayList<>();
        if(addID) {
            //add one for id
            numberOfColumns = columnNames.size()+1;
            columns.add(new Column(tableName, "id", 0));
            //then add the rest of the columns
            for (int i = 0; i < columnNames.size(); i++) {
                columns.add(new Column(tableName, columnNames.get(i), i+1));
            }
        }
        else{
            numberOfColumns = columnNames.size();
            for (int i = 0; i < columnNames.size(); i++) {
                columns.add(new Column(tableName, columnNames.get(i), i));
            }
        }
    }

    public void fillTableFromFile(ArrayList<String> dataFromFile)
            throws DBException {
        numberOfRows = initNumberOfRows(dataFromFile);
        numberOfColumns = initNumberOfColumns(dataFromFile.get(0));
        //getting column names, store in array of columns
        ArrayList<String> columnNames = parseString(dataFromFile.get(0));
        columns = new ArrayList<>();
        for (int i = 0; i < numberOfColumns; i++) {
            columns.add(new Column(tableName, columnNames.get(i), i));
        }
        //store data in array of rows
        rows = new ArrayList<>();
        for (int i = 1; i <= numberOfRows; i++) {
            ArrayList<String> currentRow = parseString(dataFromFile.get(i));
            rows.add(new Row(currentRow, numberOfColumns));
        }
    }

    public String getTable(){
        StringBuilder stringBuilder = new StringBuilder();
        FileIO fileIO = new FileIO(null);
        stringBuilder.append("Table name: ").append(tableName).append("\n");
        stringBuilder.append("From database: ").append(databaseName).append("\n");
        for(int i = 0; i < numberOfColumns; i++){
            stringBuilder.append(columns.get(i).getColumnName()).append("\t");
        }
        stringBuilder.append("\n");
        for(int j = 0; j < numberOfRows; j++){
            ArrayList<String> currentRow = rows.get(j).getElements();
            String formattedRow = fileIO.formatString(currentRow);
            stringBuilder.append(formattedRow);
            stringBuilder.append("\n");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public ArrayList<String> getColumnValues(int columnIndex)
            throws DBException {
        ArrayList<String> columnValues = new ArrayList<>();
        for(int i = 0; i < numberOfRows; i++){
            columnValues.add(rows.get(i).getElement(columnIndex));
        }
        return columnValues;
    }

    public int getColumnIndex(String columnName) throws EmptyData {
        for(int i = 0; i < numberOfColumns; i++){
            if(columns.get(i).getColumnName().equals(columnName)){
                return i;
            }
        }throw new EmptyData(
                "can't find column "+columnName+" in getColumnIndex");
    }

    public ArrayList<String> getSpecificRow(int index)
            throws IndexOutOfBounds{
        if(index <= numberOfRows && index >= 0) {
            return rows.get(index).getElements();
        }
        else{
            throw new IndexOutOfBounds(StorageType.ROW, index);
        }
    }

    public ArrayList<ArrayList<String>> getRows(){
        ArrayList<ArrayList<String>> rowString = new ArrayList<>();
        for(int i = 0; i < numberOfRows; i++){
            rowString.add(rows.get(i).getElements());
        }
        return rowString;
    }

    public ArrayList<String> getColumns(){
        ArrayList<String> columnString = new ArrayList<>();
        for(int i = 0; i < numberOfColumns; i++){
            columnString.add(columns.get(i).getColumnName());
        }
        return columnString;
    }

    private ArrayList<String> parseString(String rowToParse){
        String[] listOfWords = rowToParse.split("\t");
        ArrayList<String> arrayToList;
        arrayToList = new ArrayList<>(Arrays.asList(listOfWords));
        return arrayToList;
    }

    public String getTableName(){
        return tableName;
    }

    public void setRowID(int ID, int rowNumber)
            throws EmptyData {
        if(rowNumber <= numberOfRows){
            rows.get(rowNumber).setID(ID);
        }else{
            throw new EmptyData(
                    "setting row ID of row that doesn't exist");
        }
    }

    public void setTableName(String newTableName)
            throws EmptyName {
        if(newTableName!=null){
            tableName = newTableName;
        }
        else{
            throw new EmptyName(StorageType.TABLE, databaseName);
        }
    }

    private int initNumberOfRows(ArrayList<String> dataFromFile)
            throws EmptyData {
        if(dataFromFile!=null) {
            //minus two because the top line is column headers
            numberOfRows = dataFromFile.size() - 2;
            return numberOfRows;
        }
        else{
            throw new EmptyData(tableName);
        }
    }

    public int getNumberOfRows(){
        return numberOfRows;
    }

    private int initNumberOfColumns(String firstLine)
            throws EmptyName {
        if(firstLine!=null) {
            ArrayList<String> parsedString = parseString(firstLine);
            numberOfColumns = parsedString.size();
            return numberOfColumns;
        }
        else{
            throw new EmptyName(StorageType.COLUMN, tableName);
        }
    }

    public int getNumberOfColumns(){
        return numberOfColumns;
    }
}
