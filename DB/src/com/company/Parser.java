package com.company;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;

public class Parser {

    private int index;
    private String command;

    public Parser(String command){
        this.command = command;
        Tokenizer tokenizer = new Tokenizer(command);
        index = 0;
    }

    private void parseCommand(Tokenizer tokenizer){
        int index = 0;
        String nextCommand = tokenizer.nextToken(index);
        if(nextCommand.equals("SELECT")){

        }
        else if(nextCommand.equals("CREATE")){

        }
        else if(nextCommand.equals("USE")){

        }
    }

    private void isOp(String token) throws DBException {
        switch(token){
            case("=="):
            case(">"):
            case("<"):
            case(">="):
            case("<="):
            case("!="):
            case("LIKE"):
                break;
            default:
                throw new CommandException(token, index, "operator");
        }
    }
}
