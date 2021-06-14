package com.company.StagExceptions;

public class UnknownDataType extends StagException{

    private final String dataType;

    public UnknownDataType(String dataType){
        this.dataType = dataType;
    }
    public String toString(){
        return ("Unknown data type "+dataType);
    }
}
