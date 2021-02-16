import OXOExceptions.*;

class OXOController
{
    OXOModel gameModel;

    public OXOController(OXOModel model)
    {
        OXOPlayer firstPlayer = new OXOPlayer('X');
        gameModel = model;
        gameModel.setCurrentPlayer(firstPlayer);
        initWinThreshold(gameModel);
    }

    public void handleIncomingCommand(String command) throws OXOMoveException
    {
        OXOPlayer currentPlayer = gameModel.getCurrentPlayer();
        int x = (int)command.charAt(1);
        x = (Character.getNumericValue(x)) - 1;
        char y1 = command.charAt(0);
        int y2 = (letterToNum(y1) - 1);
        gameModel.setCellOwner(y2, x, currentPlayer);

        if(!checkGameWon(gameModel)){
            changeCurrentPlayer(currentPlayer);
        }
        else{
            gameModel.setWinner(currentPlayer);
        }
    }

    private boolean checkGameWon(OXOModel model)
    {
        int rowNum = model.getNumberOfRows();
        int colNum = model.getNumberOfColumns();

        for(int i = 0; i < rowNum; i++){
            if(checkRowColWon(model, i)){
                return true;
            }
        }
        for(int i = 0; i < colNum; i++){
            if(checkRowColWon(model, i)){
                return true;
            }
        }
        return false;
    }

    private boolean checkRowColWon(OXOModel model, int rowColNum)
    {
        for(int i = 0; i < rowColNum - 1; i++){
            if (gameModel.getCellOwner(i, rowColNum) != gameModel.getCellOwner(i + 1, rowColNum)){
                return false;
            }
        }
        return true;
    }

    private void initWinThreshold(OXOModel model)
    {
        if(model.getNumberOfRows() < model.getNumberOfColumns()){
            model.setWinThreshold(model.getNumberOfRows());
        }
        else{
            model.setWinThreshold(model.getNumberOfColumns());
        }
    }

    private void changeCurrentPlayer(OXOPlayer currentPlayer)
    {
        if('X' == currentPlayer.getPlayingLetter()){
            OXOPlayer nextPlayer = new OXOPlayer('O');
            gameModel.setCurrentPlayer(nextPlayer);
        }
        else{
            OXOPlayer nextPlayer = new OXOPlayer('X');
            gameModel.setCurrentPlayer(nextPlayer);
        }
    }

    private int letterToNum(char letter)
    {
        char compare = 'a';
        for(int i = 1; i < 27; i++){
            if(letter == compare){
                return i;
            }
            compare++;
        }
        return 0;
    }

}
