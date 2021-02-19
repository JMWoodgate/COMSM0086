package OXOExceptions;

public class InvalidLengthException extends InvalidIdentifierException
{
    int length;

    public InvalidLengthException(int length)
    {
        super();
        this.length = length;
    }

    public String toString()
    {
        return ("Identifier consists of " + length + "characters but should be two");
    }
}