package com.company.StagExceptions;

public class InvalidCommand extends StagException{

    public String command;

    public InvalidCommand(String command){
        this.command = command;
    }

    public String toString(){
        return (command+" is not a valid command");
    }
}
