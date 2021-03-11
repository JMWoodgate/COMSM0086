package com.company;

import java.util.ArrayList;

public class Column {

    private String columnNames;
    private final int numberOfColumns;
    private int columnIndex;

    public Column(String columnNames,
                  int numberOfColumns, int columnIndex){
        this.columnNames = columnNames;
        this.numberOfColumns = numberOfColumns;
        this.columnIndex = columnIndex;
    }

    public void setColumnIndex(int index){
        columnIndex = index;
    }

    public int getColumnIndex(){
        return columnIndex;
    }

    public String getColumnNames(){
        return columnNames;
    }

    public int getNumberOfColumns(){
        return numberOfColumns;
    }
}
