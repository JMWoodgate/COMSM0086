package com.company;
import java.util.ArrayList;

public class Row {

    private ArrayList<String> elements;
    private int numberOfColumns;

    public Row(ArrayList<String> elements, int numberOfColumns){
        this.elements = elements;
        this.numberOfColumns = numberOfColumns;
    }

    public ArrayList<String> getElements(){
        return elements;
    }

    public String getPrimaryKey(){
        return elements.get(0);
    }

    public String getElement(int columnIndex){
        return elements.get(columnIndex);
    }

    public boolean setElement(String newElement, int columnIndex){
        elements.set(columnIndex, newElement);
        return true;
    }
}
