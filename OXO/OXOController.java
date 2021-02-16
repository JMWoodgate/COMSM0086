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

        if(gameModel.getWinner()==null){
            changeCurrentPlayer(currentPlayer);
        }
        else{
            return;
        }
    }

    private boolean checkGameWon(OXOModel model)
    {
        int rowNum = model.getNumberOfRows();
        int colNum = model.getNumberOfColumns();

        for(int j = 0; j < rowNum; j++){
            if(!checkRowWon(model, j)){
                for(int i = 0; i < colNum; i++){
                    if(checkColWon(model, i)){
                        return true;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkColWon(OXOModel model, int colNum)
    {
        for(int i = 0; i < colNum - 1; i++){
            if (gameModel.getCellOwner(i, colNum) != gameModel.getCellOwner(i + 1, colNum)){
                return false;
            }
        }
        return true;
    }

    private boolean checkRowWon(OXOModel model, int rowNum)
    {
        for(int i = 0; i < rowNum - 1; i++){
            if (gameModel.getCellOwner(rowNum, i) != gameModel.getCellOwner(rowNum, i + 1)){
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
