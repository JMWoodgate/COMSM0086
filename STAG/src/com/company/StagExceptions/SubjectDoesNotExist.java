package com.company.StagExceptions;

public class SubjectDoesNotExist extends StagException {

    public SubjectDoesNotExist(){

    }

    public String toString(){
        return "A subject is missing";
    }
}
