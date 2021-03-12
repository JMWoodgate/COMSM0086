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
    private ArrayList<String> dataFromFile;

    public Table(String tableName, File fileToOpen){
        this.tableName = tableName;
        //read file
        FileIO fileIO = new FileIO();
        try{
            dataFromFile = fileIO.readFile(fileToOpen);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get number of rows and columns
        numberOfRows = getNumberOfRows(dataFromFile);
        numberOfColumns = getNumberOfColumns(dataFromFile.get(0));
        //fill table with data read from file
        fillTable(dataFromFile);
    }

    public void fillTable(ArrayList<String> fileStorage){
        //getting column names, then storing in an array of columns
        ArrayList<String> columnNames = parseString(fileStorage.get(0));
        columns = new ArrayList<>();
        for(int i = 0; i < numberOfColumns; i++) {
            columns.add(new Column(columnNames.get(i), i));
        }

        //storing data in array of rows
        rows = new ArrayList<>();
        for(int i = 1; i < numberOfRows; i++){
            ArrayList<String> currentRow = parseString(fileStorage.get(i));
            rows.add(new Row(currentRow, numberOfColumns));
        }
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

    public int getNumberOfRows(ArrayList<String> fileStorage) {
        //minus one because the top line is column headers
        numberOfRows = fileStorage.size() - 1;
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows){
        this.numberOfRows = numberOfRows;
    }

    public int getNumberOfColumns(String firstLine) {
        ArrayList<String> parsedString = parseString(firstLine);
        numberOfColumns = parsedString.size();
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns){
        this.numberOfColumns = numberOfColumns;
    }
}
