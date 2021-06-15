package com.company.StagExceptions;

public class ArtefactDoesNotExist extends StagException{
    String artefact;

    public ArtefactDoesNotExist(String artefact){
        this.artefact = artefact;
    }

    public String toString(){
        return (artefact+" does not exist here.");
    }
}
