package com.company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Table {

    private String tableName;
    private int numberOfRows;
    private int numberOfColumns;
    private ArrayList<Row> rows;
    private ArrayList<Column> columns;

    public Table(String tableName, ArrayList<String> dataFromFile){
        this.tableName = tableName;
        numberOfRows = initNumberOfRows(dataFromFile);
        numberOfColumns = initNumberOfColumns(dataFromFile.get(0));
        fillTable(dataFromFile);
    }

    private void fillTable(ArrayList<String> dataFromFile){
        //getting column names, then storing in an array of columns
        ArrayList<String> columnNames = parseString(dataFromFile.get(0));
        columns = new ArrayList<>();
        for(int i = 0; i < numberOfColumns; i++) {
            columns.add(new Column(columnNames.get(i), i));
        }

        //storing data in array of rows
        rows = new ArrayList<>();
        for(int i = 1; i <= numberOfRows; i++){
            ArrayList<String> currentRow = parseString(dataFromFile.get(i));
            rows.add(new Row(currentRow, numberOfColumns));
        }
    }

    public String getElement(int rowNumber, int columnNumber){
        assert(rowNumber >= 0 && rowNumber <= numberOfRows);
        assert(columnNumber >= 0 && columnNumber <= numberOfColumns);
        return rows.get(rowNumber).getElement(columnNumber);
    }

    public String getSpecificColumn(int index){
        assert(index <= numberOfColumns && index >= 0);
        return columns.get(index).getColumnName();
    }

    public ArrayList<String> getSpecificRow(int index){
        assert(index <= numberOfRows && index >= 0);
        return rows.get(index).getElements();
    }

    public ArrayList<Row> getRows(){
        return rows;
    }

    public ArrayList<Column> getColumns(){
        return columns;
    }

    private ArrayList<String> parseString(String rowToParse){
        //parsing into list of words
        String[] listOfWords = rowToParse.split("\t");
        //converting into ArrayList
        ArrayList<String> arrayToList;
        arrayToList = new ArrayList<String>(Arrays.asList(listOfWords));
        return arrayToList;
    }

    public String getTableName(){
        return tableName;
    }

    public boolean setTableName(String newTableName){
        if(newTableName!=null){
            tableName = newTableName;
            return true;
        }
        else{
            return false;
        }

    }

    private int initNumberOfRows(ArrayList<String> dataFromFile) {
        //minus two because the top line is column headers
        numberOfRows = dataFromFile.size() - 2;
        return numberOfRows;
    }

    public int getNumberOfRows(){
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows){
        this.numberOfRows = numberOfRows;
    }

    private int initNumberOfColumns(String firstLine) {
        assert(firstLine!=null);
        ArrayList<String> parsedString = parseString(firstLine);
        numberOfColumns = parsedString.size();
        return numberOfColumns;
    }

    public int getNumberOfColumns(){
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns){
        this.numberOfColumns = numberOfColumns;
    }
}
