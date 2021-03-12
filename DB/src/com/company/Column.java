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

    public boolean setColumnName(String newName){
        if(newName!=null){
            columnName = newName;
            return true;
        }
        else{
            return false;
        }
    }

    public String getColumnName(){
        return columnName;
    }

}
