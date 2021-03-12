package com.company;

import java.util.ArrayList;

public class Test {

    private Row testRow;

    public Test(){
        testRow();
    }

    private void testRow(){
        ArrayList<String> elements = new ArrayList<>();
        elements.add("1  At  y@382");
        testRow = new Row(elements, 3);
        assert(testRow.getElements().equals(elements));
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
}
