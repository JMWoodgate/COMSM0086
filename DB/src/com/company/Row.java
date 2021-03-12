package com.company;
import java.util.ArrayList;
import java.util.Arrays;

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
        if(newElement!=null && columnIndex<numberOfColumns
        && columnIndex>=0){
            elements.set(columnIndex, newElement);
            return true;
        }
        else{
            return false;
        }
    }

    private ArrayList<String> parseString(String rowToParse){
        //parsing into list of words
        String[] listOfWords = rowToParse.split("\t");
        //converting into ArrayList
        ArrayList<String> arrayToList;
        arrayToList = new ArrayList<String>(Arrays.asList(listOfWords));
        return arrayToList;
    }
}
