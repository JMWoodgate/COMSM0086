package com.company;
import com.company.DBExceptions.*;

import java.util.ArrayList;

public class Row {

    private final ArrayList<String> elements;
    private int numberOfColumns;
    private int id;

    //adding an empty row with a specified id
    public Row(int numberOfColumns, int id){
        elements = new ArrayList<>();
        for(int i = 0; i<numberOfColumns-1; i++){
            elements.add(null);
        }
        this.numberOfColumns = numberOfColumns;
        this.id = id;
        addID();
    }

    //adding a row with the id already in the first index of elements
    public Row(ArrayList<String> elements,
               int numberOfColumns){
        if(elements==null) {
            this.elements = new ArrayList<>();
            for(int i = 0; i<numberOfColumns; i++){
                this.elements.add(null);
            }
            id = 0;
        }else{
            this.elements = elements;
            id = Integer.parseInt(elements.get(0));
        }
        this.numberOfColumns = numberOfColumns;
    }

    //adding a row with the id of the previous row+1
    public Row(ArrayList<String> elements,
               int numberOfColumns, int lastID){
        this.elements = elements;
        id = lastID+1;
        addID();
        this.numberOfColumns = numberOfColumns;
    }

    public void addElement(){
        elements.add(null);
        numberOfColumns++;
    }

    //inserts id into first index of elements
    private void addID(){
        String newID = Integer.toString(id);
        elements.add(0, newID);
    }

    public void setID(int id){
        this.id = id;
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

    public void deleteElement(int columnIndex) throws EmptyData {
        if(columnIndex>numberOfColumns){
            throw new EmptyData(
                    "couldn't delete element from column "
                            +columnIndex+" at row id "+id);
        }
        elements.remove(columnIndex);
        numberOfColumns--;
    }

    public void setElement(String newElement, int columnIndex)
            throws DBException {
        if(columnIndex<numberOfColumns && columnIndex>=0){
            elements.set(columnIndex, newElement);
        }
        else{
            throw new IndexOutOfBounds(StorageType.COLUMN, columnIndex);
        }
    }
}
