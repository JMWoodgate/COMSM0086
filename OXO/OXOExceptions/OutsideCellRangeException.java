package OXOExceptions;

public class OutsideCellRangeException extends CellDoesNotExistException
{
    int position;
    RowOrColumn type;

    public OutsideCellRangeException(int position, RowOrColumn type)
    {
        super();
        this.position = position;
        this.type = type;
    }

    public String toString()
    {
        if(type == RowOrColumn.ROW){
            return (type + " " + (char)position + " is a valid character, but it is out of range (i.e. too big or too small)");
        }
        else{
            return (type + " " + position + " is a valid character, but it is out of range (i.e. too big or too small)");
        }
    }
}