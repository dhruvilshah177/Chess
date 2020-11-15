package model;

import controller.Directional_Distance;
import controller.Move.Promotion;
import controller.Outcome;
import controller.Source_Target;

import java.util.Map;

/**
 * this class is for the King piece
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class King extends Pieces {

	/**
	 * @param isWhite color of piece
	 * @param file file of current location
	 * @param rank rank of current location
	 * @param cb board
	 */
	
	public King(boolean isWhite, int file, int rank, Chessboard cb) {
        super(isWhite, file, rank, cb);
        cb.getPieceMap().put(getKey(), this);
    }

	/**
	 * @param isWhite color of piece
	 * @param fileRank input file and rank
	 * @param cb board
	 */
	
	public King(boolean isWhite, String fileRank, Chessboard cb) {
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
     * @param doMove if piece can perform the move, then true; otherwise, checks if piece can attack target and false
     * @return outcome of the move
     */
	
	private Outcome doMoveInternal(Board_Index targetIndex, boolean doMove) {
        Outcome outcome;
        Map<String, Pieces> pieceMap = chessBoard.getPieceMap();
        Board_Index currentIndex = getBoardIndex();
        Directional_Distance dd = new Directional_Distance(currentIndex.fileIndex, currentIndex.rankIndex, targetIndex.fileIndex, targetIndex.rankIndex);
        if (dd.isRegal()) {
       		if (isTargetEmptyOrLegal(targetIndex)) {
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
        
        else if (dd.isCastlingQS(isWhite()) || dd.isCastlingKS(isWhite())) {
            Board_Index rookSourceIndex;
       		Board_Index rookTargetIndex;
       		
       		if (isWhite()) {
       			if (dd.isCastlingQS(isWhite())) {
       				rookSourceIndex = new Board_Index(0, 0);
       	       		rookTargetIndex = new Board_Index(3, 0);
       			}
       			else {
       				rookSourceIndex = new Board_Index(7, 0);
       	       		rookTargetIndex = new Board_Index(5, 0);
       			}
       		}
       		else if (dd.isCastlingQS(isWhite())) {
				rookSourceIndex = new Board_Index(0, 7);
				rookTargetIndex = new Board_Index(3, 7);
			}
			else {
				rookSourceIndex = new Board_Index(7, 7);
				rookTargetIndex = new Board_Index(5, 7);
			}
       		
       		Pieces rook = pieceMap.get(rookSourceIndex.getKey());
       		
       		if (!isMoved() && (rook!=null) && !rook.isMoved()) {
	        	Directional_Distance ddCastling = new Directional_Distance(currentIndex.fileIndex, currentIndex.rankIndex, rookSourceIndex.fileIndex, rookSourceIndex.rankIndex);
	        	if (isNoneInBetween(ddCastling)) {
	                boolean isUnderAttack1 = chessBoard.isUnderAttack(isWhite(), currentIndex);
	                boolean isUnderAttack2 = chessBoard.isUnderAttack(isWhite(), targetIndex);
	                boolean isUnderAttack3 = chessBoard.isUnderAttack(isWhite(), new Board_Index((currentIndex.fileIndex+targetIndex.fileIndex)/2, currentIndex.rankIndex));
	        		if (!isUnderAttack1 && !isUnderAttack2 && !isUnderAttack3) {
	        			if (doMove) {
		           			boolean isLegal = doActualMoveCastling(targetIndex, rookSourceIndex, rookTargetIndex);
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
        }
        else {
        	return new Outcome(false, "Illegal move, try again");
        }
    	//
        return outcome;
    }

	/**
	 * @return if King has legal move for checkmate and stalemate, then true; otherwise, false
	 */

	@Override
	public boolean hasLegalMove() {	
		return hasLegalMoveOneDirectionOneStep(0, 1) != null
            || hasLegalMoveOneDirectionOneStep(0, -1) != null
            || hasLegalMoveOneDirectionOneStep(1, 0) != null
            || hasLegalMoveOneDirectionOneStep(-1, 0) != null
            || hasLegalMoveOneDirectionOneStep(1, 1) != null
            || hasLegalMoveOneDirectionOneStep(1, -1) != null
            || hasLegalMoveOneDirectionOneStep(-1, 1) != null
            || hasLegalMoveOneDirectionOneStep(-1, -1) != null;
	}


	/**
	 * @return a legal move
	 */
	
	@Override
    public Source_Target getOneLegalMove() {
    	Source_Target temp;
    	
    	temp = hasLegalMoveOneDirectionOneStep(0, 1);
    	if (temp!=null) {
    		return temp;
    	}
    
    	temp = hasLegalMoveOneDirectionOneStep(0, -1);
    	if (temp!=null) {
    		return temp;
    	}
 
    	temp = hasLegalMoveOneDirectionOneStep(1, 0);
    	if (temp!=null) {
    		return temp;
    	}
 
    	temp = hasLegalMoveOneDirectionOneStep(-1, 0);
    	if (temp!=null) {
    		return temp;
    	}
 
    	temp = hasLegalMoveOneDirectionOneStep(1, 1);
    	if (temp!=null) {
    		return temp;
    	}
 
    	temp = hasLegalMoveOneDirectionOneStep(1, -1);
    	if (temp!=null) {
    		return temp;
    	}
 
    	temp = hasLegalMoveOneDirectionOneStep(-1, 1);
    	if (temp!=null) {
    		return temp;
    	}
    	
    	temp = hasLegalMoveOneDirectionOneStep(-1, -1);
    	if (temp!=null) {
    		return temp;
    	}
    	
    	return null;
    }
    
    /**
     * @return board output for the King
     */
	
    @Override
    public String toString() {
        return super.toString() + "K";
    }
}