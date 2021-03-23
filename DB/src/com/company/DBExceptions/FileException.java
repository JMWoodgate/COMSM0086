package com.company.DBExceptions;

public class FileException extends DBException{
    Throwable error;
    String errorString;

    public FileException(){
        super();
    }

    public FileException(Throwable error){
        this.error = error;
    }

    public FileException(String errorString){
        this.errorString = errorString;
    }

    public String toString(){
        String errorMessage;
        if(error!=null) {
            errorMessage = "Error with file: " + "\n" + error;
        }
        else if(errorString!=null){
            errorMessage = "Error with file: " + "\n" + errorString;
        }else{
            errorMessage = "Error with file.";
        }
        return errorMessage;
    }
}
