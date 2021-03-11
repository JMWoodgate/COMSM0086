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

}
