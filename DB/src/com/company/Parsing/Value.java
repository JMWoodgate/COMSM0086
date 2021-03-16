package com.company.Parsing;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;

import java.util.ArrayList;

public class Value extends Parser{

    private int intLiteral;
    private boolean boolLiteral;
    private float floatLiteral;
    private ArrayList<String> tokenizedCommand;
    private int index;
    private LiteralType type;

    public Value(ArrayList<String> tokenizedCommand, int index) throws DBException {
        this.tokenizedCommand = tokenizedCommand;
        this.index = index;
        if(tokenizedCommand!=null){
            setLiteralType();

        }
        throw new EmptyData("Command");
    }

    public LiteralType getLiteralType() {
        return type;
    }

    private void setLiteralType() throws DBException{
        if(floatLiteral(tokenizedCommand.get(index))){
            type = LiteralType.FLOAT;
        }
        else if(integerLiteral(tokenizedCommand.get(index))){
            type = LiteralType.INTEGER;
        }
        else{
            boolLiteral(tokenizedCommand.get(index));
            type = LiteralType.BOOLEAN;
        }
    }

    private boolean floatLiteral(String token) throws DBException {
        if (token != null) {
            try {
                floatLiteral = Float.parseFloat(token);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        throw new EmptyData("Command in floatLiteral");
    }

    private boolean integerLiteral(String token) throws DBException {
        if(token!=null){
            try{
                intLiteral = Integer.parseInt(token);
                return true;
            } catch (NumberFormatException e) {
                return false;
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
                throw new CommandException(token, index, "bool, int or float literal");
            }
        }
        throw new EmptyData("Command in boolLiteral");
    }

}
