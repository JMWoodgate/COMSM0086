package OXOExceptions;

public class InvalidIdentifierCharacterException extends InvalidIdentifierException
{
    char character;
    RowOrColumn type;

    public InvalidIdentifierCharacterException(char character, RowOrColumn type)
    {
        super();
        this.character = character;
        this.type = type;
    }

    public String toString()
    {
        if(type == RowOrColumn.ROW){
            return ("Invalid character in " + type + ": " + character + " is not a letter");
        }
        else{
            return ("Invalid character in " + type + ": " + character + " is not a number");
        }

    }
}