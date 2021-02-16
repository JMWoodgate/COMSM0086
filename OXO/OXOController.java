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
        int length = command.length();
        //char array[] = new char[length];
        //System.out.println("array: " + array);
        int x = (int)command.charAt(1);
        x = (Character.getNumericValue(x)) - 1;
        System.out.println("x: "+ x);
        char y1 = command.charAt(0);
        int y2 = (letterToNum(y1) - 1);
        System.out.println("y: "+ y2);
        gameModel.setCellOwner(y2, x, currentPlayer);
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
