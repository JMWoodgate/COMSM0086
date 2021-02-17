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
            if(checkRowWon(model, i)){
                return true;
            }
        }
        for(int i = 0; i < colNum; i++){
            if(checkColWon(model, i)){
                return true;
            }
        }
        if(checkDiagonalWon(model, 0, 0, 1, 1)){
            System.out.println("0 0 won");
            return true;
        }
        if(checkDiagonalWon(model, 0, (colNum - 1), 1, -1)){
            System.out.println("0 col won");
            return true;
        }
        if(checkDiagonalWon(model, (rowNum - 1), 0, -1, 1)){
            System.out.println("row 0 won");
            return true;
        }
        if(checkDiagonalWon(model, (rowNum - 1), (colNum - 1), -1, -1)){
            System.out.println("row col won");
            return true;
        }
        return false;
    }

    private boolean checkDiagonalWon(OXOModel model, int rowNum, int colNum, int rowDir, int colDir)
    {
        int rowMax = model.getNumberOfRows();
        int colMax = model.getNumberOfColumns();

        while(rowNum <=  rowMax - 1 && rowNum >= 0 && colNum <= colMax - 1 && colNum >= 0){
            System.out.println("row " + rowNum + " col " + colNum);
            if(model.isEmptyCell(rowNum, colNum) == true){
                return false;
            }
            else if(rowNum + rowDir <  rowMax && rowNum + rowDir >= 0 &&
                    colNum + colDir < colMax && colNum + colDir >= 0){
                if(model.isEmptyCell((rowNum + rowDir), (colNum + colDir)) == true){
                    return false;
                }
                else if (model.getCellOwner(rowNum, colNum).getPlayingLetter() !=
                        model.getCellOwner(rowNum + rowDir, colNum + colDir).getPlayingLetter()){
                    return false;
                }
            }
            rowNum += rowDir;
            colNum += colDir;
        }
        return true;
    }

    private boolean checkRowWon(OXOModel model, int rowNum)
    {
        int colNum = model.getNumberOfColumns();

        for(int i = 0; i < colNum - 1; i++){
            if(model.isEmptyCell(rowNum, i) == true){
                return false;
            }
            else if(model.isEmptyCell(rowNum, i + 1) == true){
                return false;
            }
            else if (model.getCellOwner(rowNum, i).getPlayingLetter() !=
                    model.getCellOwner(rowNum, i + 1).getPlayingLetter()){
                return false;
            }
        }
        return true;
    }

    private boolean checkColWon(OXOModel model, int colNum)
    {
        int rowNum = model.getNumberOfRows();

        for(int i = 0; i < rowNum - 1; i++){
            if(model.isEmptyCell(i, colNum) == true){
                return false;
            }
            if(model.isEmptyCell(i + 1, colNum) == true){
                return false;
            }
            else if (model.getCellOwner(i, colNum).getPlayingLetter() !=
                    model.getCellOwner(i + 1, colNum).getPlayingLetter()){
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
