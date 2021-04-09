package com.company;

import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;

import java.util.HashMap;

public class Database {

    private final String databaseName;
    private final HashMap<String, Table> tables;

    public Database(String databaseName){
        this.databaseName = databaseName;
        tables = new HashMap<>();
    }

    public void removeTable(String tableName) throws DBException {
        if(tables.containsKey(tableName)) {
            tables.remove(tableName);
            assert(!tables.containsKey(tableName));
        }else{
            throw new EmptyData("table does not exist in memory");
        }
    }

    public Table getTable(String tableName) throws EmptyData {
        if(tables.containsKey(tableName)) {
            return tables.get(tableName);
        }else{
            throw new EmptyData("table does not exist in memory");
        }
    }

    public HashMap<String, Table> getTables() {
        return tables;
    }

    public void addTable(Table newTable)
        throws EmptyData{
        if(newTable!=null) {
            tables.put(newTable.getTableName(), newTable);
        }
        else{
            throw new EmptyData(databaseName);
        }
    }

    public String getDatabaseName() throws EmptyData {
        if(databaseName!=null){
            return databaseName;
        }
        throw new EmptyData("database name in database object");
    }
}
