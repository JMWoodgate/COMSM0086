package com.company.StagExceptions;

public class DataTypeException extends StagException{

    private final String dataType;

    public DataTypeException(String dataType){
        this.dataType = dataType;
    }
    public String toString(){
        return ("Unknown data type "+dataType);
    }
}
