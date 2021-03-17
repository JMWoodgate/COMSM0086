package com.company;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBCommand.*;

import java.util.ArrayList;

public class Test {

    public Test() throws DBException{
        try {
            testJoinCMD();
            testDeleteCMD();
            testUpdateCMD();
            testSelectCMD();
            testInsertCMD();
            testAlterCMD();
            testDropCMD();
            testCreateCMD();
            testUseCMD();
            testValue();
            testParser();
            testTokenizer();
            testTable();
            testRow();
            testColumn();
        }
        catch(DBException e){
            System.out.println("DBException " + e);
            e.printStackTrace();
        }
    }

    private void testJoinCMD() throws DBException {
        String command = "JOIN  parties AND ward ON AND;";
        try{
            Parser testParser = new Parser(command);
            ArrayList<String> tokenizedCommand = testParser.getTokenizedCommand();
            JoinCMD testJoin = new JoinCMD(tokenizedCommand, 0);
            assert(testJoin.getIndex()==6);
            assert(testJoin.getFirstTableName().equals("parties"));
            assert(testJoin.getSecondTableName().equals("ward"));
        }
        catch(DBException e){
            throw new CommandException(command, 0, "JOIN", e);
        }
    }

    private void testDeleteCMD() throws DBException {
        String command = "DELETE FROM elections WHERE ;";
        try{
            Parser testParser = new Parser(command);
            ArrayList<String> tokenizedCommand = testParser.getTokenizedCommand();
            DeleteCMD testDelete = new DeleteCMD(tokenizedCommand, 0);
            assert(testDelete.getIndex()==4);
            assert(testDelete.getTableName().equals("elections"));
        }
        catch(DBException e){
            throw new CommandException(command, 0, "DELETE", e);
        }
    }

    private void testUpdateCMD() throws DBException {
        String command = "UPDATE elections SET WHERE ;";
        try{
            Parser testParser = new Parser(command);
            ArrayList<String> tokenizedCommand = testParser.getTokenizedCommand();
            UpdateCMD testUpdate = new UpdateCMD(tokenizedCommand, 0);
            assert(testUpdate.getIndex()==4);
            assert(testUpdate.getTableName().equals("elections"));
        }
        catch(DBException e){
            throw new CommandException(command, 0, "UPDATE", e);
        }
    }

    private void testSelectCMD() throws DBException {
        String command = "SELECT FROM elections WHERE;";
        try{
            Parser testParser = new Parser(command);
            ArrayList<String> tokenizedCommand = testParser.getTokenizedCommand();
            SelectCMD testSelect = new SelectCMD(tokenizedCommand, 0);
            assert(testSelect.getIndex()==4);
            assert(testSelect.getTableName().equals("elections"));
        }
        catch(DBException e){
            throw new CommandException(command, 0, "SELECT", e);
        }
    }

    private void testInsertCMD() throws DBException {
        String command = "INSERT INTO elections VALUES ( ;";
        try{
            Parser testParser = new Parser(command);
            ArrayList<String> tokenizedCommand = testParser.getTokenizedCommand();
            InsertCMD testInsert = new InsertCMD(tokenizedCommand, 0);
            assert(testInsert.getIndex()==5);
            assert(testInsert.getTableName().equals("elections"));
        }
        catch(DBException e){
            throw new CommandException(command, 0, "INSERT", e);
        }
    }

    private void testAlterCMD() throws DBException {
        String command = "ALTER TABLE elections ADD party;";
        try{
            Parser testParser = new Parser(command);
            ArrayList<String> tokenizedCommand = testParser.getTokenizedCommand();
            AlterCMD testAlter = new AlterCMD(tokenizedCommand, 0);
            assert(testAlter.getIndex()==5);
            assert(testAlter.getTableName().equals("elections"));
            assert(testAlter.getAttributeName().equals("party"));
        }
        catch(DBException e){
            throw new CommandException(command, 0, "ALTER", e);
        }
    }

    private void testDropCMD() throws DBException{
        String command1 = "DROP table elections ;";
        try{
            Parser testParser1 = new Parser(command1);
            ArrayList<String> tokenizedCommand1 = testParser1.getTokenizedCommand();
            DropCMD testDrop1 = new DropCMD(tokenizedCommand1, 0);
            assert(testDrop1.getIndex()==3);
            assert(testDrop1.getTableName().equals("elections"));
        }
        catch(DBException e){
            throw new CommandException(command1, 0, "DROP", e);
        }
        String command2 = "DROP database politics;";
        try{
            Parser testParser2 = new Parser(command2);
            ArrayList<String> tokenizedCommand2 = testParser2.getTokenizedCommand();
            DropCMD testDrop2 = new DropCMD(tokenizedCommand2, 0);
            assert(testDrop2.getIndex()==3);
            assert(testDrop2.getDatabaseName().equals("politics"));
        }
        catch(DBException e){
            throw new CommandException(command2, 0, "DROP", e);
        }
    }

