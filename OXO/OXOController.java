import OXOExceptions.*;

class OXOController
{
    OXOModel gameModel;

    public OXOController(OXOModel model)
    {
        gameModel = model;
        OXOPlayer firstPlayer = gameModel.getPlayerByNumber(0);
        gameModel.setCurrentPlayer(firstPlayer);
    }

    public void handleIncomingCommand(String command) throws OXOMoveException
    {
        int rowMax = gameModel.getNumberOfRows();
        int colMax = gameModel.getNumberOfColumns();
        OXOPlayer currentPlayer = gameModel.getCurrentPlayer();

        validateCommandLength(command);

        int x = (int)command.charAt(1);
        x = (Character.getNumericValue(x)) - 1;
        char yChar = command.charAt(0);

        validateCharacter(yChar, RowOrColumn.ROW, '0');
        validateCharacter((char)(x + '0'), RowOrColumn.COLUMN, command.charAt(1));

        int yNum = (letterToNum(yChar) - 1);

        validateCellRange(yNum, yChar, x, rowMax, colMax);
        validateCellEmpty(gameModel, yNum, yChar, x);

        if(!checkGameWon(gameModel)){
            gameModel.setCellOwner(yNum, x, currentPlayer);
        }
        if(checkGameWon(gameModel)){
            gameModel.setWinner(currentPlayer);
        }
        else{
            if(checkGameDrawn(gameModel, rowMax, colMax)){
                gameModel.setGameDrawn();
            }
            changeCurrentPlayer(gameModel, currentPlayer);
        }
    }

    private void validateCellEmpty(OXOModel model, int row, char rowChar, int col) throws CellAlreadyTakenException
    {
        if(!gameModel.isEmptyCell(row, col)){
            throw new CellAlreadyTakenException(rowChar, col + 1);
        }
    }

    private void validateCharacter(char character, RowOrColumn type, char colConversion) throws InvalidIdentifierCharacterException
    {
        if(type == RowOrColumn.ROW){
            if(!Character.isLetter(character)){
                throw new InvalidIdentifierCharacterException(character, type);
            }
        }
        else {
            if(!Character.isDigit(character)){
                throw new InvalidIdentifierCharacterException(colConversion, type);
            }
            else if(Character.isLetter(colConversion)){
                throw new InvalidIdentifierCharacterException(colConversion, type);
            }
        }
    }

    private void validateCommandLength(String command) throws InvalidLengthException
    {
        int length = command.length();

        if(length > 2){
            throw new InvalidLengthException(length);
        }
    }

    private void validateCellRange(int rowInt, char rowChar, int col,
                                   int rowMax, int colMax) throws OutsideCellRangeException
    {
        RowOrColumn type;

        if(rowInt >= rowMax){
            type = RowOrColumn.ROW;
            throw new OutsideCellRangeException(rowChar, type);
        }
        if(col >= colMax){
            type = RowOrColumn.COLUMN;
            throw new OutsideCellRangeException(col + 1, type);
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
        int win = model.getWinThreshold();
        int cnt = 1;

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
            cnt++;
            if(cnt == win){
                return true;
            }
            rowNum += rowDir;
            colNum += colDir;
        }
        return false;
    }

    private boolean checkRowWon(OXOModel model, int rowNum)
    {
        int win = model.getWinThreshold();

        for(int i = 0; i < win - 1; i++){
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
        int win = model.getWinThreshold();

        for(int i = 0; i < win - 1; i++){
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

    private void changeCurrentPlayer(OXOModel model, OXOPlayer currentPlayer)
    {
        int numPlayers = model.getNumberOfPlayers();
        int currentPlayerNum = model.getIndexOfPlayer(currentPlayer);
        int nextPlayerNum = currentPlayerNum + 1;
        if(nextPlayerNum == numPlayers){
            nextPlayerNum = 0;
        }
        OXOPlayer nextPlayer = model.getPlayerByNumber(nextPlayerNum);
        gameModel.setCurrentPlayer(nextPlayer);
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
