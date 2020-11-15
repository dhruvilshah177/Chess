package controller;

import model.Board_Index;

/**
 * this class is for a source and a target in which a piece can move
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Source_Target {

    //source index of piece
    private Board_Index sourceIndex;

    // target index of piece
    private Board_Index targetIndex;

	private Source_Target() {
		sourceIndex = null;
		targetIndex	= null;
	}
    /**
     * @param source piece's source
     * @param target piece's target
     */
	
    public Source_Target(Board_Index source, Board_Index target) {
		sourceIndex = new Board_Index(source);
		targetIndex	= new Board_Index(target);
	}
    /**
     * @return source index
     */
    
    public Board_Index getSource() {
		return sourceIndex;
	}
    /**
     * @return target index
     */
    
    public Board_Index getTarget() {
		return targetIndex;
	}
}