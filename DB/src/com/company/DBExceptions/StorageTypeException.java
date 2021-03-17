package com.company.DBExceptions;

public class StorageTypeException extends CommandException{

    private StorageType type;
    private int index;
    private String expected;

    public StorageTypeException(StorageType type, int index, String expected){
        super();
        this.type = type;
        this. index = index;
        this. expected = expected;
    }

    public String toString(){
        return "Error with storage type. Expected "
                + expected + " but got " + type + " at " + index;
    }
}
