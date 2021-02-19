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
        int rowMax = gameModel.getNumberOfRows();
        int colMax = gameModel.getNumberOfColumns();

        OXOPlayer currentPlayer = gameModel.getCurrentPlayer();

        validateCommandLength(command);

        int x = (int)command.charAt(1);
        x = (Character.getNumericValue(x)) - 1;
        char y1 = command.charAt(0);
        validateCharacter(y1, RowOrColumn.ROW);
        int y2 = (letterToNum(y1) - 1);

        validateCellRange(y2, x, rowMax, colMax);
        validateCellEmpty(gameModel, y2, x);

        gameModel.setCellOwner(y2, x, currentPlayer);

        if(!checkGameWon(gameModel)){
            if(checkGameDrawn(gameModel, rowMax, colMax)){
                gameModel.setGameDrawn();
            }
            changeCurrentPlayer(currentPlayer);
        }
        else{
            gameModel.setWinner(currentPlayer);
        }
    }

    private void validateCellEmpty(OXOModel model, int row, int col) throws CellAlreadyTakenException
    {
        Exception e;
        if(!gameModel.isEmptyCell(row, col)){
            throw new CellAlreadyTakenException(row, col);
            System.out.println(e.CellAlreadyTakenException);
        }
    }

    private void validateCharacter(char character, RowOrColumn type) throws InvalidIdentifierCharacterException
    {
        Exception e;
        if(!Character.isLetter(character)){
            throw new InvalidIdentifierCharacterException(character, type);
            System.out.println(e.InvalidIdentifierCharacterException);
        }
    }

    private void validateCommandLength(String command) throws InvalidLengthException
    {
        int length = command.length();
        Exception e;

        if(length > 2){
            throw new InvalidLengthException(length);
            System.out.println(e.InvalidLengthException);
        }
    }

    private void validateCellRange(int row, int col, int rowMax, int colMax) throws OutsideCellRangeException
    {
        RowOrColumn type;
        Exception e;

        if(row > rowMax){
            type = RowOrColumn.ROW;
            throw new OutsideCellRangeException(row, type);
            System.out.println(e.OutsideCellRangeException);
        }
        if(col > colMax){
            type = RowOrColumn.COLUMN;
            throw new OutsideCellRangeException(row, type);
            System.out.println(e.OutsideCellRangeException);
        }
    }

    private boolean checkGameDrawn(OXOModel model, int rowNum, int colNum)
    {
        for(int j = 0; j < rowNum; j++){
            for(int i = 0; i < colNum; i++){
                if(model.isEmptyCell(j, i)){
                    return false;
                }
            }
        }
        return true;
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
            return true;
        }
        if(checkDiagonalWon(model, 0, (colNum - 1), 1, -1)){
            return true;
        }
        if(checkDiagonalWon(model, (rowNum - 1), 0, -1, 1)){
            return true;
        }
        if(checkDiagonalWon(model, (rowNum - 1), (colNum - 1), -1, -1)){
            return true;
        }
        return false;
    }

    private boolean checkDiagonalWon(OXOModel model, int rowNum,
                                     int colNum, int rowDir, int colDir)
    {
        int rowMax = model.getNumberOfRows();
        int colMax = model.getNumberOfColumns();

        while(rowNum <  rowMax && rowNum >= 0 && colNum < colMax && colNum >= 0){
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
        char lower = Character.toLowerCase(letter);

        for(int i = 1; i < 27; i++){
            if(lower == compare){
                return i;
            }
            compare++;
        }
        return 0;
    }

}