package com.company;

import com.company.DBExceptions.EmptyName;
import com.company.DBExceptions.StorageType;

public class Column {

    private String tableName;
    private String columnName;
    private int columnIndex;

    public Column(String tableName, String columnName, int columnIndex){
        System.out.println("making new column "+columnName+" in "+tableName+" at "+columnIndex);
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

    public void setColumnName(String newName) throws EmptyName {
        if(newName!=null){
            columnName = newName;
        }
        else{
            throw new EmptyName(StorageType.COLUMN, tableName);
        }
    }

    public String getColumnName(){
        return columnName;
    }

}
