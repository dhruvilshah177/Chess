package view;

import model.Board_Index;
import model.Pieces;

import java.util.Map;

/**
 * view for chess
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Chess_View {
	
    private final static String SPACE = " ";

    /**
     * @param piece_Map input map of pieces to output
     */
    
    public void show(Map<String, Pieces> piece_Map) {
        for (int rank = 7; rank >= 0; rank--) {
            for (int file = 0; file < 8; file++) {
                Pieces onePiece = piece_Map.get(Board_Index.getKey(file, rank));
                if (onePiece == null) {
                    int sum = file + rank;
                    System.out.print((sum % 2 == 0) ? ("##" + SPACE) : ("  " + SPACE));
                }
                else {
                    System.out.print(onePiece + SPACE);
                }
            }            
            System.out.println(rank + 1);
        }
        System.out.println(" a  b  c  d  e  f  g  h");
    }
}

