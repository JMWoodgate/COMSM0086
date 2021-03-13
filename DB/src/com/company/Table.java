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

    public Table(String databaseName, String tableName,
                 ArrayList<String> dataFromFile) throws DBException {
        this.databaseName = databaseName;
        this.tableName = tableName;
        numberOfRows = initNumberOfRows(dataFromFile);
        numberOfColumns = initNumberOfColumns(dataFromFile.get(0));
        fillTable(dataFromFile);
    }

    private void fillTable(ArrayList<String> dataFromFile)
            throws EmptyData{
        if(dataFromFile!=null) {
            //getting column names, then storing in an array of columns
            ArrayList<String> columnNames = parseString(dataFromFile.get(0));
            columns = new ArrayList<>();
            for (int i = 0; i < numberOfColumns; i++) {
                columns.add(new Column(tableName, columnNames.get(i), i));
            }

            //storing data in array of rows
            rows = new ArrayList<>();
            for (int i = 1; i <= numberOfRows; i++) {
                ArrayList<String> currentRow = parseString(dataFromFile.get(i));
                rows.add(new Row(tableName, currentRow, numberOfColumns));
            }
        }
        else{
            throw new EmptyData(tableName);
        }
    }

    public void printTable(){
        for(int i = 0; i < numberOfColumns; i++){
            System.out.print(columns.get(i).getColumnName() + " ");
        }
        System.out.println();
        for(int i = 0; i < numberOfRows; i++){
            System.out.println(rows.get(i).getElements());
        }
    }

    public String getElement(int rowNumber, int columnNumber)
            throws IndexOutOfBounds{
        if(rowNumber >= 0 && rowNumber <= numberOfRows) {
            if(columnNumber >= 0 && columnNumber <= numberOfColumns){
                return rows.get(rowNumber).getElement(columnNumber);
            }
            else{
                throw new IndexOutOfBounds(StorageType.COLUMN, columnNumber);
            }
        }
        else{
            throw new IndexOutOfBounds(StorageType.ROW, rowNumber);
        }
    }

    public String getSpecificColumn(int index)
            throws IndexOutOfBounds{
        if(index <= numberOfColumns && index >= 0) {
            return columns.get(index).getColumnName();
        }
        else{
            throw new IndexOutOfBounds(StorageType.COLUMN, index);
        }
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

    public ArrayList<Row> getRows(){
        return rows;
    }

    public ArrayList<Column> getColumns(){
        return columns;
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

    public boolean setTableName(String newTableName)
            throws EmptyName {
        if(newTableName!=null){
            tableName = newTableName;
            return true;
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

    public void setNumberOfRows(int numberOfRows){
        this.numberOfRows = numberOfRows;
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

    public void setNumberOfColumns(int numberOfColumns){
        this.numberOfColumns = numberOfColumns;
    }
}
