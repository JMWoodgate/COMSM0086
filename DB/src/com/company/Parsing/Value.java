package com.company.Parsing;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;

import java.util.ArrayList;

public class Value {

    private int intLiteral;
    private boolean booleanLiteral;
    private float floatLiteral;
    private String stringLiteral;
    private final ArrayList<String> tokenizedCommand;
    private final int index;
    private LiteralType type;

    public Value(ArrayList<String> tokenizedCommand, int index) throws DBException {
        this.tokenizedCommand = tokenizedCommand;
        this.index = index;
        if(tokenizedCommand!=null){
            setLiteralType();
        }
        else {
            throw new EmptyData("Command");
        }
    }

    public LiteralType getLiteralType() {
        return type;
    }

    public boolean getBooleanLiteral(){
        return booleanLiteral;
    }

    public float getFloatLiteral(){
        return floatLiteral;
    }

    public int getIntLiteral(){
        return intLiteral;
    }

    public String getStringLiteral(){
        return stringLiteral;
    }

    private void setLiteralType() throws DBException{
        if(integerLiteral(tokenizedCommand.get(index))){
            type = LiteralType.INTEGER;
        }
        else if(floatLiteral(tokenizedCommand.get(index))){
            type = LiteralType.FLOAT;
        }
        else if(boolLiteral(tokenizedCommand.get(index))){
            type = LiteralType.BOOLEAN;
        }
        else{
            type = LiteralType.STRING;
            //if the token isn't an int, float, or boolean, it is a stringLiteral
            stringLiteral = tokenizedCommand.get(index);
        }
    }

    private boolean floatLiteral(String token) throws DBException {
        if (token != null) {
            try {
                //getting the float value from the string
                floatLiteral = Float.parseFloat(token);
                return true;
            } catch (NumberFormatException e) {
                //if we can't get a float from the string, return false
                return false;
            }
        }
        else {
            throw new EmptyData("Command in floatLiteral");
        }
    }

    private boolean integerLiteral(String token) throws DBException {
        if(token!=null){
            try{
                //getting the integer value from the string
                intLiteral = Integer.parseInt(token);
                return true;
            } catch (NumberFormatException e) {
                //if we can't get an integer from the string, return false
                return false;
            }
        }
        else {
            throw new EmptyData("Command in integerLiteral");
        }
    }

    private boolean boolLiteral(String token) throws DBException{
        if(token!=null){
            if(token.equals("true")){
                //if the string is true, setting the boolean variable to true
                booleanLiteral = true;
                //returning true because the token is a boolean value
                return true;
            }
            else if(token.equals("false")){
                //if the string is true, setting the boolean variable to false
                booleanLiteral = false;
                //returning true because the token is a boolean value
                return true;
            }
            else{
                //otherwise return false because it is not a boolean value
                return false;
            }
        }
        else {
            //catching null pointers
            throw new EmptyData("Command in boolLiteral");
        }
    }

}
