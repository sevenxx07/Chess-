package chessclient.players;

import chessclient.gamelogic.*;
import chessclient.network.Turn;
import chessclient.pieces.Color;

import java.util.List;
import java.util.Random;

/**
 * RandomAI class represents a simple artificial intelligence, which plays
 * random valid turns according to the rules of chess. If the AI should choose
 * a pawn promotion it always chooses queen. RandomAI is controlled exclusively
 * via the Player interface.
 */
public class RandomAI implements Player {

    private final Board board;
    private final Color color;
    private ChessClock clock;
    private Turn turn;

    /**
     * Creates a new RandomAI player with the given board and color.
     *
     * @param board the game board
     * @param color player's color
     */
    public RandomAI(Board board, Color color) {
        this.board = board;
        this.color = color;
        this.clock = null;
        this.turn = null;
    }

    @Override
    public void play(Turn opponentTurn) {
        this.clock.update();
        Random rnd = new Random();
        List<Move> validMoves = Ruleset.getAllValidMoves(this.color, this.board);
        Move m;
        //Always choose a random valid move, and if it's a promotion, promote the Pawn into a Queen
        if (!validMoves.isEmpty()) {
            m = validMoves.get(rnd.nextInt(validMoves.size()));
            if (m.getSpecialMove() != null && m.getSpecialMove().equals(SpecialMove.PROMOTION)) {
                m.setPromotionIdentifier("queen");
            }
        } else {
            m = null;
        }
        this.clock.pause();
        this.turn = new Turn(m, this.clock);
    }

    @Override
    public boolean hasTurn() {
        return this.turn != null;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public Turn getTurn() {
        return this.turn;
    }

    @Override
    public void setChessClock(ChessClock clock) {
        this.clock = clock;
    }

}
