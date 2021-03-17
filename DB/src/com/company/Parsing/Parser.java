package com.company.Parsing;

import com.company.DBExceptions.*;
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
        if(command != null) {
            try {
                //tokenizing the command
                tokenizer = new Tokenizer(command);
                tokenizedCommand = tokenizer.getTokenizedCommand();
                commandSize = tokenizedCommand.size();
                //checking that the statement ends with a ;
                assert(checkEndOfStatement());
                index = 0;
                parseCommand();
            } catch (DBException e) {
                e.printStackTrace();
            }
        }else{
            throw new EmptyData("Parser command");
        }
    }

    public Parser(){}

    private boolean parseCommand() throws CommandException{
        try {
            String nextCommand = tokenizer.nextToken(index);
            switch (nextCommand) {
                case "use":
                    parseUse();
                    break;
                case "create":
                    parseCreate();
                    break;
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

    private boolean parseCreate() throws DBException{
        try{
            CreateCMD create = new CreateCMD(tokenizedCommand, index);
            index = create.getIndex();
            if(create.getType()== StorageType.DATABASE){
                databaseName = create.getDatabaseName();
            }else if(create.getType()==StorageType.TABLE){
                tableName = create.getTableName();
            }
            else{
                throw new StorageTypeException(
                        create.getType(), index, "table or database");
            }
        } catch(DBException e){
            throw new CommandException(
                    tokenizer.nextToken(index), index, "create");
        }
        return true;
    }

    private boolean parseUse() throws DBException {
        try {
            //creates a new instance of useCMD and parses it
            UseCMD use = new UseCMD(tokenizedCommand, index);
            //updating the current index
            index = use.getIndex();
            databaseName = use.getDatabaseName();
            return true;
        }catch(DBException e){
            throw new CommandException(
                    tokenizer.nextToken(index), index, "use");
        }
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
