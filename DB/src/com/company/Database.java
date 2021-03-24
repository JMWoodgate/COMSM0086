package com.company;

import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;

import java.util.ArrayList;
import java.util.HashMap;

public class Database {

    private final String databaseName;
    private HashMap<String, Table> tables;
    private int numberOfTables = 0;

    public Database(String databaseName){
        this.databaseName = databaseName;
        tables = new HashMap<>();
    }

    public void removeTable(String tableName) throws DBException {
        System.out.println("entered removeTable");
        if(tables!=null) {
            if(tables.containsKey(tableName)) {
                tables.remove(tableName);
            }else{
                throw new EmptyData("table does not exist in memory");
            }
        }throw new EmptyData("hashmap of tables in database");
    }

    public Table getTable(String tableName) throws EmptyData {
        if(tables!=null) {
            if(tables.containsKey(tableName)) {
                return tables.get(tableName);
            }else{
                throw new EmptyData("table does not exist in memory");
            }
        }throw new EmptyData("hashmap of tables in database");
    }

    public HashMap<String, Table> getTables() throws EmptyData {
        if(tables!=null) {
            return tables;
        }throw new EmptyData("hashmap of databases");
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
            System.out.println("adding table "+newTable.getTableName());
            tables.put(newTable.getTableName(), newTable);
            System.out.println("added table "+tables.get(newTable.getTableName()).getTableName());
            numberOfTables++;
        }
        else{
            throw new EmptyData(databaseName);
        }
    }

    public int getNumberOfTables(){
        return numberOfTables;
    }

    public String getDatabaseString() {
        String database = null;
        StringBuilder stringBuilder = new StringBuilder();
        for (Table table : tables.values()) {
            stringBuilder.append(table.getTable());
        }
        database = stringBuilder.toString();
        return database;
    }

    public void printDatabase(){
        for(Table table : tables.values()){
            table.printTable();
        }
    }

    public String getDatabaseName() throws EmptyData {
        if(databaseName!=null){
            return databaseName;
        }
        throw new EmptyData("database name in database object");
    }
}
