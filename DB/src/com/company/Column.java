package com.company;

import com.company.DBExceptions.EmptyName;
import com.company.DBExceptions.StorageType;

import java.util.ArrayList;

public class Column {

    private String tableName;
    private String columnName;
    private int columnIndex;

    public Column(String tableName, String columnName, int columnIndex){
        this.tableName = tableName;
        this.columnName = columnName;
        this.columnIndex = columnIndex;
    }

    public void setColumnIndex(int index){
        columnIndex = index;
    }

    public int getColumnIndex(){
        return columnIndex;
    }

    public boolean setColumnName(String newName) throws EmptyName {
        if(newName!=null){
            columnName = newName;
            return true;
        }
        else{
            throw new EmptyName(StorageType.COLUMN, tableName);
        }
    }

    public String getColumnName(){
        return columnName;
    }

}
