public class InvalidLengthException extends InvalidIdentifierException
{
    int length;

    public InvalidLengthException()
    {

    }

    public String toString()
    {
        return "Iidentifier consists of more than two characters";
    }
}