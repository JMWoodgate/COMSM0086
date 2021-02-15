import java.util.*;

class OXOModel
{
    private OXOPlayer cells[][];
    private OXOPlayer players[];
    private OXOPlayer currentPlayer;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh)
    {
        winThreshold = winThresh;
        cells = new OXOPlayer[numberOfRows][numberOfColumns];
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
        return cells.length;
    }

    public int getNumberOfColumns()
    {
        return cells[0].length;
    }

    public OXOPlayer getCellOwner(int rowNumber, int colNumber)
    {
        return cells[rowNumber][colNumber];
    }

    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player)
    {
        cells[rowNumber][colNumber] = player;
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

    public boolean expandBoard()
    {
        if(isGameDrawn()){
            int rows, cols;
            OXOPlayer newCells[][];
            rows = getNumberOfRows();
            cols = getNumberOfColumns();
            rows++;
            cols++;
            newCells = new OXOPlayer[rows][cols];
            assert(copyCells(cells, newCells));
            cells = newCells;
            return true;
        }
        return false;
    }

    public boolean copyCells(OXOPlayer oldCells[][], OXOPlayer newCells[][])
    {
        int j, i;

        for(j = 0; j < oldCells.length; j++){
            for(i = 0; i < oldCells[0].length; i++){
                newCells[j][i] = oldCells[j][i];
            }
        }
        return true;
    }
}
