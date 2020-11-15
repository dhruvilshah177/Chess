package controller;

import model.Chessboard;
import model.Player;
import view.Chess_View;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * controller of chess
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Chess_Controller {

     // shows view

    private Chess_View chess_View;

     // main model of the board
     
    private Chessboard chessboard;

     // two players' list
   
    private List<Player> players;

     // if it is white's turn, then it is true; otherwise, false for black's turn
     
    private boolean isWhiteTurn;

    //standard input scanner
    
    private Scanner stdin = null;

   //file input stream
    
    private InputStream inputStream = null;

    //input file scanner
    
    private Scanner file = null;

    /**
     * @param whitePlayer white player
     * @param blackPlayer black player
     */
    public Chess_Controller(Player whitePlayer, Player blackPlayer) {
        chess_View = new Chess_View();
        chessboard = new Chessboard();
        players = new ArrayList<>();
        players.add(whitePlayer);
        players.add(blackPlayer);
        isWhiteTurn = true;
    }
    /**
     * @param filename input file to be used
     */
    
    public void OpenR(String fname) {
    	if(fname != null) {
	        try {
	            inputStream = new FileInputStream(fname);
                file = new Scanner(inputStream);
            }
            catch(IOException e) {	           
	            file = null;	         
	        }
    	}    	
    	if(file == null) {
        	stdin = new Scanner(System.in);
    	}
    }


    /**
     * @return one line from input source (file or standard input)
     */
    
    public String readoneLine() {
        return (file != null) ? file.nextLine() : stdin.nextLine();
    }


    //closes file 
    
    public void CloseR() {
    	if(stdin != null) {
    		stdin.close();
    	}

    	if(file != null) {
    		file.close();
    	}

    	if(inputStream != null) {
    		try {
				inputStream.close();
			}
    		catch (IOException e) {				
			}
    	}
    }


    /**
     * @param parameter input file
     */
    
    public void run(String parameter) {
        chess_View.show(chessboard.getPieceMap());
        System.out.println();

        OpenR(parameter);

        boolean ischeck = false;
        boolean isdrawProposed = false;
        while (true) {
            String input;
        	if(isBatchMode()) {
                input = readoneLine();
                if(ischeck) {
                    System.out.println("Check");
                    System.out.println();
                }
                System.out.println(getCurrentPlayer().getPrompt() + input);
                System.out.println();
        	}
        	else {
                if(ischeck) {
                    System.out.println("Check");
                    System.out.println();
                }
                System.out.print(getCurrentPlayer().getPrompt());
                input = readoneLine();
                System.out.println();
        	}

            Move move = new Move(input);
            if(move.isValid()) {
            	if(move.isDraw()) {
            		if(isdrawProposed) {
            			System.out.println("Draw");
            			break;
            		}
                    System.out.println("Illegal move, please try again");
                    System.out.println();
                }
            	else if(move.isResign()) {
                    System.out.println(!getCurrentPlayer().isWhitePlayer() ? "White wins" : "Black wins");
                    break;
            	}
            	else {
                    Outcome outcome = chessboard.doMove(move, getCurrentPlayer().isWhitePlayer());

                    if(outcome.isOK()) {
                		chess_View.show(chessboard.getPieceMap());
                        System.out.println();

                    	if(outcome.isOpponentCheckmate()) {
                            System.out.println("Checkmate");
                            System.out.println();
                            System.out.println(getCurrentPlayer().isWhitePlayer() ? "White wins" : "Black wins");
                            break;
                    	}
                        if(outcome.isOpponentStalemate()) {
                    	    System.out.println("Stalemate");
                    	    System.out.println();
                            System.out.println("Draw");
                            break;
                        }
                        switchPlayer();

                        isdrawProposed = move.isAskingDraw();

                        ischeck = outcome.isOpponentInCheck();
                    }
                    else {
                        System.out.println("Illegal move, please try again");
                        System.out.println();
                    }
            	}
            }
            else {
                System.out.println("Illegal move, please try again");
                System.out.println();
            }
        }
        //
        CloseR();
    }

    /**
     * switches whose turn it is
     */
    public void switchPlayer() {
    	isWhiteTurn = !isWhiteTurn;
    }

    /**
     * @return current player
     */
    public Player getCurrentPlayer() {
            return players.get(isWhiteTurn ? 0 : 1);
    }

    /**
     * @return if read from file, then true; otherwise, false.
     */
    public boolean isBatchMode() {
        return file != null;
    }
}