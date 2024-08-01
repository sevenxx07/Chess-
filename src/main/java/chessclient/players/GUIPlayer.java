package chessclient.players;

import chessclient.gamelogic.ChessClock;
import chessclient.gamelogic.Move;
import chessclient.gamelogic.SpecialMove;
import chessclient.network.Turn;
import chessclient.pieces.Color;

/**
 * GUIPlayer represents a player who's playing locally through GUI.
 * GUIPlayer is controlled through the Player interface, but its method
 * setTurn, and setPromotionIdentifier need to be called directly from the
 * GUI once the player plays a turn.
 */
public class GUIPlayer implements Player {

    private Turn turn;
    private ChessClock clock;
    private final Color color;

    /**
     * Assigns the color to the GUIPlayer.
     *
     * @param color player's color
     */
    public GUIPlayer(Color color) {
        this.color = color;
    }

    @Override
    public void play(Turn opponentTurn) {
        clock.update();
        this.turn = null;
    }

    /**
     * Method to be called directly from the GUI, once the player chooses which
     * piece should the pawn promote into once the player plays a promotion
     * special move.
     *
     * @param identifier identifier of the piece the pawn shall be promoted into
     */
    public void setPromotionIdentifier(String identifier) {
        Move m = this.turn.getMove();
        m = new Move(m.getFrom(), m.getTo(), m.getSpecialMove());
        m.setPromotionIdentifier(identifier);
        ChessClock curr = new ChessClock();
        curr.synchronize(clock);
        curr.pause();
        this.turn = new Turn(m, curr);
    }

    /**
     * Creates a Turn based on the Move provided to the GUIPlayer from the GUI.
     * This method should be called directly from the GUI after the player plays a
     * move.
     *
     * @param m player's move
     */
    public void setTurn(Move m) {
        if (m.getSpecialMove() == null || !m.getSpecialMove().equals(SpecialMove.PROMOTION)) {
            ChessClock curr = new ChessClock();
            curr.synchronize(clock);
            curr.pause();
            this.turn = new Turn(m, curr);
        } else {
            this.turn = new Turn(m, clock);
        }
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
