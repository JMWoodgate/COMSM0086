package com.company.DBExceptions;

public class FileException extends DBException{
    Throwable error;

    public FileException(){
        super();
    }

    public FileException(Throwable error){
        this.error = error;
    }

    public String toString(){
        return "Error with file: "+"\n"+error;
    }
}
