package com.company.DBExceptions;

public class EmptyName extends DBException{
    StorageType type;
    String parentName;

    public EmptyName(StorageType type, String parentName){
        this.type = type;
        this.parentName = parentName;
    }

    public String toString() {
        return type + " name from " + parentName + " is empty";
    }
}
