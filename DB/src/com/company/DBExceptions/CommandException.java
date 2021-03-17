package com.company.DBExceptions;

public class CommandException extends DBException{
    private String command;
    private int index;
    private String expected;

    public CommandException(){}

    public CommandException(String command, int index, String expected){
        this.command = command;
        this.index = index;
        this.expected = expected;
    }

    public String toString(){
        return command + " at index " + index + " is not a valid command. Expected " + expected;
    }
}
