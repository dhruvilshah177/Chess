package controller;

/**
 * The direction and distance of each move.
 *
 * @author Dhruvil Shha
 * @author Drashti Mehta
*/
public class Directional_Distance {

    // all possible directions a piece can move
     
    public enum Direction {
		NONE, N, S, W, E, NW, NE, SW, SE 
	}
    
     // distance between current piece (source) and target piece (target)
     
	private int distance;

    //direction of move
	
	private Direction direction;

    //change in file for step
	
	private int deltaFile;

    //change in rank for step 
	
	private int deltaRank;

    //source file
	
	private int sourceFile;

	//source rank
	
	private int sourceRank;

    //target file 
	
	private int targetFile;

    //target rank
	
	private int targetRank;


    /**
     * @param sourceFile current file
     * @param sourceRank current rank
     * @param targetFile target file
     * @param targetRank target rank
     */
    public Directional_Distance(int sourceFile, int sourceRank, int targetFile, int targetRank) {
		direction = Direction.NONE;
		
		this.sourceFile = sourceFile;
		this.sourceRank	= sourceRank;
		this.targetFile	= targetFile;
		this.targetRank	= targetRank;

        int distance_File = targetFile - sourceFile;
        int distance_Rank = targetRank - sourceRank;
    
        if (distance_File == 0) {
            distance = Math.abs(distance_Rank);
            if (distance_Rank > 0) {
            	direction	= Direction.N;
            	deltaRank	= 1;
            	deltaFile	= 0;
            	
            }
            else {
            	direction	= Direction.S;
            	deltaRank	= -1;
            	deltaFile	= 0;
            	
            }
        }
        else if (distance_Rank == 0) {
        	distance = Math.abs(distance_File);
            if (distance_File > 0) {
            	direction	= Direction.E;
            	deltaRank	= 0;
            	deltaFile	= 1;
            	
            }
            else {
            	direction	= Direction.W;
            	deltaRank	= 0;
            	deltaFile	= -1;
            	
            }
        }
        else if (Math.abs(distance_Rank) == Math.abs(distance_File)) {
        	distance = Math.abs(distance_File);
            if (distance_File > 0 && distance_Rank > 0) {
            	direction	= Direction.NE;
            	deltaRank	= 1;
            	deltaFile	= 1;
            	
            }
            else if (distance_File > 0 && distance_Rank < 0) {
            	direction	= Direction.SE;
            	deltaRank	= -1;
            	deltaFile	= 1;
            	
            }
            else if (distance_File < 0 && distance_Rank > 0) {
            	direction	= Direction.NW;
            	deltaRank	= 1;
            	deltaFile	= -1;
            	
            }
            else {
            	direction	= Direction.SW;
            	deltaRank	= -1;
            	deltaFile	= -1;
            	
            }
        }
	}
    /**
     * @return distance between source and target.
     */
    
    public int getDistance() {
		return distance;
	}
    /**
     * @return change in file for step
     */
    
    public int getDeltaFile() {
		return deltaFile;
    }
    /**
     * @return change in rank for step
     */
    
    public int getDeltaRank() {
		return deltaRank;
    }
    /**
     * @return True if move is valid in every direction, false if move is illegal.
     */
    
    public boolean isValid() {
		return direction != Direction.NONE;
	}
    /**
     * @return if piece moves along the grid, then true; otherwise, false 
     */
    
    public boolean isParallel() {
		return direction == Direction.N || direction == Direction.S || direction == Direction.W || direction == Direction.E;
	}
    /**
     * @return if piece moves diagonally, then true; otherwise, false
     */
    
    public boolean isDiagonal() {
		return direction == Direction.NW || direction == Direction.NE || direction == Direction.SW || direction == Direction.SE;
	}
    /**
     * @return if valid moves are knight, then true; otherwise, false
     */
    
    public boolean isKnightly() {
		return	(targetFile == sourceFile + 1 && targetRank == sourceRank + 2 ) ||
    			(targetFile == sourceFile + 1 && targetRank == sourceRank - 2 ) ||
                (targetFile == sourceFile + 2 && targetRank == sourceRank + 1 ) ||
                (targetFile == sourceFile + 2 && targetRank == sourceRank - 1 ) ||
                (targetFile == sourceFile - 1 && targetRank == sourceRank + 2 ) ||
                (targetFile == sourceFile - 1 && targetRank == sourceRank - 2 ) ||
                (targetFile == sourceFile - 2 && targetRank == sourceRank + 1 ) ||
                (targetFile == sourceFile - 2 && targetRank == sourceRank - 1 );
	}
    /** 
     * @return if piece moves in all directions 1 step, then true; otherwise, false (king)
     */
    
    public boolean isRegal() {
		return isValid() && distance == 1;
	}
    /**
     * @param isWhite color of piece.
     * @return if the King's side castling move is valid, then true; otherwise, false 
     */
    
    public boolean isCastlingKS(boolean isWhite) {       
		if (isWhite) {
			return sourceRank == 0 && distance == 2 && direction == Direction.E;
		}
		return sourceRank == 7 && distance == 2 && direction == Direction.E;
	}
    /**
     * @param isWhite color of piece
     * @return if the Queen's side castling move is valid, then true; otherwise, false
     */
    
    public boolean isCastlingQS(boolean isWhite) {
		if (isWhite) {
			return sourceRank == 0 && distance == 2 && direction == Direction.W;
		}
		return sourceRank == 7 && distance == 2 && direction == Direction.W;
	}
    /**
     * @param isWhite color of piece
     * @return if pawn moves one step, then true; otherwise, false
     */
    
    public boolean isPawnOneStep(boolean isWhite) {
		if (isWhite) {
			return direction == Direction.N && distance == 1;
		}
		return direction == Direction.S && distance == 1;
	}
    /**
     * @param isWhite color of piece
     * @return if pawn moves 2 steps, then true; otherwise, false
     */
    
    public boolean isPawnTwoStep(boolean isWhite) {
		if (isWhite) {
			return direction == Direction.N && distance == 2 && sourceRank == 1;
		}
		return direction == Direction.S && distance == 2 && sourceRank == 6;
	}
    /**
     * @param isWhite color of piece
     * @return if pawn is killed, then true; otherwise, false
     */
    
    public boolean isPawnKill(boolean isWhite) {
		if (isWhite) {
			return distance == 1 && (direction == Direction.NW || direction == Direction.NE);
		}
		return distance == 1 && (direction == Direction.SW || direction == Direction.SE);
	}
    /**
     * @param isWhite color of piece
     * @return if pawn is promoted, then true; otherwise, false
     */
    
    public boolean isPawnPromotion(boolean isWhite) {
		return isWhite ? targetRank == 7 : targetRank == 0;
	}
}