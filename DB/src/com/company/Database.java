package com.company;

import com.company.DBExceptions.EmptyData;

import java.util.ArrayList;

public class Database {

    private final String databaseName;
    private ArrayList<Table> tables;
    private int numberOfTables = 0;

    public Database(String databaseName){
        this.databaseName = databaseName;
        tables = new ArrayList<Table>();
    }

    public ArrayList<Table> getTables(){
        return tables;
    }

    public ArrayList<String> getTableNames(){
        ArrayList<String> listOfTableNames = new ArrayList<>();
        for(int i = 0; i < numberOfTables; i++){
            listOfTableNames.add(tables.get(i).getTableName());
        }
        return listOfTableNames;
    }

    public void addTable(Table newTable)
        throws EmptyData{
        if(newTable!=null) {
            tables.add(newTable);
            numberOfTables++;
        }
        else{
            throw new EmptyData(databaseName);
        }
    }

    public int getNumberOfTables(){
        return numberOfTables;
    }

    public String getDatabase() {
        String database = null;
        StringBuilder stringBuilder = new StringBuilder();
        for (Table table : tables) {
            stringBuilder.append(table.getTable());
        }
        database = stringBuilder.toString();
        return database;
    }

    public void printDatabase(){
        for(int i = 0; i < tables.size(); i++){
            tables.get(i).printTable();
        }
    }
}
