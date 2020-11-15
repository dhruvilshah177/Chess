package chess;

import model.Game;

/**
 * main class for chess 
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class ChessMain {
	
	 /**
	  *  @param args filename of moves
	  */
	
    public static void main(String[] args) {
        Game game = new Game();
        
        String para = null;
        if (args != null && args.length > 0 && args[0] != null && args[0].length() > 0) {
        	para = args[0];
        }
        
        game.playGame(para);
	}
}