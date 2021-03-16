package com.company;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;

public class Parser {

    private final int index;
    private final Tokenizer tokenizer;

    public Parser(String command){
        tokenizer = new Tokenizer(command);
        index = 0;
    }

    private void parseCommand(){
        int index = 0;
        String nextCommand = tokenizer.nextToken(index);
        switch (nextCommand) {
            case "SELECT":
            case "CREATE":
                return;
            case "USE":
                break;
        }
    }

    protected boolean isOp(String token) throws DBException {
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
