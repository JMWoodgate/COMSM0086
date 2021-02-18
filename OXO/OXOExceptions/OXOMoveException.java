package OXOExceptions;

public class OXOMoveException extends Exception
{
    private int rowNumber;
    private int columnNumber;

    public OXOMoveException()
    {
    }

    public OXOMoveException(int row, int column)
    {
        rowNumber = row;
        columnNumber = column;
    }

    public String toString()
    {
        return "Invalid move";
    }
    
    protected int getRow()
    {
        return rowNumber;
    }

    protected int getColumn()
    {
        return columnNumber;
    }

}