package com.company;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;

public class Parser {

    private final int index;
    private final Tokenizer tokenizer;

    public Parser(String command) throws EmptyData {
        tokenizer = new Tokenizer(command);
        index = 0;
    }

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
                return;
        }
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
