public class InvalidIdentifierCharacterException extends InvalidIdentifierException
{
    char character;
    RowOrColumn type;

    public InvalidIdentifierCharacterException()
    {

    }

    public String toString()
    {
        return "Invalid character identified";
    }
}