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

    public Row(String tableName, ArrayList<String> elements, int numberOfColumns){
        this.tableName = tableName;
        this.elements = elements;
        this.numberOfColumns = numberOfColumns;
    }

    public ArrayList<String> getElements(){
        return elements;
    }

    public String getPrimaryKey(){

        return elements.get(0);
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
