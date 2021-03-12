package com.company;

import java.util.ArrayList;

public class Database {

    private String databaseName;
    private ArrayList<Table> tables;

    public Database(String databaseName){
        this.databaseName = databaseName;
    }
}
