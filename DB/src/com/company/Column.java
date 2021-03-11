package com.company;

public class Column {

    private final String columnName;
    private final int numberOfColumns;

    public Column(String columnName, int numberOfColumns){
        this.columnName = columnName;
        this.numberOfColumns = numberOfColumns;
    }

    public String getColumnName(){
        return columnName;
    }

    public int getNumberOfColumns(){
        return numberOfColumns;
    }
}
