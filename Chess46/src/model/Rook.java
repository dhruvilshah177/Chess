package model;

import controller.Directional_Distance;
import controller.Move.Promotion;
import controller.Outcome;
import controller.Source_Target;

/**
 * this class is for the Rook piece
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Rook extends Pieces {
    
    /**
     * @param isWhite color of piece
     * @param file file of current location.
     * @param rank rank of current location.
     * @param cb board
     */
	
    public Rook(boolean isWhite, int file, int rank, Chessboard cb) {
        super(isWhite, file, rank, cb);
        cb.getPieceMap().put(getKey(), this);
    }

    /**
     * @param isWhite color of piece
     * @param fileRank input file and rank
     * @param cb board
     */
    
    public Rook(boolean isWhite, String fileRank, Chessboard cb) {
        super(isWhite, fileRank, cb);
        cb.getPieceMap().put(getKey(), this);
    }


    /**
     * @param targetIndex target index a piece will move to
     * @param promotion Promotion used only for pawn
     * @return outcome of the move
     */
    
    public Outcome doMove(Board_Index targetIndex, Promotion promotion) {
        return doMoveInternal(targetIndex, true);
    }

    /**
     * @param targetIndex target index a piece will move to
     * @return outcome if the piece can attack the target
     */
    
    public Outcome doAttack(Board_Index targetIndex) {
        return doMoveInternal(targetIndex, false);
    }

    /**
     * @param targetIndex target index a piece will move to
     * @param doMove if piece perform move, then true; otherwise, false checks if piece can attack target
     * @return outcome of the move
     */
    
    private Outcome doMoveInternal(Board_Index targetIndex, boolean doMove) {
        Outcome outcome;

        Board_Index currentIndex = getBoardIndex();

        Directional_Distance dd = new Directional_Distance(currentIndex.fileIndex, currentIndex.rankIndex, targetIndex.fileIndex, targetIndex.rankIndex);

        if (dd.isParallel()) {
            if (isTargetEmptyOrLegal(targetIndex)) {
                if (isNoneInBetween(dd)) {
                    if (doMove) {
                        boolean isLegal = doActualMove(targetIndex, false);
                        if (isLegal) {
                            outcome = new Outcome(true, "OK");
                        }
                        else {
                            outcome = new Outcome(false, "Illegal move, try again");
                        }
                    }
                    else {
                        outcome = new Outcome(true, "OK");
                    }
                }
                else {
                    outcome = new Outcome(false, "Illegal move, try again");
                }
            }
            else {
                outcome = new Outcome(false, "Illegal move, try again");
            }
        }
        else {
            outcome = new Outcome(false, "Illegal move, try again");
        }

        return outcome;
    }

    /**
     * @return if Rook has legal move, then true; otherwise, false
     */

    @Override
    public boolean hasLegalMove() {
		return hasLegalMoveInOneDirection(0, 1)!=null
                || hasLegalMoveInOneDirection(0, -1)!=null
                || hasLegalMoveInOneDirection(1, 0)!=null
                || hasLegalMoveInOneDirection(-1, 0)!=null;
    }

    /**
     * @return a legal move for the Rook
     */
    
    @Override
    public Source_Target getOneLegalMove() {
    	Source_Target temp;

    	temp = hasLegalMoveInOneDirection(0, 1);
    	if (temp!=null) {
    		return temp;
    	}

    	temp = hasLegalMoveInOneDirection(0, -1);
    	if (temp!=null) {
    		return temp;
    	}

    	temp = hasLegalMoveInOneDirection(1, 0);
    	if (temp!=null) {
    		return temp;
    	}

    	temp = hasLegalMoveInOneDirection(-1, 0);
    	if (temp!=null) {
    		return temp;
    	}

    	return null;
    }
    
    /**
     * @return board output for the Rook
     */
    
    @Override
    public String toString() {
        return super.toString() + "R";
    }
}