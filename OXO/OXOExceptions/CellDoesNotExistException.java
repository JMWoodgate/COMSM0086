package OXOExceptions;

public class CellDoesNotExistException extends OXOMoveException
{
    private int rowNumber;
    private int columnNumber;

    public CellDoesNotExistException()
    {
        super();
    }

    public CellDoesNotExistException(int row, int column)
    {
        super(row, column);
        rowNumber = row;
        columnNumber = column;
    }

    public String toString()
    {
        return ("Cell " + rowNumber + columnNumber + " does not exist");
    }
}