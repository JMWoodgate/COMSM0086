package com.company;

public class Row {

    private final int primaryKey;
    private final int numberOfRows;

    public Row(int primaryKey, int numberOfRows){
        this.primaryKey = primaryKey;
        this.numberOfRows = numberOfRows;
    }

    public int getPrimaryKey(){
        return primaryKey;
    }

    public int getNumberOfRows(){
        return numberOfRows;
    }

}
