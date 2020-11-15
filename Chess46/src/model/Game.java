package model;

import controller.Chess_Controller;

/**
 * model for the game
 *
 * @author Dhruvil Shah
 * @author Drashti Mehta
 */
public class Game {

    /**
     * white player
     */
    Player whitePlayer;

    /**
     * black player
     */
    Player blackPlayer;

    /**
     * controller for board.
     */
    Chess_Controller controller;

    public Game() {
        whitePlayer = new Player(true);
        blackPlayer = new Player(false);
        controller = new Chess_Controller(whitePlayer, blackPlayer);
    }

    /**
     * @param parameter input file
     */
    public void playGame(String parameter) {
        controller.run(parameter);
    }
}