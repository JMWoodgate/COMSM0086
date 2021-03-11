package com.company;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Table {

    private String tableName;
    private int numberOfRows;
    private int numberOfColumns;
    private ArrayList<Row> rows;
    private ArrayList<Column> columns;
    private ArrayList<String> fileStorage;

    public Table(String tableName, ArrayList<String> fileStorage){
        this.tableName = tableName;
        this.fileStorage = fileStorage;

        numberOfRows = getNumberOfRows(fileStorage);
        numberOfColumns = getNumberOfColumns(fileStorage.get(0));

        ArrayList<Row> rows = new ArrayList<Row>();
        ArrayList<Column> columns = new ArrayList<Column>();
        for(int i = 0; i < numberOfRows; i++){
            rows.add(null);
        }

        for(int i = 0; i < numberOfColumns; i++) {
            columns.add(null);
        }
    }

    public void fillTable(File fileToOpen){
        ArrayList<String> fileStorage = null;
        FileIO fileIO = new FileIO();
        try{
            fileStorage = fileIO.readFile(fileToOpen);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] parseString(String rowToParse){
        String[] listOfWords = rowToParse.split("\t");
        return listOfWords;
    }

    public String getTableName(){
        return tableName;
    }

    public int getNumberOfRows(ArrayList<String> fileStorage) {
        numberOfRows = fileStorage.size();
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows){
        this.numberOfRows = numberOfRows;
    }

    public int getNumberOfColumns(String firstLine) {
        String[] parsedString = parseString(firstLine);
        numberOfColumns = parsedString.length;
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns){
        this.numberOfColumns = numberOfColumns;
    }
}
