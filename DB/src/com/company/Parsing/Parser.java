package com.company.Parsing;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;
import com.company.Tokenizer;

public class Parser {

    protected int index;
    private Tokenizer tokenizer;

    public Parser(String command) throws EmptyData {
        tokenizer = new Tokenizer(command);
        index = 0;
    }

    public Parser(){}

    private void parseCommand(){
        int index = 0;
        String nextCommand = tokenizer.nextToken(index);
        switch (nextCommand) {
            case "USE":
            case "CREATE":
            case "DROP":
            case "ALTER":
            case "INSERT":
            case "SELECT":
            case "UPDATE":
            case "DELETE":
            case "JOIN":
                break;
            default:
        }
    }

    private float floatLiteral(String token) throws DBException{
        if(token!=null){
            try{
                return Float.parseFloat(token);
            } catch (NumberFormatException e){
                e.printStackTrace();
                throw new CommandException(token, index, "float literal");
            }
        }
        throw new EmptyData("Command in floatLiteral");
    }

    private int integerLiteral(String token) throws DBException{
        if(token!=null){
            try{
                return Integer.parseInt(token);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                throw new CommandException(token, index, "integer literal");
            }
        }
        throw new EmptyData("Command in integerLiteral");
    }

    private boolean boolLiteral(String token) throws DBException{
        if(token!=null){
            if(token.equals("true")){
                return true;
            }
            else if(token.equals("false")){
                return false;
            }
            else{
                throw new CommandException(token, index, "bool literal");
            }
        }
        throw new EmptyData("Command in boolLiteral");
    }

    private boolean isOp(String token) throws DBException {
        if(token!=null) {
            switch (token) {
                case ("=="):
                case (">"):
                case ("<"):
                case (">="):
                case ("<="):
                case ("!="):
                case ("LIKE"):
                    break;
                default:
                    throw new CommandException(token, index, "operator");
            }
            return true;
        }
        throw new EmptyData("Command in isOp");
    }
}
