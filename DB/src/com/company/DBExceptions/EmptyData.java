package com.company.DBExceptions;

public class EmptyData extends FileException{
    String fileName;

    public EmptyData(String fileName){
        this.fileName = fileName;
    }

    public String toString(){
        return "Error storing data from: " + fileName;
    }
}
