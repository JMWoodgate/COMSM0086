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
    private String attributeName;

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

    private void parseCommand() throws CommandException{
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
                    parseDrop();
                    break;
                case "alter":
                    parseAlter();
                    break;
                case "insert":
                    parseInsert();
                    break;
                case "select":
                case "update":
                case "delete":
                case "join":
                    break;
                default:
            }
        } catch(DBException e){
            throw new CommandException(tokenizer.nextToken(index), index, "command");
        }
    }

    private void parseInsert() throws DBException{
        try{
            InsertCMD insert = new InsertCMD(tokenizedCommand, index);
            index = insert.getIndex();
        } catch(DBException e){
            throw new CommandException(
                    tokenizer.nextToken(index), index, "insert");
        }
    }

    private void parseAlter() throws DBException{
        try{
            AlterCMD alter = new AlterCMD(tokenizedCommand, index);
            index = alter.getIndex();
        } catch(DBException e){
            throw new CommandException(
                    tokenizer.nextToken(index), index, "alter");
        }
    }

    private void parseDrop() throws DBException{
        try{
            DropCMD drop = new DropCMD(tokenizedCommand, index);
            index = drop.getIndex();
            if(drop.getType()== StorageType.DATABASE){
                databaseName = drop.getDatabaseName();
            }else if(drop.getType()==StorageType.TABLE){
                tableName = drop.getTableName();
            }
            else{
                throw new StorageTypeException(
                        drop.getType(), index, "table or database");
            }
        } catch(DBException e){
            throw new CommandException(
                    tokenizer.nextToken(index), index, "drop");
        }
    }

    private void parseCreate() throws DBException{
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
    }

    private void parseUse() throws DBException {
        try {
            //creates a new instance of useCMD and parses it
            UseCMD use = new UseCMD(tokenizedCommand, index);
            //updating the current index
            index = use.getIndex();
            databaseName = use.getDatabaseName();
        }catch(DBException e){
            throw new CommandException(
                    tokenizer.nextToken(index), index, "use");
        }
    }

    protected String parseAttributeName(ArrayList<String> command, int index) throws CommandException {
        index++;
        String nextToken = command.get(index);
        if(isAlphaNumerical(nextToken)) {
            //getting the attribute name
            attributeName = nextToken;
            return attributeName;
        }
        throw new CommandException(nextToken, index, "databaseName");
    }

    protected String parseDatabaseName(ArrayList<String> command, int index) throws CommandException {
        index++;
        String nextToken = command.get(index);
        if(isAlphaNumerical(nextToken)) {
            //getting the database name
            databaseName = nextToken;
            return databaseName;
        }
        throw new CommandException(nextToken, index, "databaseName");
    }

    protected String parseTableName(ArrayList<String> command, int index)
            throws CommandException {
        index++;
        String nextToken = command.get(index);
        if(isAlphaNumerical(nextToken)) {
            //getting the table name
            tableName = nextToken;
            index++;
            nextToken = command.get(index);
            switch (nextToken) {
                case ";":
                    //end of statement
                    return tableName;
                case "(":
                    //call attributeList
                    return tableName;
                case "add":
                case "drop":
                    //string being parsed is an Alter command
                    return tableName;
                default:
                    throw new CommandException(nextToken, index, "; ( add or drop");
            }
        }
        else{
            throw new CommandException(nextToken, index, "table name");
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
