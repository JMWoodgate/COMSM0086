import java.util.*;

class OXOModel
{
    private ArrayList<ArrayList<OXOPlayer>> cells;
    private ArrayList<OXOPlayer> players;
    private OXOPlayer currentPlayer;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;
    public int rowNum;
    public int colNum;
    public int numPlayers;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh, int numberOfPlayers)
    {
        winThreshold = winThresh;
        rowNum = numberOfRows;
        colNum = numberOfColumns;
        numPlayers = numberOfPlayers;
        players = new ArrayList<OXOPlayer>(numPlayers);
        cells = new ArrayList<ArrayList<OXOPlayer>>();

        for(int i = 0; i < rowNum; i++){
            cells.add(new ArrayList<OXOPlayer>());
            for(int j = 0; j < colNum; j++){
                cells.get(i).add(null);
            }
        }
    }

    public boolean expandBoard()
    {
        for(int i = 0; i <= rowNum; i++){
            cells.add(rowNum, new ArrayList<OXOPlayer>());
            for(int j = 0; j <= colNum; j++){
                cells.get(i).add(null);
            }
        }

        rowNum++;
        colNum++;

        return true;
    }

    public void initPlayerArray()
    {
        char newLetter = 'A';
        OXOPlayer firstPlayer = new OXOPlayer('X');
        players.set(0, firstPlayer);
        OXOPlayer secondPlayer = new OXOPlayer('O');
        players.set(1, secondPlayer);

        for(int i = 2; i < numPlayers; i++){
            OXOPlayer newPlayer = new OXOPlayer(newLetter);
            players.add(i, newPlayer);
            newLetter++;
            if(newLetter == 'X' || newLetter == 'O'){
                newLetter++;
            }
        }
    }

    public int getNumberOfPlayers()
    {
        return numPlayers;
    }

    public void addPlayer(OXOPlayer player)
    {
        char newPlayer = player.getPlayingLetter();
        players.add(new OXOPlayer(newPlayer));
    }

    public int getIndexOfPlayer(OXOPlayer player)
    {
        char playerLetter = player.getPlayingLetter();
        OXOPlayer comparisonPlayer;
        char comparisonChar;

        for(int i = 0; i < numPlayers; i++){
            comparisonPlayer = players.get(i);
            comparisonChar = comparisonPlayer.getPlayingLetter();
            if(comparisonChar == playerLetter){
                return i;
            }
        }
        return 0;
    }

    public OXOPlayer getPlayerByNumber(int number)
    {
        return players.get(number);
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
