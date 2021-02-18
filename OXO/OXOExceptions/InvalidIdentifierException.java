public class InvalidIdentifierException extends CellDoesNotExistException
{
    public InvalidIdentifierException()
    {

    }

    public String toString()
    {
        return "Invalid identifier";
    }
}