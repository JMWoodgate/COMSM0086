package com.company.DBExceptions;

public class StorageTypeException extends CommandException{

    private final StorageType type;
    private final int index;
    private final String expected;

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
