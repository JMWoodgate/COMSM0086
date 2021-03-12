package com.company;

import java.util.ArrayList;

public class Test {

    public Test(){
        testTable();
        testRow();
        testColumn();
    }

    private void testTable(){
        ArrayList<String> testData = new ArrayList<>();
        testData.add("id\tname\taddress");
        testData.add("1\tHi\tplace");
        testData.add("2\tBye\tnowhere");
        testData.add(null);
        Table testTable = new Table("myTable", testData);
        assert(testTable.getNumberOfColumns()==3);
        assert(testTable.getNumberOfRows()==2);
        ArrayList<String> parsedTest = new ArrayList<>();
        parsedTest.add("1");
        parsedTest.add("Hi");
        parsedTest.add("place");
        assert(testTable.getSpecificRow(0).equals(parsedTest));
        assert(testTable.getSpecificColumn(0).equals("id"));
        assert(testTable.getTableName().equals("myTable"));
        assert(testTable.setTableName("new"));
        assert(testTable.getTableName().equals("new"));
        assert(!testTable.setTableName(null));
        assert(testTable.getElement(0,0).equals("1"));
        assert(testTable.getElement(1,2).equals("nowhere"));
    }

    private void testRow(){
        ArrayList<String> elements = new ArrayList<>();
        elements.add("1\tAt\ty@382");
        Row testRow = new Row(elements, 3);
        assert(testRow.getElements().equals(elements));
        System.out.println("test get key " + testRow.getPrimaryKey());
        assert(testRow.getPrimaryKey().equals("1"));
        assert(testRow.getElement(1).equals("At"));
        assert(testRow.setElement("Them", 1));
        assert(testRow.getElement(1).equals("Them"));
        assert(!testRow.setElement("T", 4));
        assert(!testRow.setElement(null, 2));
        assert(!testRow.setElement("T", -4));
        assert(testRow.setElement("6", 0));
        assert(testRow.getElement(1).equals("6"));
    }

    private void testColumn(){
        Column testColumn = new Column("id", 0);
        assert(testColumn.getColumnIndex()==0);
        testColumn.setColumnIndex(1);
        assert(testColumn.getColumnIndex()==1);
        assert(testColumn.getColumnName().equals("id"));
        assert(testColumn.setColumnName("hi"));
        assert(testColumn.getColumnName().equals("hi"));
        assert(!testColumn.setColumnName(null));
    }
}
