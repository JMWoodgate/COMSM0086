package com.company.StagExceptions;

public class LocationDoesNotExist extends StagException{

    private final String locationName;

    public LocationDoesNotExist(String locationName){
        this.locationName = locationName;
    }

    public String toString(){ return ("Location does not exist "+locationName); }
}
