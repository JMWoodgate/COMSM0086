package com.company;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBCommand.*;
import com.company.DBExceptions.FileException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Test {

    public Test() throws DBException{
        try {
            //testFiles();
            testParseConditions();
            testCondition();
            testWildAttributeList();
            testNameValueList();
            testValueList();
            testJoinCMD();
            testDeleteCMD();
            testUpdateCMD();
            testSelectCMD();
            testInsertCMD();
            testAlterCMD();
            testDropCMD();
            //testCreateCMD();
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

    private void testFiles() throws DBException, IOException {
        try{
            //create parent folder ("databases")
            String folderName = "."+ File.separator+"databases";
            File parentFolder = new File(folderName);
            if(!parentFolder.exists()) {
                final boolean mkdir = parentFolder.mkdir();
                //create new folder (for new database)
                if (!mkdir) {
                    throw new IOException();
                }
            }
            //create new database folder inside parent folder
            FileIO fileIO = new FileIO("newDatabase");
            fileIO.makeFolder(folderName, "newDatabase");
            fileIO.makeFile(folderName+File.separator+"newDatabase", "newTable");
        }catch(DBException e){
            throw new FileException(e);
        }
    }

    private void testParseConditions() throws DBException{
        String command1 = "DELETE FROM elections WHERE party == 'green';";
        String command2 = "DELETE FROM elections WHERE ((field == 'labour')OR(ward!=true))AND id>5;";
        ArrayList<String> elements1 = new ArrayList<>();
        elements1.add("(");
        elements1.add("(");
        elements1.add("field");
        elements1.add("=");
        elements1.add("=");
        elements1.add("'labour'");
        elements1.add(")");
        elements1.add("or");
        elements1.add("(");
        elements1.add("ward");
        elements1.add("!");
        elements1.add("=");
        elements1.add("true");
        elements1.add(")");
        elements1.add(")");
        elements1.add("and");
        elements1.add("id");
        elements1.add(">");
        elements1.add("5");
        elements1.add(";");
        try{
            Parser parser1 = new Parser(command1, "."+ File.separator+"databases");
            assert(parser1.getConditionListArray().get(0).getConditionString().equals("party=='green'"));
            assert(parser1.getTableName().equals("elections"));

            ConditionList conditionList = new ConditionList(elements1, 0);
            assert(conditionList.getConditionList().get(0).getConditionString().equals("field=='labour'"));
            assert(conditionList.getConditionList().get(1).getConditionString().equals("ward!=true"));
            assert(conditionList.getConditionList().get(2).getConditionString().equals("id>5"));
            Parser parser2 = new Parser(command2, "."+ File.separator+"databases");
            assert(parser2.getConditionListArray().get(0).getConditionString().equals("field=='labour'"));
            assert(parser2.getConditionListArray().get(1).getConditionString().equals("ward!=true"));
            assert(parser2.getConditionListArray().get(2).getConditionString().equals("id>5"));
            assert(parser2.getTableName().equals("elections"));
        }catch(DBException e){
            throw new CommandException(command1, 0, "testParseCondition", e);
        }
    }

    private void testCondition() throws DBException{
        ArrayList<String> elements1 = new ArrayList<>();
        elements1.add("party");
        elements1.add("<");
        elements1.add("=");
        elements1.add("3");
        elements1.add(";");
        try{
            Condition condition = new Condition(elements1, 0);
            assert(condition.getIndex()==4);
            assert(condition.getOp().equals("<="));
            assert(condition.getAttribute().equals("party"));
            assert(condition.getValueString().equals("3"));
        }catch(DBException e){
            throw new CommandException(elements1.get(0), 0, "testCondition", e);
        }
    }

    private void testWildAttributeList() throws DBException{
        ArrayList<String> elements1 = new ArrayList<>();
        elements1.add("party");
        elements1.add(",");
        elements1.add("ward");
        elements1.add("from");
        elements1.add(";");
        ArrayList<String> elements2 = new ArrayList<>();
        elements2.add("*");
        elements2.add("from");
        elements2.add(";");
        try{
            WildAttributeList wildAttributeList1 = new WildAttributeList(elements1, 0);
            assert(wildAttributeList1.getAttributeList().get(0).equals("party"));
            assert(wildAttributeList1.getAttributeList().get(1).equals("ward"));
            //assert(wildAttributeList1.getIndex()==4);
            WildAttributeList wildAttributeList2 = new WildAttributeList(elements2, 0);
            assert(wildAttributeList2.getAttributeList().get(0).equals("*"));
            //assert(wildAttributeList2.getIndex()==2);
        }catch(DBException e){
            throw new CommandException(elements1.get(0), 0, "wild attribute list", e);
        }
    }

    private void testNameValueList() throws DBException{
        ArrayList<String> elements1 = new ArrayList<>();
        elements1.add("ward");
        elements1.add("=");
        elements1.add("3");
        elements1.add(",");
        elements1.add("party");
        elements1.add("=");
        elements1.add("'green'");
        elements1.add(";");
        try{
            NameValueList nameValueList = new NameValueList(elements1, 0);
            assert(nameValueList.getAttributeList().get(0).equals("ward"));
            assert(nameValueList.getValueList().get(0).equals("3"));
            assert(nameValueList.getAttributeList().get(1).equals("party"));
            assert(nameValueList.getValueList().get(1).equals("'green'"));
            assert(nameValueList.getIndex()==7);
        }catch(DBException e){
            throw new CommandException(elements1.get(0), 0, "testValueList", e);
        }
    }

    private void testValueList() throws DBException {
        ArrayList<String> elements = new ArrayList<>();
        elements.add("(");
        elements.add("true");
        elements.add(",");
        elements.add("3.122");
        elements.add(",");
        elements.add("'pla");
        elements.add("!");
        elements.add("ce'");
        elements.add(",");
        elements.add("','");
        elements.add(")");
        try{
            ValueList valueList = new ValueList(elements, 0);
            assert(valueList.getValueListString().get(0).equals("true"));
            assert(valueList.getValueListString().get(3).equals("','"));
            assert(valueList.getIndex()==10);
        }catch(DBException e){
            throw new CommandException(elements.get(0), 0, "testValueList", e);
        }
    }

    private void testJoinCMD() throws DBException {
        String command = "JOIN parties AND ward ON name AND id;";
        try{
            Parser testParser = new Parser(command, "."+ File.separator+"databases");
            ArrayList<String> tokenizedCommand = testParser.getTokenizedCommand();
            JoinCMD testJoin = new JoinCMD(tokenizedCommand, 0);
            assert(testJoin.getIndex()==8);
            assert(testJoin.getTableNames().get(0).equals("parties"));
            assert(testJoin.getTableNames().get(1).equals("ward"));
            assert(testJoin.getAttributeNames().get(0).equals("name"));
            assert(testJoin.getAttributeNames().get(1).equals("id"));
        }
        catch(DBException e){
            throw new CommandException(command, 0, "JOIN", e);
        }
    }

    private void testDeleteCMD() throws DBException {
        String command = "DELETE FROM elections WHERE (id<3) AND (party!='labour');";
        try{
            Parser testParser = new Parser(command, "."+ File.separator+"databases");
            ArrayList<String> tokenizedCommand = testParser.getTokenizedCommand();
            DeleteCMD testDelete = new DeleteCMD(tokenizedCommand, 0);
            assert(testDelete.getTableName().equals("elections"));
        }
        catch(DBException e){
            throw new CommandException(command, 0, "DELETE", e);
        }
    }

    private void testUpdateCMD() throws DBException {
        String command = "UPDATE elections SET party='name', ward=9 WHERE (id<3) AND (party!='labour');";
        try{
            Parser testParser = new Parser(command, "."+ File.separator+"databases");
            ArrayList<String> tokenizedCommand = testParser.getTokenizedCommand();
            UpdateCMD testUpdate = new UpdateCMD(tokenizedCommand, 0);
            assert(testUpdate.getTableName().equals("elections"));
            ArrayList<String> attributeList = testUpdate.getAttributeList();
            assert(attributeList.get(0).equals("party"));
            assert(attributeList.get(1).equals("ward"));
            attributeList = testUpdate.getValueList();
            assert(attributeList.get(0).equals("'name'"));
            assert(attributeList.get(1).equals("9"));
        }
        catch(DBException e){
            throw new CommandException(command, 0, "UPDATE", e);
        }
    }

    private void testSelectCMD() throws DBException {
        String command = "SELECT ward, parties FROM elections WHERE (id<3) AND (party!='labour');";
        try{
            Parser testParser = new Parser(command, "."+ File.separator+"databases");
            ArrayList<String> tokenizedCommand = testParser.getTokenizedCommand();
            SelectCMD testSelect = new SelectCMD(tokenizedCommand, 0);
            assert(testSelect.getTableName().equals("elections"));
            assert(testSelect.getAttributeList().get(0).equals("ward"));
            assert(testSelect.getAttributeList().get(1).equals("parties"));
        }
        catch(DBException e){
            throw new CommandException(command, 0, "SELECT", e);
        }
    }

    private void testInsertCMD() throws DBException {
        String command = "INSERT INTO elections VALUES ('name', false, 23, 'h=5');";
        try{
            Parser testParser = new Parser(command, "."+ File.separator+"databases");
            ArrayList<String> tokenizedCommand = testParser.getTokenizedCommand();
            InsertCMD testInsert = new InsertCMD(tokenizedCommand, 0);
            assert(testInsert.getTableName().equals("elections"));
        }
        catch(DBException e){
            throw new CommandException(command, 0, "INSERT", e);
        }
    }

    private void testAlterCMD() throws DBException {
        String command = "ALTER TABLE elections ADD party;";
        try{
            Parser testParser = new Parser(command, "."+ File.separator+"databases");
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
            Parser testParser1 = new Parser(command1, "."+ File.separator+"databases");
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
            Parser testParser2 = new Parser(command2, "."+ File.separator+"databases");
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
        String command2 = "CREATE database politics;";
        try{
            Parser testParser2 = new Parser(command2, "."+ File.separator+"databases");
            ArrayList<String> tokenizedCommand2 = testParser2.getTokenizedCommand();
            CreateCMD testCreate2 = new CreateCMD(tokenizedCommand2, 0, "."+ File.separator+"databases");
            assert(testCreate2.getIndex()==3);
            assert(testCreate2.getDatabaseName().equals("politics"));
        }
        catch(DBException e){
            throw new CommandException(command2, 0, "CREATE", e);
        }
        String command1 = "CREATE table elections (party, ward);";
        try{
            Parser testParser1 = new Parser(command1, "."+ File.separator+"databases"+ File.separator+"politics");
            ArrayList<String> tokenizedCommand1 = testParser1.getTokenizedCommand();
            CreateCMD testCreate1 = new CreateCMD(tokenizedCommand1, 0, "."+ File.separator+"databases"+ File.separator+"politics");
            assert(testCreate1.getTableName().equals("elections"));
        }
        catch(DBException e){
            throw new CommandException(command1, 0, "CREATE", e);
        }
    }

    private void testUseCMD()throws DBException{
        String command = "USE elections ;";
        try{
            Parser testParser = new Parser(command, "."+ File.separator+"databases");
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
            elements.add("'place'");
            elements.add("3.122");
            elements.add("'pla");
            elements.add("!");
            elements.add("ce'");
            Value testValue1 = new Value(elements, 0);
            assert (testValue1.getLiteralType() == LiteralType.INTEGER);
            assert (testValue1.getIntLiteral() == 1);
            Value testValue2 = new Value(elements, 1);
            assert (testValue2.getLiteralType() == LiteralType.BOOLEAN);
            assert (testValue2.getBooleanLiteral());
            Value testValue3 = new Value(elements, 2);
            assert (testValue3.getLiteralType() == LiteralType.STRING);
            assert (testValue3.getStringLiteral().equals("'place'"));
            Value testValue4 = new Value(elements, 3);
            assert (testValue4.getLiteralType() == LiteralType.FLOAT);
            assert (testValue4.getFloatLiteral() <= 3.122 || testValue4.getFloatLiteral() >= 3.122);
            Value testValue5 = new Value(elements, 4);
            assert (testValue5.getLiteralType() == LiteralType.STRING);
            assert (testValue5.getStringLiteral().equals("'pla!ce'"));
        }catch(DBException e){
            System.out.println("DBException " + e);
            e.printStackTrace();
        }
    }

    private void testParser() throws DBException {
        String testCommand = "FROM\tparties SELECT *;\n";
        Parser testParser = new Parser(testCommand, "."+ File.separator+"databases");
    }

    private void testTokenizer() throws DBException {
        String testCommand = "FROM 'pa1!rties' (SELECT) *;";
        try {
            ArrayList<String> tokenizedCommand = new ArrayList<>();
            Tokenizer testTokenizer = new Tokenizer(testCommand);
        } catch (DBException e){
            throw new CommandException(testCommand, 0, "tokenizer", e);
        }
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
