package com.company.Parsing;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;
import com.company.Tokenizer;

import java.util.ArrayList;

public class Parser {

    protected int index;
    private Tokenizer tokenizer;
    private ArrayList<String> tokenizedCommand;
    private int commandSize;
    private String tableName;
    private String databaseName;

    public Parser(String command) throws DBException {
        try {
            tokenizer = new Tokenizer(command);
            tokenizedCommand = tokenizer.getTokenizedCommand();
            commandSize = tokenizedCommand.size();
            checkEndOfStatement();
            index = 0;
            parseCommand();
        } catch(DBException e){
            e.printStackTrace();
        }
    }

    public Parser(){}

    private boolean parseCommand() throws CommandException{
        try {
            String nextCommand = tokenizer.nextToken(index);
            switch (nextCommand) {
                case "use":
                    //creates a new instance of use and parses it
                    UseCMD use = new UseCMD(tokenizedCommand, index);
                    index = use.getIndex();
                    break;
                case "create":

                case "drop":
                case "alter":
                case "insert":
                case "select":
                case "update":
                case "delete":
                case "join":
                    break;
                default:
                    return false;
            }
        } catch(DBException e){
            e.printStackTrace();
        }
        return true;
    }

    private boolean checkEndOfStatement() throws CommandException {
        String lastToken = tokenizedCommand.get(commandSize-1);
        if(lastToken.equals(";")){
            return true;
        }
        else{
            throw new CommandException(lastToken, commandSize-1, ";");
        }
    }

    protected boolean isAlphaNumerical(String token){
        for(int i = 0; i < token.length(); i++){
            if(!Character.isLetterOrDigit(token.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public ArrayList<String> getTokenizedCommand() throws EmptyData {
        if(tokenizedCommand!=null){
            return tokenizedCommand;
        }
        else{
            throw new EmptyData("tokenized command");
        }
    }

    public int getIndex(){
        return index;
    }

    public String getDatabaseName() throws EmptyData {
        if(databaseName!=null) {
            return databaseName;
        }
        throw new EmptyData("database name");
    }

    public String getTableName() throws EmptyData {
        if(tableName!=null) {
            return tableName;
        }
        throw new EmptyData("table name");
    }

    public void setIndex(int newIndex){
        index = newIndex;
    }

    private boolean isOp() throws DBException {
        if(tokenizedCommand!=null) {
            switch (tokenizedCommand.get(index)) {
                case ("=="):
                case (">"):
                case ("<"):
                case (">="):
                case ("<="):
                case ("!="):
                case ("LIKE"):
                    break;
                default:
                    throw new CommandException(tokenizedCommand.get(index), index, "operator");
            }
            return true;
        }
        throw new EmptyData("Command in isOp");
    }
}
