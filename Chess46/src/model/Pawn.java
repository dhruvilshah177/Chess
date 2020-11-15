package model;

import controller.Directional_Distance;
import controller.Move.Promotion;
import controller.Outcome;
import controller.Source_Target;

/**
 * this class is for the Pawn piece
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Pawn extends Pieces{

	/**
	 * @param isWhite color of piece
	 * @param file file of current location
	 * @param rank rank of current location
	 * @param cb board
	 */
    public Pawn(boolean isWhite, int file, int rank, Chessboard cb) {
        super(isWhite, file, rank, cb);
        cb.getPieceMap().put(getKey(), this);
    }

    /**
	 * @param isWhite color of piece
	 * @param fileRank input file and rank
	 * @param cb board
	 */
    public Pawn(boolean isWhite, String fileRank, Chessboard cb) {
        super(isWhite, fileRank, cb);
        cb.getPieceMap().put(getKey(), this);
    }


    /**
     * @param targetIndex target index a piece will move to
     * @param promotion promotion for pawn
     * @return outcome of the move
     */
    @Override
    public Outcome doMove(Board_Index targetIndex, Promotion promotion) {
        return doMoveInternal(targetIndex, true, false, promotion);
    }


    /**
     * @param targetIndex target index a piece will move to
     * @return outcome if the piece can attack the target
     */
    public Outcome doAttack(Board_Index targetIndex) {
    	Promotion promotion;
    	promotion = Promotion.NONE;
        return doMoveInternal(targetIndex, false, false, promotion);
    }


    /**
     * @param targetIndex target index a piece will move to
     * @param doMove if piece can perform the move, then true; otherwise, checks if piece can attack target and false
     * @param rollback if true, rollback after checking move
     * @param promotion promotion when pawn reaches correct position on the board
     * @return outcome of the move
     */
    private Outcome doMoveInternal(Board_Index targetIndex, boolean doMove, boolean rollback, Promotion promotion) {
        Outcome outcome;
        Board_Index currentIndex = getBoardIndex();
        Directional_Distance dd = new Directional_Distance(currentIndex.fileIndex, currentIndex.rankIndex, targetIndex.fileIndex, targetIndex.rankIndex);

        if (doMove && dd.isPawnOneStep(isWhite())) {
       		if (isTargetEmpty(targetIndex)) {
				boolean isPromotion = dd.isPawnPromotion(isWhite());
				boolean isLegal;
				// Promotion
				if (isPromotion) {
                    isLegal = doActualMoveIsPromotion(targetIndex, promotion, rollback);
                }
                else {
                    isLegal = doActualMove(targetIndex, rollback);
                }
				outcome = isLegal ? new Outcome(true, "OK") : new Outcome(false, "Illegal move, try again");    // King will be in check
			}
       		else {       		    
       			outcome = new Outcome(false, "Illegal move, try again");
       		}
        }
        else if (dd.isPawnKill(isWhite())) {
       		if (isTargetLegal(targetIndex)) {
       			if (doMove) {
	                boolean isPromotion = dd.isPawnPromotion(isWhite());
	       			boolean isLegal;

	       			if (isPromotion) {
	       				isLegal = doActualMoveIsPromotion(targetIndex, promotion, rollback);
	       			}
	       			else {
	       				isLegal = doActualMove(targetIndex, rollback);
	       			}

                    outcome = isLegal ? new Outcome(true, "OK") : new Outcome(false, "Illegal move, try again"); 
       			}
       			else {
           			outcome = new Outcome(true, "OK");
       			}
       		} 
       		else {
       			outcome = new Outcome(false, "Illegal move, try again");
       		}
        }
        else if (doMove && dd.isPawnTwoStep(isWhite())) {
       		if (isTargetEmpty(targetIndex) && isTargetEmpty(new Board_Index(currentIndex.fileIndex, (currentIndex.rankIndex+targetIndex.rankIndex)/2))) {
				boolean isLegal = doActualMove(targetIndex, rollback);
				if (isLegal) {
                    outcome = new Outcome(true, "OK");
                }
                else {                 
                 outcome = new Outcome(false, "Illegal move, try again.");
                }
			}
       		else {       		   
       			outcome = new Outcome(false, "Illegal move, try again");
       		}
        }
        else {
            outcome = new Outcome(false, "Illegal move, try again");
        }
        //
        return outcome;
    }


    /**
     * @return if Pawn has legal move, then true; otherwise, false
     */
    @Override
	public boolean hasLegalMove() {
		if (isWhite()) {
			return 		isLegalMove(0, 1)!=null
					|| 	isLegalMove(0, 2)!=null 
					|| 	isLegalMove(1, 1)!=null
					|| 	isLegalMove(-1, 1)!=null;
		}
        return 		isLegalMove(0, -1)!=null
        		|| 	isLegalMove(0, -2)!=null
        		|| 	isLegalMove(1, -1)!=null
        		|| 	isLegalMove(-1, -1)!=null;
    }


    /**
     * @return a legal move for the Pawn piece
     */
    @Override
    public Source_Target getOneLegalMove() {
    	Source_Target temp;

		if (isWhite()) {
	    	temp = isLegalMove(0, 1);
	    	if (temp!=null) {
	    		return temp;
	    	}

	    	temp = isLegalMove(0, 2);
	    	if (temp!=null) {
	    		return temp;
	    	}

	    	temp = isLegalMove(1, 1);
	    	if (temp!=null) {
	    		return temp;
	    	}

	    	temp = isLegalMove(-1, 1);
	    	if (temp!=null) {
	    		return temp;
	    	}

	    	return null;
		}

		temp = isLegalMove(0, -1);
		if (temp!=null) {
            return temp;
        }

		temp = isLegalMove(0, -2);
		if (temp!=null) {
            return temp;
        }

		temp = isLegalMove(1, -1);
		if (temp!=null) {
            return temp;
        }

		temp = isLegalMove(-1, -1);
		if (temp!=null) {
            return temp;
        }

		return null;
	}
    
    
    
    /**
     * @param deltaFile change in file for step
     * @param deltaRank change in rank for step
     * @return if legal move after change, then true; otherwise, false if illegal move.
     */
    private Source_Target isLegalMove(int deltaFile, int deltaRank) {
		Board_Index currentIndex = getBoardIndex();
		Source_Target legalMove = null;
		int file = currentIndex.fileIndex + deltaFile;
		int rank = currentIndex.rankIndex + deltaRank;

		if (file >= 0 && file < 8 && rank >= 0 && rank < 8) {
    		Board_Index targetIndex = new Board_Index(file, rank);

        	Promotion promotion;
        	promotion = Promotion.NONE;
			Outcome outcome = doMoveInternal(targetIndex, true, true, promotion);
			
			if (outcome.isOK()) {
				legalMove = new Source_Target(currentIndex, targetIndex);
			}
			else {
				// do nothing
			}
		}
		return legalMove;
	}
    
    
    /**
     * @return board output for Pawn
     */
    @Override
    public String toString() {
        return super.toString() + "p";
    }
}