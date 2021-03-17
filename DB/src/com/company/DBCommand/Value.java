package com.company.DBCommand;

import com.company.DBExceptions.CommandException;
import com.company.DBExceptions.DBException;
import com.company.DBExceptions.EmptyData;

import java.util.ArrayList;

public class Value {

    private int intLiteral;
    private boolean booleanLiteral;
    private float floatLiteral;
    private String stringLiteral;
    private final ArrayList<String> command;
    private final int index;
    private LiteralType type;
    private String value;

    public Value(ArrayList<String> command, int index) throws DBException {
        this.command = command;
        this.index = index;
        if(command!=null){
            setLiteralType();
            value = command.get(index);
        }
        else {
            throw new EmptyData("Command");
        }
    }

    public String getValue() throws EmptyData {
        if(value!=null) {
            return value;
        }
        throw new EmptyData("value");
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
        if(integerLiteral(command.get(index))){
            type = LiteralType.INTEGER;
        }
        else if(floatLiteral(command.get(index))){
            type = LiteralType.FLOAT;
        }
        else if(boolLiteral(command.get(index))){
            type = LiteralType.BOOLEAN;
        }
        else if(stringLiteral(command.get(index))){
            type = LiteralType.STRING;
        }
        else{
            throw new CommandException(command.get(index), index, "value");
        }
    }

    private boolean stringLiteral(String token) throws DBException {
        if(token.charAt(0)!='\''){
            return false;
        }
        if(token.charAt(token.length())!='\''){
            return false;
        }
        return true;
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
