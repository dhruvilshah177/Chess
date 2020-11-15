package model;

import controller.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * main class for the board
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Chessboard {

	// map of pieces
    private Map<String, Pieces> pieceMap;

    //white king
    private Pieces whiteKing;

    //black king
    private Pieces blackKing;

    
   //rollback
    private Last_Move_Rollback lastMoveForRollback;
    
    /**
     * @return map of pieces
     */
    
    public Map<String, Pieces> getPieceMap() {
        return pieceMap;
    }

    /**
     * @return white king
     */
    
    public Pieces getWhiteKing() {
    	return whiteKing;
    }

    /**
     * @return black king
     */
    
    public Pieces getBlackKing() {
    	return blackKing;
    }
    
    /**
     * @return rollback
     */
    
    public Last_Move_Rollback getLastMoveForRollback() {
    	return lastMoveForRollback;
    }

    /**
     * initializes board to starting position
     */
    
    public Chessboard() {
    	doInitStart();
    }

    /**
     * @param move move from player input
     * @param isWhite color of piece
     * @return outcome of the move
     */

    public Outcome doMove(Move move, boolean isWhite) {
    	if (move.isAI()) {
    		Source_Target temp = getOneLegalMove(isWhite);
    		if (temp!=null) {
    		    move.setSourceAndTargetForAIMove(temp.getSource(), temp.getTarget());
        		return _doMove(move, isWhite);
    		}
            return new Outcome(false, "No Legal Move");
        }
        if (move.isRollback()) {
            boolean ret = Pieces.doRollback(this);
            if (ret) {
                return new Outcome(true, "Roll backed");
            }
            return new Outcome(false, "Not roll backed");
    	}
        return _doMove(move, isWhite);
    }

    /**
     * @param move move from player input
     * @param isWhite color of piece
     * @return outcome of the move
     */
    
    private Outcome _doMove(Move move, boolean isWhite) {
        Board_Index currentIndex = move.getSourceBoardIndex();
        Board_Index targetIndex = move.getTargetBoardIndex();

        Pieces sourcePiece = pieceMap.get(currentIndex.getKey());

        if (sourcePiece == null) {
            return new Outcome(false, "Illegal move, try again");
        }

        if (currentIndex.equals(targetIndex)) {
            return new Outcome(false, "Illegal move, try again");
        }
        
        if (sourcePiece.isWhite() == isWhite) {
            Outcome outcome = sourcePiece.doMove(targetIndex, move.getPromotion());
        
            if (outcome.isOK()) {
                Pieces opponentKing = (!isWhite) ? whiteKing : blackKing;       
                boolean opponentInCheck = isUnderAttack(!isWhite, opponentKing.getBoardIndex());
                boolean opponentHasLegalMove = hasLegalMove(!isWhite);

                if (opponentInCheck) {
                    if (!opponentHasLegalMove) {
                        outcome.setOpponentCheckmate();
                    }
                    else {
                        outcome.setOpponentInCheck();
                    }
                }
                else {
                    if (!opponentHasLegalMove) {
                        outcome.setOpponentStalemate();
                    }
                }
            }
            return outcome;
        }
        return new Outcome(false, "Illegal move, try again");
    }

    /**
     * @param isWhite color of piece
     * @return if all pieces of 1 color has at least 1 legal move, then true; otherwise, false
     */
    
    public boolean hasLegalMove(boolean isWhite) {
    	List<Pieces> list = Helper.filter(pieceMap.values(), isWhite, (p,is_white)->(p.isWhite()==is_white));
        return Helper.findOne(list, Pieces::hasLegalMove);
    }

    /**
     * @param isWhite color of piece
     * @return a legal move
     */
    
    public Source_Target getOneLegalMove(boolean isWhite) {
    	List<Pieces> list = Helper.filter(pieceMap.values(), isWhite, (p,is_white)->(p.isWhite()==is_white));
    	
    	if (list.size()>0) {
        	long lng = System.currentTimeMillis();
        	lng = lng >>> 2;
        	lng = lng % list.size();
        	
        	int start = (int) lng;
            for (int i=0; i<list.size(); i++) {
            	int j = (i + start) % list.size();
            	Source_Target temp = list.get(j).getOneLegalMove();
                if (temp!=null) {
                    return temp;
                }
            }            
            return null;
    	}
        return null;
    }
    
    /**
     * @param isWhite color of piece
     * @param targetIndex target index
     * @return if a piece is under attack, then true; otherwise, false
     */

    public boolean isUnderAttack(boolean isWhite, Board_Index targetIndex) {
    	return Helper.findOne(pieceMap.values(), targetIndex, isWhite, (p, tIndex, is_white) -> p.isWhite() == !is_white && p.doAttack(tIndex).isOK());
    }

   //sets pieces to their initial position
    
    private void doInitStart() {
        pieceMap = new HashMap<>();

    	lastMoveForRollback = new Last_Move_Rollback();

        whiteKing = new King(true, 4, 0, this);
        blackKing = new King(false, 4, 7, this);

        for (int i = 0; i < 8; i++) {
            new Pawn(true, i, 1, this);
            new Pawn(false, i, 6, this);
        }

        new Rook(true, 0, 0, this);
        new Rook(true, 7, 0, this);
        new Rook(false, 0, 7, this);
        new Rook(false, 7, 7, this);

        new Knight(true, 1, 0, this);
        new Knight(true, 6, 0, this);
        new Knight(false, 1, 7, this);
        new Knight(false, 6, 7, this);

        new Bishop(true, 2, 0, this);
        new Bishop(true, 5, 0, this);
        new Bishop(false, 2, 7, this);
        new Bishop(false, 5, 7, this);

        new Queen(true, 3, 0, this);
        new Queen(false, 3, 7, this);
    }

}