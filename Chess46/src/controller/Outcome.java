package controller;

/**
 * outcome of the move
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Outcome {

    // checks if move was okay 
    private boolean isOK;

   //reason for why the move was invalid
    private String 	reason;

    //checks if the opponent is in check after the next move
    private boolean isOpponentInCheck	= false;

    
     //checks if opponent is in checkmate after move
    private boolean isOpponentCheckmate	= false;

   
    //checks if opponent is in stalemate after move
    private boolean isOpponentStalemate	= false;

    /**
     * @param inBool if move is successful, then true; otherwise, false
     * @param inString reason for invalid move
     */
    
    public Outcome(boolean input_Bool, String input_String) {
        isOK = input_Bool;
        reason = input_String;
    }
    /**
     * @return if move is successful, then true; otherwise, false
     */
    
    public boolean isOK() {
        return isOK;
    }
    /**
     * @return reason for invalid move
     */
    
    public String getReason() {
        return reason;
    }
    /**
     * @return if opponent is in check after move, then true; otherwise, false
     */
    
	public boolean isOpponentInCheck() {
		return isOpponentInCheck;
	}
    /**
     * set isOpponentInCheck to true
     */
	
    public void setOpponentInCheck() {
		this.isOpponentInCheck = true;
	}
    /**
     * @return if opponent is in checkmate after move, then true; otherwise, false
     */
    
    public boolean isOpponentCheckmate() {
		return isOpponentCheckmate;
	}

    
    public void setOpponentCheckmate() {
		this.isOpponentCheckmate = true;
	}

    /**
     * @return if opponent is in stalemate after move, then true; otherwise, false
     */
    public boolean isOpponentStalemate() {
		return isOpponentStalemate;
	}

    
    public void setOpponentStalemate() {
		this.isOpponentStalemate = true;
	}
}