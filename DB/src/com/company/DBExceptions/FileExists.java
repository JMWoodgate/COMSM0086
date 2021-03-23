package com.company.DBExceptions;

public class FileExists extends FileException{
    String fileName;

    public FileExists(String fileName){
        this.fileName = fileName;
    }

    public String toString(){
        return "File " + fileName + " already exists";
    }
}

