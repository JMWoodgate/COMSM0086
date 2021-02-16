import OXOExceptions.*;

class OXOController
{
    OXOModel gameModel;

    public OXOController(OXOModel model)
    {
        OXOPlayer firstPlayer = new OXOPlayer('X');
        gameModel = model;
        gameModel.setCurrentPlayer(firstPlayer);
    }


    public void handleIncomingCommand(String command) throws OXOMoveException
    {
        OXOPlayer currentPlayer = gameModel.getCurrentPlayer();
        System.out.println("command: " + command);
        char array[] = command.toCharArray();
        System.out.println("array: " + array);
        int x = (int)array[1];
        System.out.println("x: "+ x);
        int y = (int)array[0];
        System.out.println("y: "+ y);
        gameModel.setCellOwner(x, y, currentPlayer);
    }

}
