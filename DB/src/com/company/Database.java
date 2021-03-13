package com.company;

import java.util.ArrayList;

public class Database {

    private String databaseName;
    private ArrayList<Table> tables;

    public Database(String databaseName){
        this.databaseName = databaseName;
    }

    public void addTable(Table newTable){
        tables.add(newTable);
    }

    public void printDatabase(){
        for(int i = 0; i < tables.size(); i++){
            tables.get(i).printTable();
        }
    }
}
