package com.company.DBCommand;

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
        if(command!=null && command.length()>0) {
            try {
                checkQuotes(command);
                checkBrackets(command);
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
                    parseSelect();
                    break;
                case "update":
                    parseUpdate();
                    break;
                case "delete":
                    parseDelete();
                    break;
                case "join":
                    parseJoin();
                    break;
                default:
            }
        } catch(DBException e){
            throw new CommandException(tokenizer.nextToken(index), index, "command", e);
        }
    }

    private void parseJoin() throws DBException{
        try{
            JoinCMD join = new JoinCMD(tokenizedCommand, index);
            index = join.getIndex();
            //do we need to store the table names? (as there are two tables here)
        } catch(DBException e){
            throw new CommandException(
                    tokenizer.nextToken(index), index, "join", e);
        }
    }

    private void parseDelete() throws DBException{
        try{
            DeleteCMD delete = new DeleteCMD(tokenizedCommand, index);
            index = delete.getIndex();
            tableName = delete.getTableName();
        } catch(DBException e){
            throw new CommandException(
                    tokenizer.nextToken(index), index, "delete", e);
        }
    }

    private void parseUpdate() throws DBException{
        try{
            UpdateCMD update = new UpdateCMD(tokenizedCommand, index);
            index = update.getIndex();
            tableName = update.getTableName();
        } catch(DBException e){
            throw new CommandException(
                    tokenizer.nextToken(index), index, "update", e);
        }
    }

    private void parseSelect() throws DBException{
        try{
            SelectCMD select = new SelectCMD(tokenizedCommand, index);
            index = select.getIndex();
            tableName = select.getTableName();
        } catch(DBException e){
            throw new CommandException(
                    tokenizer.nextToken(index), index, "select", e);
        }
    }

    private void parseInsert() throws DBException{
        try{
            InsertCMD insert = new InsertCMD(tokenizedCommand, index);
            index = insert.getIndex();
            tableName = insert.getTableName();
        } catch(DBException e){
            throw new CommandException(
                    tokenizer.nextToken(index), index, "insert", e);
        }
    }

    private void parseAlter() throws DBException{
        try{
            AlterCMD alter = new AlterCMD(tokenizedCommand, index);
            index = alter.getIndex();
            tableName = alter.getTableName();
        } catch(DBException e){
            throw new CommandException(
                    tokenizer.nextToken(index), index, "alter", e);
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
                    tokenizer.nextToken(index), index, "drop", e);
        }
    }

    private void parseCreate() throws DBException{
        try{
            CreateCMD create = new CreateCMD(tokenizedCommand, index);
            index = create.getIndex();
            if(create.getType()==StorageType.DATABASE){
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
                    tokenizer.nextToken(index), index, "create", e);
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
                    tokenizer.nextToken(index), index, "use", e);
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
        throw new CommandException(nextToken, index, "attribute name");
    }

    protected String parseDatabaseName(ArrayList<String> command, int index) throws CommandException {
        index++;
        String nextToken = command.get(index);
        if(isAlphaNumerical(nextToken)) {
            //getting the database name
            databaseName = nextToken;
            return databaseName;
        }
        throw new CommandException(nextToken, index, "database name");
    }

    protected String parseTableName(ArrayList<String> command, int index)
            throws CommandException {
        index++;
        String nextToken = command.get(index);
        if(isAlphaNumerical(nextToken)) {
            //getting the table name
            tableName = nextToken;
            return tableName;
        }
        else{
            throw new CommandException(nextToken, index, "table name");
        }
    }

    protected void checkNextToken(String nextToken, String expectedToken, int index) throws CommandException{
        if (!nextToken.equals(expectedToken)) {
            expectedToken = expectedToken.toUpperCase();
            throw new CommandException(nextToken, index, expectedToken);
        }
    }

    public void checkBrackets(String command) throws DBException {
        int open = 0;
        int close = 0;
        if(command!=null) {
            for (int i = 0; i < command.length(); i++) {
                if (command.charAt(i) == '(') {
                    open++;
                }
                else if (command.charAt(i) == ')'){
                    close++;
                }
            }
            if (open==close) {
                return;
            }
            throw new CommandException(command, index, "missing bracket");
        } throw new EmptyData("command");
    }

    public void checkQuotes(String command) throws DBException {
        int count = 0;
        if(command!=null) {
            for (int i = 0; i < command.length(); i++) {
                if (command.charAt(i) == '\'') {
                    count++;
                }
            }
            if (count % 2 == 0) {
                return;
            }
            throw new CommandException(command, index, "missing quotation mark");
        } throw new EmptyData("command");
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