package com.company;

import com.company.DBExceptions.EmptyData;

public class Column {

    private final String tableName;
    private final String columnName;
    private final int columnIndex;

    public Column(String tableName, String columnName, int columnIndex){
        this.tableName = tableName;
        this.columnName = columnName;
        this.columnIndex = columnIndex;
    }

    public int getColumnIndex(){
        return columnIndex;
    }

    public String getTableName() throws EmptyData {
        if(tableName!=null){
            return tableName;
        }
        throw new EmptyData("table name in Column object");
    }

    public String getColumnName(){
        return columnName;
    }

}
