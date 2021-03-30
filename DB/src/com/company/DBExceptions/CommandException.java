package com.company.DBExceptions;

public class CommandException extends DBException{
    private String command;
    private int index;
    private String expected;
    private Throwable error;

    public CommandException(){}

    public CommandException(String command, int index, String expected){
        this.command = command;
        this.index = index;
        this.expected = expected;
    }

    public CommandException(
            String command, int index, String expected, Throwable error){
        this.command = command;
        this.index = index;
        this.expected = expected;
        this.error = error;
    }

    public String toString(){
        String errorString = "'"+command + "' at index " +
                index + " is not a valid command. Expected "
                + "'"+expected+"'";
        if(error!=null){
            errorString += "\n" + error;
        }
        return errorString;
    }
}
