package com.company;

import java.util.ArrayList;
import java.util.LinkedList;

public class Table {

    private String tableName;
    private int numberOfRows;
    private int numberOfColumns;
    public ArrayList<Column<Row>> table;

    public Table(String tableName, int numberOfRows, int numberOfColumns){
        this.tableName = tableName;
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;

        ArrayList<Row> rows = new ArrayList<Row>();
        ArrayList<Column> columns = new ArrayList<Column>();
        for(int i = 0; i < numberOfRows; i++){
            rows.add(null);
        }

        for(int i = 0; i < numberOfColumns; i++) {
            columns.add(null);
        }

        table = new ArrayList<Column<Row>>();

        for(int i = 0; i < numberOfColumns; i++){
            table.add(new Column<Row>(null));
            for(int j = 0; j < numberOfRows; j++){
                table.get(i).add(new Row(null));
            }
        }
    }

    public String getTableName(){
        return tableName;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows){
        this.numberOfRows = numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns){
        this.numberOfColumns = numberOfColumns;
    }
}
