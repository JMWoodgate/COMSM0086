package OXOExceptions;

public abstract class InvalidIdentifierException extends CellDoesNotExistException
{
    private int rowNumber;
    private int columnNumber;

    public InvalidIdentifierException()
    {
        super();
    }

    public InvalidIdentifierException(int row, int column)
    {
        super(row, column);
        rowNumber = row;
        columnNumber = column;
    }

}