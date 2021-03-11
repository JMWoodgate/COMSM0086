package com.company;
import java.util.ArrayList;

public class Row {

    private ArrayList<String> elements;
    private int numberOfRows;

    public Row(ArrayList<String> elements, int numberOfRows){
        this.elements = elements;
        this.numberOfRows = numberOfRows;
    }

    public ArrayList<String> getElements(){
        return elements;
    }

    public int getNumberOfRows(){
        return numberOfRows;
    }

}
