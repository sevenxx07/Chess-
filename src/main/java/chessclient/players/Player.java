package chessclient.players;

import chessclient.gamelogic.ChessClock;
import chessclient.network.Turn;
import chessclient.pieces.Color;

/**
 * Interface Player defines the methods through which the Game class communicates
 * with the various Player implementations.
 */
public interface Player {

    /**
     * Prompts the player to play his turn, and provides him with the turn his opponent
     * has made.
     *
     * @param turn opponent's last Turn
     */
    public void play(Turn turn);

    /**
     * Asks the player whether he's already come up with a Turn, or whether he's
     * still playing.
     *
     * @return true if the player has made a turn, else false
     */
    public boolean hasTurn();

    /**
     * Returns the color of the player's chess pieces
     *
     * @return color of the player's pieces
     */
    public Color getColor();

    /**
     * Returns the Turn a player has made.
     *
     * @return the player's last Turn
     */
    public Turn getTurn();

    /**
     * Sets the player's ChessClock to the given ChessClock object. The ChessClock is used
     * for tracking the remaining time of the player. Should be done after initialization.
     *
     * @param clock ChessClock to which the player's internal ChessClock should be set
     */
    public void setChessClock(ChessClock clock);
}
