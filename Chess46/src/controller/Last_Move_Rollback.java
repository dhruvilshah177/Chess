package controller;

import model.Board_Index;
import model.Pieces;

/**
 * this class is for when undoing a move is needed
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Last_Move_Rollback {

    //source index
    public Board_Index sourceIndex;

    //target index
    public Board_Index targetIndex;

   //source index for rook
    public Board_Index sourceIndexRook;

    //target index for rook
    public Board_Index targetIndexRook;

    // if the piece has moved
	public boolean isMove;

   //if the piece was removed
    public Pieces removedRegular;

    //if piece for promotion is removed
    public Pieces removedIsPromotion;

    //if move is performed
    public boolean isRegularMove;

    //if castling is performed
    public boolean isCastling;

    //if promotion is performed
    public boolean isPromotion;

    //pawn that moved two steps
    public Pieces lastTwoStepPawnMove = null;

    //pawn that last moved two steps
    public Pieces lastTwoStepPawnMovePrevious = null;

	/**
	 * @param isRollback checks if piece needs rollback
	 */
    
	public void doInit(boolean isRollb) {
		sourceIndex = null;
		targetIndex = null;
		sourceIndexRook = null;
		targetIndexRook	= null;
		
		isMove = false;
		removedRegular = null;
		removedIsPromotion = null;
		
		isRegularMove = false;
		isCastling = false;
		isPromotion	= false;
		
		if (isRollb) {
			lastTwoStepPawnMove	= lastTwoStepPawnMovePrevious;
		    lastTwoStepPawnMovePrevious	= null;
		}
		else {
		    lastTwoStepPawnMovePrevious	= lastTwoStepPawnMove;
			lastTwoStepPawnMove = null;
		}
	}

	
    /**
     * @return pawn that moved 2 steps in the last move
     */
    public Pieces getLastTwoStepMovePawn() {
    	return lastTwoStepPawnMove;
    }
}