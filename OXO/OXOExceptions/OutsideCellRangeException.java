public class OutsideCellRangeException extends CellDoesNotExistException
{
    int position;
    RowOrColumn type;

    public OutsideCellRangeException()
    {

    }

    public String toString()
    {
        return "Identifiers are valid characters, but they are out of range (i.e. too big or too small)";
    }
}