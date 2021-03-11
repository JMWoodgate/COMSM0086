package com.company;

import java.util.ArrayList;

public class Column {

    private String columnName;
    private int columnIndex;

    public Column(String columnName, int columnIndex){
        this.columnName = columnName;
        this.columnIndex = columnIndex;
    }

    public void setColumnIndex(int index){
        columnIndex = index;
    }

    public int getColumnIndex(){
        return columnIndex;
    }

    public String getColumnName(){
        return columnName;
    }

}
