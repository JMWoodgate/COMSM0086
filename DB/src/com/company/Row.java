package com.company;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyName;
import com.company.DBExceptions.IndexOutOfBounds;
import com.company.DBExceptions.StorageType;

import java.util.ArrayList;

public class Row {

    private final String tableName;
    private final ArrayList<String> elements;
    private final int numberOfColumns;
    private final int id;

    public Row(String tableName, int numberOfColumns, int id){
        this.tableName = tableName;
        elements = new ArrayList<>();
        for(int i = 0; i<numberOfColumns-1; i++){
            elements.add(null);
        }
        this.numberOfColumns = numberOfColumns;
        this.id = id;
        addID();
    }

    public Row(String tableName, ArrayList<String> elements,
               int numberOfColumns){
        this.tableName = tableName;
        this.elements = elements;
        this.numberOfColumns = numberOfColumns;
        id = Integer.parseInt(elements.get(0));
    }

    public Row(String tableName, ArrayList<String> elements,
               int numberOfColumns, int lastID){
        this.tableName = tableName;
        this.elements = elements;
        id = lastID+1;
        addID();
        this.numberOfColumns = numberOfColumns;
    }

    private void addID(){
        String newID = Integer.toString(id);
        elements.add(0, newID);
    }

    public int getID(){
        return id;
    }

    public ArrayList<String> getElements(){
        return elements;
    }

    public String getElement(int columnIndex)
            throws IndexOutOfBounds {
        if(columnIndex <= numberOfColumns && columnIndex >= 0) {
            return elements.get(columnIndex);
        }
        else{
            throw new IndexOutOfBounds(StorageType.COLUMN, columnIndex);
        }
    }

    public boolean setElement(String newElement, int columnIndex)
            throws DBException {
        if(columnIndex<numberOfColumns
        && columnIndex>=0){
            if(newElement!=null) {
                elements.set(columnIndex, newElement);
                return true;
            }
            else{
                throw new EmptyName(StorageType.COLUMN, tableName);
            }
        }
        else{
            throw new IndexOutOfBounds(StorageType.COLUMN, columnIndex);
        }
    }
}
