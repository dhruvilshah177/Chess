package model;

/**
 * class for the players
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Player {

	//color of piece
    private boolean isWhitePlayer;

    /**
     * @param _isWhitePlayer player
     */
    
    public Player(boolean _isWhitePlayer) {
        isWhitePlayer = _isWhitePlayer;
    }

    /**
     * @return prompt for player to enter moves
     */
    
    public String getPrompt() {
        return (isWhitePlayer ? "White" : "Black") + "'s move: ";
    }

    /**
     * @return if it is a White player, then true; otherwise, false if is a Black player
     */
    
    public boolean isWhitePlayer() {
        return isWhitePlayer;
    }
}