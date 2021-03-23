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

    public Table(String databaseName, String tableName) throws DBException {
        this.databaseName = databaseName;
        this.tableName = tableName;
    }

    public void fillTableFromMemory(ArrayList<String> columnNames, ArrayList<String> rowData){
        if(columnNames!=null){
            //add one for id
            numberOfColumns = columnNames.size()+1;
            columns = new ArrayList<>();
            //have to add id column
            columns.add(new Column(tableName, "id", 0));
            //then add the rest of the columns
            for (int i = 0; i < columnNames.size(); i++) {
                columns.add(new Column(tableName, columnNames.get(i), i+1));
            }
        }
        if(rowData!=null){
            numberOfRows = rowData.size();
            rows = new ArrayList<>();
            for (int i = 1; i <= numberOfRows; i++) {
                ArrayList<String> currentRow = parseString(rowData.get(i));
                rows.add(new Row(tableName, currentRow, numberOfColumns));
            }
        }
    }
    //can we replace the filling section with fillTableFromMemory? as they are the same
    //just need to separate rows from column names
    public void fillTableFromFile(ArrayList<String> dataFromFile)
            throws DBException {
        numberOfRows = initNumberOfRows(dataFromFile);
        numberOfColumns = initNumberOfColumns(dataFromFile.get(0));
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

    public String getTable(){
        String table = null;
        StringBuilder stringBuilder = new StringBuilder();
        FileIO fileIO = new FileIO(null);
        stringBuilder.append("Table name: " + tableName + "\n");
        stringBuilder.append("From database: " + databaseName + "\n");

        for(int i = 0; i < numberOfColumns; i++){
            stringBuilder.append(columns.get(i).getColumnName() + "\t");
        }
        stringBuilder.append("\n");
        for(int j = 0; j < numberOfRows; j++){
            ArrayList<String> currentRow = rows.get(j).getElements();
            String formattedRow = fileIO.formatString(currentRow);
            stringBuilder.append(formattedRow);
            stringBuilder.append("\n");
        }
        stringBuilder.append("\n");
        table = stringBuilder.toString();
        return table;
    }

    public void printTable(){
        System.out.println("Table name: " + tableName);
        System.out.println("From database: " + databaseName);
        for(int i = 0; i < numberOfColumns; i++){
            System.out.print(columns.get(i).getColumnName() + " ");
        }
        System.out.println();
        for(int i = 0; i < numberOfRows; i++){
            System.out.println(rows.get(i).getElements());
        }
        System.out.println();
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
