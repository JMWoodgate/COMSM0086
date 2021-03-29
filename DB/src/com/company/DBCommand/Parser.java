package com.company.DBCommand;

import com.company.DBExceptions.*;
import com.company.Tokenizer;

import java.io.File;
import java.util.ArrayList;

public class Parser {

    protected int index;
    private Tokenizer tokenizer;
    private ArrayList<String> tokenizedCommand;
    private int commandSize;
    private String tableName;
    private String secondTableName;
    private String databaseName;
    private String attributeName;
    private ArrayList<Condition> conditionListArray;
    private ConditionList conditionListObject;
    private boolean parsedOK;
    private String exception;
    private String currentFolder;
    private String homeDirectory;
    private StorageType storageType;
    private AlterationType alterationType;
    private ArrayList<String> attributeList;
    private ArrayList<String> valueListString;
    private ArrayList<Value> valueListObject;
    private boolean hasCondition;

    public Parser(String command, String currentFolder) {
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
                conditionListArray = new ArrayList<>();
                index = 0;
                this.currentFolder = currentFolder;
                homeDirectory = "."+File.separator+"databases";
                        parseCommand();
                //if returned without error, parsed ok
                parsedOK = true;
            } catch (DBException e) {
                parsedOK = false;
                exception = setException(e);
            }
        }else{
            System.out.println("Empty parser command");
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
                    throw new CommandException(tokenizer.nextToken(index), index, "command");
            }
        } catch(DBException e){
            throw new CommandException(tokenizer.nextToken(index), index, "command", e);
        }
    }

    private void parseJoin() throws DBException{
        try{
            JoinCMD join = new JoinCMD(tokenizedCommand, index);
            index = join.getIndex();
            tableName = join.getTableNames().get(0);
            secondTableName = join.getTableNames().get(1);
            attributeList = join.getAttributeNames();
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
            conditionListArray = delete.getConditionListArray();
            conditionListObject = delete.getConditionListObject();
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
            conditionListArray = update.getConditionListArray();
            conditionListObject = update.getConditionListObject();
            attributeList = update.getAttributeList();
            valueListString = update.getValueListString();
            valueListObject = update.getValueListObject();
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
            attributeList = select.getAttributeList();
            if(select.getHasCondition()) {
                hasCondition = true;
                conditionListArray = select.getConditionListArray();
                conditionListObject = select.getConditionListObject();
            }
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
            valueListString = insert.getValueListString();
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
            attributeName = alter.getAttributeName();
            alterationType = alter.getAlterationType();
        } catch(DBException e){
            throw new CommandException(
                    tokenizer.nextToken(index), index, "alter", e);
        }
    }

    private void parseDrop() throws DBException{
        try{
            DropCMD drop = new DropCMD(tokenizedCommand, index);
            index = drop.getIndex();
            storageType = drop.getType();
            if(storageType== StorageType.DATABASE){
                databaseName = drop.getDatabaseName();
                currentFolder = homeDirectory+File.separator+databaseName;
            }else if(storageType==StorageType.TABLE){
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
            CreateCMD create = new CreateCMD(tokenizedCommand, index, currentFolder);
            index = create.getIndex();
            storageType = create.getType();
            if(storageType==StorageType.DATABASE){
                databaseName = create.getDatabaseName();
            }else if(create.getType()==StorageType.TABLE){
                tableName = create.getTableName();
                attributeList = create.getAttributeList();
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
            //don't need to call execute, just need to update the folder name
            currentFolder = homeDirectory+File.separator+databaseName;
        }catch(DBException e){
            throw new CommandException(
                    tokenizer.nextToken(index), index, "use", e);
        }
    }

    protected String parseAttributeName(ArrayList<String> command, int index) throws CommandException {
        String nextToken = command.get(index);
        if(isAlphaNumerical(nextToken)||nextToken.equals("*")) {
            //getting the attribute name
            return nextToken;
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

    //what if there is a bracket inside a string literal?
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

    private void checkQuotes(String command) throws DBException {
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

    public ArrayList<Value> getValueListObject() throws EmptyData {
        if(valueListObject!=null){
            return valueListObject;
        }
        throw new EmptyData("value list");
    }

    public ArrayList<String> getValueListString() throws DBException{
        if(valueListString!=null){
            return valueListString;
        }
        throw new EmptyData("get value list string");
    }

    public StorageType getStorageType(){
        return storageType;
    }

    public AlterationType getAlterationType()throws EmptyData{
        if(alterationType!=null){
            return alterationType;
        }
        throw new EmptyData("alteration type");
    }

    public String getException() {
        return exception;
    }

    public String setException(Exception e) {
        exception = e.toString();
        return exception;
    }

    public boolean getHasCondition(){
        return hasCondition;
    }

    public boolean getParsedOK(){
        return parsedOK;
    }

    public ArrayList<String> getAttributeList() throws EmptyData {
        if(attributeList!=null){
            return attributeList;
        }
        throw new EmptyData("wild attribute list");
    }

    public ConditionList getConditionListObject() throws EmptyData {
        if(conditionListObject!=null){
            return conditionListObject;
        }
        else{
            throw new EmptyData("condition list string");
        }
    }

    public ArrayList<Condition> getConditionListArray() throws EmptyData {
        if(conditionListArray!=null){
            return conditionListArray;
        }
        else{
            throw new EmptyData("condition list string");
        }
    }

    public ArrayList<String> getTokenizedCommand() throws EmptyData {
        if(tokenizedCommand!=null){
            return tokenizedCommand;
        }
        else{
            throw new EmptyData("tokenized command");
        }
    }

    public String getCurrentFolder() throws EmptyData {
        if(currentFolder!=null) {
            return currentFolder;
        }
        throw new EmptyData("current folder name");
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

    public String getSecondTableName() throws EmptyData {
        if(secondTableName!=null) {
            return secondTableName;
        }
        throw new EmptyData("table name");
    }

    public String getTableName() throws EmptyData {
        if(tableName!=null) {
            return tableName;
        }
        throw new EmptyData("table name");
    }

    public String getAttributeName() throws EmptyData{
        if(attributeName!=null){
            return attributeName;
        }
        throw new EmptyData("attribute name");
    }

    public void setIndex(int newIndex){
        index = newIndex;
    }
}
