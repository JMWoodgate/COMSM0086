package com.company.DBExceptions;

public class EmptyData extends FileException{
    String fileName;

    public EmptyData(String fileName){
        this.fileName = fileName;
    }

    public String toString(){
        return "No data stored from " + fileName;
    }
}
