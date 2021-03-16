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