    private void testCreateCMD() throws DBException{
        String command1 = "CREATE table elections ;";
        try{
            Parser testParser1 = new Parser(command1);
            ArrayList<String> tokenizedCommand1 = testParser1.getTokenizedCommand();
            CreateCMD testCreate1 = new CreateCMD(tokenizedCommand1, 0);
            assert(testCreate1.getIndex()==3);
            assert(testCreate1.getTableName().equals("elections"));
        }
        catch(DBException e){
            throw new CommandException(command1, 0, "CREATE", e);
        }
        String command2 = "CREATE database politics;";
        try{
            Parser testParser2 = new Parser(command2);
            ArrayList<String> tokenizedCommand2 = testParser2.getTokenizedCommand();
            CreateCMD testCreate2 = new CreateCMD(tokenizedCommand2, 0);
            assert(testCreate2.getIndex()==3);
            assert(testCreate2.getDatabaseName().equals("politics"));
        }
        catch(DBException e){
            throw new CommandException(command2, 0, "CREATE", e);
        }
    }

    private void testUseCMD()throws DBException{
        String command = "USE elections ;";
        try{
            Parser testParser = new Parser(command);
            ArrayList<String> tokenizedCommand = testParser.getTokenizedCommand();
            UseCMD testUse = new UseCMD(tokenizedCommand, 0);
            assert(testUse.getDatabaseName().equals("elections"));
            assert(testUse.getIndex()==2);
        }
        catch(DBException e){
            throw new CommandException(command, 0, "USE", e);
        }
    }

    private void testValue()throws DBException {
        try {
            ArrayList<String> elements = new ArrayList<>();
            elements.add("1");
            elements.add("true");
            elements.add("place");
            elements.add("3.122");
            Value testValue1 = new Value(elements, 0);
            assert (testValue1.getLiteralType() == LiteralType.INTEGER);
            assert (testValue1.getIntLiteral() == 1);
            Value testValue2 = new Value(elements, 1);
            assert (testValue2.getLiteralType() == LiteralType.BOOLEAN);
            assert (testValue2.getBooleanLiteral());
            Value testValue3 = new Value(elements, 2);
            assert (testValue3.getLiteralType() == LiteralType.STRING);
            assert (testValue3.getStringLiteral().equals("place"));
            Value testValue4 = new Value(elements, 3);
            assert (testValue4.getLiteralType() == LiteralType.FLOAT);
            assert (testValue4.getFloatLiteral() <= 3.122 || testValue4.getFloatLiteral() >= 3.122);
        }catch(DBException e){
            System.out.println("DBException " + e);
            e.printStackTrace();
        }
    }

    private void testParser() throws DBException {
        String testCommand = "FROM\tparties SELECT *;\n";
        Parser testParser = new Parser(testCommand);
    }

    private void testTokenizer() throws DBException {
        String testCommand = "FROM parties (SELECT) *;";
        ArrayList<String> tokenizedCommand = new ArrayList<>();
        Tokenizer testTokenizer = new Tokenizer(testCommand);
    }

    private void testTable() throws DBException {
        String dbName = "testDB";
        ArrayList<String> testData = new ArrayList<>();
        testData.add("id\tname\taddress");
        testData.add("1\tHi\tplace");
        testData.add("2\tBye\tnowhere");
        testData.add(null);

        Table testTable = new Table(dbName, "myTable", testData);
        assert (testTable.getNumberOfColumns() == 3);
        assert (testTable.getNumberOfRows() == 2);
        ArrayList<String> parsedTest = new ArrayList<>();
        parsedTest.add("1");
        parsedTest.add("Hi");
        parsedTest.add("place");
        assert (testTable.getSpecificRow(0).equals(parsedTest));
        assert (testTable.getSpecificColumn(0).equals("id"));
        assert (testTable.getTableName().equals("myTable"));
        testTable.setTableName("new");
        assert (testTable.getTableName().equals("new"));
        //testTable.setTableName(null);
        assert (testTable.getElement(0, 0).equals("1"));
        assert (testTable.getElement(1, 2).equals("nowhere"));
    }

    private void testRow() throws DBException{
        ArrayList<String> elements = new ArrayList<>();
        String tableName = "newTable";
        elements.add("1");
        elements.add("Hi");
        elements.add("place");
        Row testRow = new Row(tableName, elements, 3);
        assert(testRow.getElements().equals(elements));
        assert(testRow.getPrimaryKey().equals("1"));
        assert(testRow.getElement(1).equals("Hi"));
        assert(testRow.setElement("Them", 1));
        assert(testRow.getElement(1).equals("Them"));
        //testRow.setElement("T", 4);
        //testRow.setElement("T", -4);
        assert(testRow.setElement("6", 0));
        assert(testRow.getElement(0).equals("6"));
        //testRow.setElement(null, 2);
    }

    private void testColumn() throws DBException {
        String tableName = "testTable";
        Column testColumn = new Column(tableName,"id", 0);
        assert(testColumn.getColumnIndex()==0);
        testColumn.setColumnIndex(1);
        assert(testColumn.getColumnIndex()==1);
        assert(testColumn.getColumnName().equals("id"));
        testColumn.setColumnName("hi");
        assert(testColumn.getColumnName().equals("hi"));
        //testColumn.setColumnName(null);
    }
}
