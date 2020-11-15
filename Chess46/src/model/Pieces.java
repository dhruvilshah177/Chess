package model;

import controller.Directional_Distance;
import controller.Last_Move_Rollback;
import controller.Move.Promotion;
import controller.Outcome;
import controller.Source_Target;

import java.util.Map;

/**
 * Parent class for all the pieces
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public abstract class Pieces {

    //position of piece
    private Board_Index boardIndex = null;

   // color of piece
    private boolean isWhite = false;

   
    //checks if piece is previously moved. Used for castling
    private boolean isMoved = false;

    // chessboard the piece belongs to
    protected Chessboard chessBoard = null;
    /**
     * @param _isWhite color of piece
     * @param file file of piece
     * @param rank rank of piece
     * @param cb board
     */
    
    protected Pieces(boolean _isWhite, int file, int rank, Chessboard cb) {
        boardIndex = new Board_Index(file, rank);
        isWhite = _isWhite;
        chessBoard = cb;
    }

    /**
     * @param _isWhite color of piece
     * @param fileRank input file and rank
     * @param cb board
     */
    protected Pieces(boolean _isWhite, String fileRank, Chessboard cb) {
        char charFile = fileRank.toUpperCase().trim().charAt(0);
        char charRank = fileRank.toUpperCase().trim().charAt(1);
        boardIndex = new Board_Index(charFile - 'A', charRank - '1');
        isWhite = _isWhite;
        chessBoard = cb;
    }

    /**
     * @return if piece is previously moved, then true; otherwise, false
     */
    
    public boolean isMoved() {
        return isMoved;
    }

    /**
     * @return color of piece
     */
    
    public boolean isWhite() {
        return isWhite;
    }

    /**
     * @return position of piece
     */
    
    public Board_Index getBoardIndex() {
        return boardIndex;
    }

    /**
     * @param input position of piece
     */
    
    protected void setBoardIndex(Board_Index input) {
        boardIndex = input;
    }

    /**
     * @param targetIndex target position of a move
     * @param promotion Promotion used for Pawn only
     * @return outcome of the move
     */
    
    abstract public Outcome doMove(Board_Index targetIndex, Promotion promotion);

    /**
     * @param targetIndex target position of a move
     * @return if this piece can attack target, then true; otherwise, false
     */
    
    abstract public Outcome doAttack(Board_Index targetIndex);

    /**
     * @return if piece has legal move, then true; otherwise, false
     */
    
    abstract public boolean hasLegalMove();

    /**
     * @return if piece has legal move, then true; otherwise, false
     */
    
    abstract public Source_Target getOneLegalMove();

    /**
     * @param targetIndex target position of a move
     * @param rollback if true, rollback after checking move; otherwise, false. Used for checking checkmate and stalemate
     * @return if piece can move, then true; otherwise, false if move is illegal (as King is in check after move)
     */
    
    protected boolean doActualMove(Board_Index targetIndex, boolean rollback) {
        Map<String, Pieces> pieceMap = chessBoard.getPieceMap();
        Board_Index sourceIndex = getBoardIndex();
        Pieces target = pieceMap.get(targetIndex.getKey());
        pieceMap.remove(getKey());
        setBoardIndex(targetIndex);
        pieceMap.put(getKey(), this);

        Pieces king = isWhite() ? chessBoard.getWhiteKing() : chessBoard.getBlackKing();
        boolean isKingUnderAttack = chessBoard.isUnderAttack(isWhite(), king.getBoardIndex());

        if (isKingUnderAttack || rollback) {
            pieceMap.remove(getKey());
            setBoardIndex(sourceIndex);
            pieceMap.put(getKey(), this);

            if (target != null) {
                pieceMap.put(target.getKey(), target);
            }
        } else {
            Last_Move_Rollback lastMove = chessBoard.getLastMoveForRollback();
            lastMove.doInit(false);

            if (this instanceof Pawn) {
                int dFile = targetIndex.fileIndex - sourceIndex.fileIndex;
                int dRank = targetIndex.rankIndex - sourceIndex.rankIndex;
                if ((dFile == 0) && Math.abs(dRank) == 2) {
                    lastMove.lastTwoStepPawnMove = this;
                } else {
                    lastMove.lastTwoStepPawnMove = null;
                }
            } else {
                lastMove.lastTwoStepPawnMove = null;
            }

            lastMove.isRegularMove = true;
            lastMove.sourceIndex = new Board_Index(sourceIndex);
            lastMove.targetIndex = new Board_Index(targetIndex);
            lastMove.isMove = isMoved;                       
            lastMove.removedRegular = target;

            if (!isMoved) {
                isMoved = true;
            }
        }
        return !isKingUnderAttack;
    }

    /**
     * @param targetIndex target position of a move
     * @param rookSourceIndex current position of rook
     * @param rookTargetIndex target position of rook
     * @return if King and Rook can castle,then true; otherwise, false. Always return true as King will not be under attack when castling begins.
     */
    
    protected boolean doActualMoveCastling(Board_Index targetIndex, Board_Index rookSourceIndex, Board_Index rookTargetIndex) {
        Map<String, Pieces> pieceMap = chessBoard.getPieceMap();
        Board_Index sourceIndex = getBoardIndex();

        pieceMap.remove(getKey());
        setBoardIndex(targetIndex);
        pieceMap.put(getKey(), this);

        Pieces rook = pieceMap.remove(rookSourceIndex.getKey());
        rook.setBoardIndex(rookTargetIndex);
        pieceMap.put(rook.getKey(), rook);
        
        Last_Move_Rollback lastMove = chessBoard.getLastMoveForRollback();
        lastMove.doInit(false);

        lastMove.isCastling = true;

        lastMove.sourceIndex = new Board_Index(sourceIndex);
        lastMove.targetIndex = new Board_Index(targetIndex);
        lastMove.sourceIndexRook = new Board_Index(rookSourceIndex);
        lastMove.targetIndexRook = new Board_Index(rookTargetIndex);
        
        return true;
    }

    /**
     * @param targetIndex target position of a move
     * @param promotion type of Promotion for pawn
     * @param rollback if rollback is needed, then true; otherwise, false
     * @return if promotion is successful, then true; otherwise, false
     */
    
    protected boolean doActualMoveIsPromotion(Board_Index targetIndex, Promotion promotion, boolean rollback) {
        Map<String, Pieces> pieceMap = chessBoard.getPieceMap();
        Board_Index sourceIndex = getBoardIndex();

        Pieces target = pieceMap.get(targetIndex.getKey());
        pieceMap.remove(getKey());
        
        if (promotion == Promotion.Bishop) {
            new Bishop(isWhite(), targetIndex.fileIndex, targetIndex.rankIndex, chessBoard);
        } else if (promotion == Promotion.Rook) {
            new Rook(isWhite(), targetIndex.fileIndex, targetIndex.rankIndex, chessBoard);
        } else if (promotion == Promotion.Knight) {
            new Knight(isWhite(), targetIndex.fileIndex, targetIndex.rankIndex, chessBoard);
        } else {
            new Queen(isWhite(), targetIndex.fileIndex, targetIndex.rankIndex, chessBoard);
        }
        
        Pieces king = isWhite() ? chessBoard.getWhiteKing() : chessBoard.getBlackKing();
        boolean isKingUnderAttack = chessBoard.isUnderAttack(isWhite(), king.getBoardIndex());
        
        if (isKingUnderAttack || rollback) {
            pieceMap.put(getKey(), this);        
            if (target != null) {
                pieceMap.put(target.getKey(), target);
            } else {
                pieceMap.remove(targetIndex.getKey());
            }
        } else {
        	Last_Move_Rollback lastMove = chessBoard.getLastMoveForRollback();
            lastMove.doInit(false);
            lastMove.isPromotion = true;
            lastMove.sourceIndex = new Board_Index(sourceIndex);
            lastMove.targetIndex = new Board_Index(targetIndex);
            lastMove.removedRegular = target;
            lastMove.removedIsPromotion = this;

            if (!isMoved) {
                isMoved = true;
            }
        }
        return !isKingUnderAttack;
    }

    /**
     * @param chessBoard input chessboard
     * @return if rollback can happen, then true; otherwise, false
     */
    
    public static boolean doRollback(Chessboard chessBoard) {
    	Last_Move_Rollback lastMove = chessBoard.getLastMoveForRollback();
        Map<String, Pieces> pieceMap = chessBoard.getPieceMap();
        if (lastMove.isRegularMove) {
            Pieces target = pieceMap.get(lastMove.targetIndex.getKey());
            pieceMap.remove(target.getKey());
            target.setBoardIndex(lastMove.sourceIndex);
            pieceMap.put(target.getKey(), target);
            target.isMoved = lastMove.isMove;

            if (lastMove.removedRegular != null) {
                pieceMap.put(lastMove.removedRegular.getKey(), lastMove.removedRegular);
            }
            lastMove.doInit(true);

            return true;
        }
        if (lastMove.isCastling) {
            Pieces target = pieceMap.get(lastMove.targetIndex.getKey());
            pieceMap.remove(target.getKey());
            target.setBoardIndex(lastMove.sourceIndex);
            pieceMap.put(target.getKey(), target);

            Pieces targetRook = pieceMap.get(lastMove.targetIndexRook.getKey());
            pieceMap.remove(targetRook.getKey());
            targetRook.setBoardIndex(lastMove.sourceIndexRook);
            pieceMap.put(targetRook.getKey(), targetRook);
            lastMove.doInit(true);

            return true;
        }        
        if (lastMove.isPromotion) {
            Pieces target = pieceMap.get(lastMove.targetIndex.getKey());
            pieceMap.remove(target.getKey());
            pieceMap.put(lastMove.removedIsPromotion.getKey(), lastMove.removedIsPromotion);

            if (lastMove.removedRegular != null) {
                pieceMap.put(lastMove.removedRegular.getKey(), lastMove.removedRegular);
            }

            lastMove.doInit(true);
            
            return true;
        }
        return false;
    }

    /**
     * @return board output for pieces
     */
    
    @Override
    public String toString() {
        return isWhite ? "w" : "b";
    }

    /**
     * @return key for the map of pieces
     */
    
    public String getKey() {
        return "" + boardIndex.fileIndex + "-" + boardIndex.rankIndex;
    }

    /**
     * @param targetIndex target position of a move
     * @return if target is empty or an opponent piece, then true; otherwise, false
     */
    public boolean isTargetEmptyOrLegal(Board_Index targetIndex) {
        Pieces targetPiece = chessBoard.getPieceMap().get(targetIndex.getKey());        
        return (targetPiece == null) || (isWhite() == !targetPiece.isWhite());
    }

    /**
     * @param targetIndex target position of a move
     * @return if target is empty, then true; otherwise, false
     */
    
    public boolean isTargetEmpty(Board_Index targetIndex) {
        Pieces targetPiece = chessBoard.getPieceMap().get(targetIndex.getKey());
        return targetPiece == null;
    }

    /**
     * @param targetIndex target position of a move
     * @return if target is an opponent piece, then true; otherwise, false
     */
    
    public boolean isTargetLegal(Board_Index targetIndex) {
        Pieces targetPiece = chessBoard.getPieceMap().get(targetIndex.getKey());        
        return (targetPiece != null) && (isWhite() == !targetPiece.isWhite());
    }

    /**
     * @param dd direction, distance
     * @return if no pieces are between this piece and target, then true; otherwise, false
     */
    
    protected boolean isNoneInBetween(Directional_Distance dd) {
        Map<String, Pieces> pieceMap = chessBoard.getPieceMap();
        boolean out = true;

        for (int i = 1; i < dd.getDistance(); i++) {
            int deltaFile = dd.getDeltaFile();
            int deltaRank = dd.getDeltaRank();
            Board_Index oneIndex = new Board_Index(boardIndex.fileIndex + i * deltaFile, boardIndex.rankIndex + i * deltaRank);
            Pieces onePiece = pieceMap.get(oneIndex.getKey());
            if (onePiece != null) {
                out = false;
                break;
            }
        }

        return out;
    }

    /**
     * @param deltaFile change in file for step
     * @param deltaRank change in rank for step
     * @return if there is at least 1 legal move along 1 direction, then true; otherwise, false
     */
    
    protected Source_Target hasLegalMoveInOneDirection(int deltaFile, int deltaRank) {
        Board_Index sourceIndex = getBoardIndex();
        Map<String, Pieces> pieceMap = chessBoard.getPieceMap();
        Source_Target legalMove = null;

        for (int file = sourceIndex.fileIndex + deltaFile, rank = sourceIndex.rankIndex + deltaRank; (file >= 0 && file < 8 && rank >= 0 && rank < 8); file = file + deltaFile, rank = rank + deltaRank) {
            Board_Index targetIndex = new Board_Index(file, rank);

            boolean isLegalMove;
            Pieces targetPiece = pieceMap.get(targetIndex.getKey());
            if (targetPiece != null && targetPiece.isWhite() == isWhite()) {          
                break;
            } else if ((targetPiece != null && targetPiece.isWhite() != isWhite())) { 
                isLegalMove = doActualMove(targetIndex, true);
                if (!isLegalMove) {
                    break;
                }
            } else {
                isLegalMove = doActualMove(targetIndex, true);
            }

            if (isLegalMove) {
                legalMove = new Source_Target(sourceIndex, targetIndex);
                break;
            }
        }

        return legalMove;
    }

    /**
     * @param deltaFile change in file for step
     * @param deltaRank change in rank for step
     * @return if is legal for 1 step in 1 direction, then true; otherwise, false
     */
    
    protected Source_Target hasLegalMoveOneDirectionOneStep(int deltaFile, int deltaRank) {
        Board_Index sourceIndex = getBoardIndex();
        Map<String, Pieces> pieceMap = chessBoard.getPieceMap();
        Source_Target legalMove = null;

        for (int file = sourceIndex.fileIndex + deltaFile, rank = sourceIndex.rankIndex + deltaRank; (file >= 0 && file < 8 && rank >= 0 && rank < 8); file = 100, rank = -100) {
            Board_Index targetIndex = new Board_Index(file, rank);
            Pieces targetPiece = pieceMap.get(targetIndex.getKey());
            if (targetPiece != null && targetPiece.isWhite() == isWhite()) {
                break;
            }
            boolean isLegalMove = doActualMove(targetIndex, true);

            if (isLegalMove) {
                legalMove = new Source_Target(sourceIndex, targetIndex);
                break;
            }
        }

        return legalMove;
    }
}