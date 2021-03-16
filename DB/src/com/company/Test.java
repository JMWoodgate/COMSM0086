package com.company;

import com.company.DBExceptions.DBException;
import com.company.Parsing.Parser;

import java.util.ArrayList;

public class Test {

    public Test() throws DBException{
        try {
            testParser();
            testTokenizer();
            testTable();
            testRow();
            testColumn();
        }
        catch(DBException e){
            System.out.println("DBException " + e);
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
