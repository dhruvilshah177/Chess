package model;

/**
 * index of a piece
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Board_Index {

    /**
     * @param fileIndex file
     * @param rankIndex rank
     * @return creates a key for the map of pieces
     */
    public static String getKey(int fileIndex, int rankIndex) {
        return "" + fileIndex + "-" + rankIndex;
    }

   // file(a to h)
    public int fileIndex;

    //rank (1 to 8)
    public int rankIndex;

    /**
     * @param _fileIndex file
     * @param _rankIndex rank
     */
    
    public Board_Index(int _fileIndex, int _rankIndex) {
        fileIndex = _fileIndex;
        rankIndex = _rankIndex;
    }
    public Board_Index(Board_Index input) {
        fileIndex = input.fileIndex;
        rankIndex = input.rankIndex;
    }

    /**
     * @param obj same position for equals operation
     * @return if in same position, then true; otherwise, false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Board_Index)) {
            return false;
        }
        return this.fileIndex == ((Board_Index)obj).fileIndex && this.rankIndex == ((Board_Index)obj).rankIndex;
    }

    /**
     * @return key for map of the pieces
     */
    public String getKey() {
        return getKey(fileIndex, rankIndex);
    }
}