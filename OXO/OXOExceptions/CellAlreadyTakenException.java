public class CellAlreadyTakenException extends OXOMoveException
{
    public CellAlreadyTakenException()
    {

    }

    public String toString()
    {
        return "Cell exists, but it has already been claimed by a player";
    }
}