package com.company.DBExceptions;

public class IndexOutOfBounds extends DBException {
    StorageType type;
    int index;

    public IndexOutOfBounds(StorageType type, int index){
        this.type = type;
        this.index = index;
    }

    public String toString(){
        return "Index " + index + " of " + type + " out of bounds";
    }
}
