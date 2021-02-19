package OXOExceptions;

public class CellAlreadyTakenException extends OXOMoveException
{
    private int rowNumber;
    private int columnNumber;

    public CellAlreadyTakenException(int row, int column)
    {
        super(row, column);
        rowNumber = row;
        columnNumber = column;
    }

    public String toString()
    {
        return ("Cell " + rowNumber + columnNumber + " exists, but it has already been claimed by a player");
    }
}