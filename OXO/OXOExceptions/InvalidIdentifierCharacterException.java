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
        return ("Invalid character: " + character + " " + type + " is not a letter");
    }
}