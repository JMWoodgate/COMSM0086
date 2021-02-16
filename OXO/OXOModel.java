import java.util.*;

class OXOModel
{
    private ArrayList<ArrayList<OXOPlayer>> cells;
    private OXOPlayer players[];
    private OXOPlayer currentPlayer;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;
    public int rowNum;
    public int colNum;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh)
    {
        winThreshold = winThresh;
        rowNum = numberOfRows;
        colNum = numberOfColumns;
        cells = new ArrayList<ArrayList<OXOPlayer>>();

        for(int i=0; i<rowNum; i++){
            ArrayList<OXOPlayer> row = new ArrayList<>();
            for(int j=0; j<colNum; j++){
                row.add(null);
            }
            cells.add(row);
        }

        players = new OXOPlayer[2];

    }

    public int getNumberOfPlayers()
    {
        return players.length;
    }

    public void addPlayer(OXOPlayer player)
    {
        for(int i=0; i<players.length ;i++) {
            if(players[i] == null) {
                players[i] = player;
                return;
            }
        }
    }

    public OXOPlayer getPlayerByNumber(int number)
    {
        return players[number];
    }

    public OXOPlayer getWinner()
    {
        return winner;
    }

    public void setWinner(OXOPlayer player)
    {
        winner = player;
    }

    public OXOPlayer getCurrentPlayer()
    {
        return currentPlayer;
    }

    public void setCurrentPlayer(OXOPlayer player)
    {
        currentPlayer = player;
    }

    public int getNumberOfRows()
    {
        return rowNum;
    }

    public int getNumberOfColumns()
    {
        return colNum;
    }

    public OXOPlayer getCellOwner(int rowNumber, int colNumber)
    {
        return cells.get(rowNumber).get(colNumber);
    }

    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player)
    {
        cells.get(rowNumber).set(colNumber, player);
    }

    public boolean isEmptyCell(int rowNumber, int colNumber)
    {
        if(cells.get(rowNumber).get(colNumber) == null){
            return true;
        }
        else{
            return false;
        }
    }

    public void setWinThreshold(int winThresh)
    {
        winThreshold = winThresh;
    }

    public int getWinThreshold()
    {
        return winThreshold;
    }

    public void setGameDrawn()
    {
        gameDrawn = true;
    }

    public boolean isGameDrawn()
    {
        return gameDrawn;
    }

}
