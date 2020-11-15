package controller;

import model.Board_Index;

import java.util.Arrays;
import java.util.List;

/**
 * for a move by a player
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Move {

    /**
     * all possible choices a player has when promoting
     */
    public enum Promotion {
		NONE, Knight, Queen, Rook, Bishop
	}

    // source position
    private Board_Index sourceBoardIndex = null;

    //target position
    private Board_Index targetBoardIndex  = null;

    //checks if the player resigned
    private boolean isResign = false;

    //checks if the player accepts the draw proposal
    private boolean isDraw = false;

    //checks if the player is proposing draw
    private boolean isAskingDraw = false;

    // AI
    private boolean isAI = false;
    
    //rollback
    private boolean isRollback = false;
    
    // checks if player chose promotion
    private Promotion promotion;

    //checks if the move is valid
    private boolean isValid = true;

    //reason behind the invalid move
    private String reason = "Initial";

    /**
     * @return source position
     */
    public Board_Index getSourceBoardIndex() {
        return sourceBoardIndex;
    }

    /**
     * @return target position
     */
    public Board_Index getTargetBoardIndex() {
        return targetBoardIndex;
    }

    /**
     * @return if player resigns, then true; otherwise, false
     */
    public boolean isResign() {
        return isResign;
    }

    /**
     * @return if player accept draw proposal, then true; otherwise, false
     */
    public boolean isDraw() {
        return isDraw;
    }

    /**
     * @return if player proposes draw, then true; otherwise, false
     */
    public boolean isAskingDraw() {
        return isAskingDraw;
    }

    /**
     * @return if AI, then true; otherwise, false
     */
    public boolean isAI() {
        return isAI;
    }
    
    /**
     * @return if Rollback, then true; otherwise, false
     */
    public boolean isRollback() {
        return isRollback;
    }
    
    /**
     * @return Promotion if player wants promotion for pawn
     */
    public Promotion getPromotion() {
    	return promotion;
    }

    /**
     * @return if valid move, then true; otherwise, false
     */
    public boolean isValid() {
        return isValid;
    }

    /**
     * @return Reason for invalid move
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param source current place
     * @param target where move will end
     */
    public void setSourceAndTargetForAIMove(Board_Index source, Board_Index target) {
    	sourceBoardIndex = new Board_Index(source) ;
    	targetBoardIndex  = new Board_Index(target);
    }
    
    
    /**
     * @param input player input
     */
    public Move(String input) {
    	promotion = Promotion.NONE;
        List<String> tokens = Arrays.asList(input.split("\\s+"));
        String first = tokens.get(0).trim().toUpperCase();
        
        if (first.equalsIgnoreCase("resign")) {
            isResign = true;
        }
        else if (first.equalsIgnoreCase("draw")) {
            isDraw = true;
        }
        else if (first.equalsIgnoreCase("AI")) {
        	isAI = true;
        }
        else if (first.equalsIgnoreCase("Rollback")) {
        	isRollback = true;
        }
        else if (first.length() == 2) {
            char charFile1 = first.charAt(0);
            char charRank1 = first.charAt(1);

            if (charFile1 >= 'A' && charFile1 <= 'H' && charRank1 >= '1' && charRank1 <= '8') {
                sourceBoardIndex = new Board_Index(charFile1-'A', charRank1 - '1');
            }
            else {
                isValid = false;                
                reason = "Illegal input, please enter again";
            }

            if (tokens.size() >= 2) {                
                String second = tokens.get(1).trim().toUpperCase();
                if (second.length() == 2) {
                    char charFile2 = second.charAt(0);
                    char charRank2 = second.charAt(1);
                    if (charFile2 >= 'A' && charFile2 <= 'H' && charRank2 >= '1' && charRank2 <= '8') {
                        targetBoardIndex = new Board_Index(charFile2 - 'A', charRank2 - '1');
                    }
                }
                else {
                    isValid = false;                   
                    reason = "Illegal input, please enter again";
                }

                if (tokens.size() >= 3) {                    
                    String third = tokens.get(2).trim();
                    if (third.equalsIgnoreCase("draw?")) {
                        isAskingDraw = true;
                    }
                    else if (third.equalsIgnoreCase("Q")) {
                    	promotion = Promotion.Queen;
                    }
                    else if (third.equalsIgnoreCase("N")) {
                    	promotion = Promotion.Knight;
                    }
                    else if (third.equalsIgnoreCase("R")) {
                    	promotion = Promotion.Rook;
                    }
                    else if (third.equalsIgnoreCase("B")) {
                    	promotion = Promotion.Bishop;
                    }
                }
            }
            else {
                isValid = false;                
                reason = "Illegal input, enter again";
            }
        }
        else {
            isValid = false;            
            reason = "Illegal input, enter again";
        }
    }
}