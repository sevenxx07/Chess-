package chessclient.gamelogic;

import chessclient.gui.ChessSceneGenerator;
import chessclient.network.Turn;
import chessclient.players.GUIPlayer;
import chessclient.players.NetworkPlayer;
import chessclient.players.Player;
import chessclient.utilities.GameSave;
import chessclient.utilities.SaveManager;
import javafx.beans.property.StringProperty;

/**
 * Class game connects together components of this application, and
 * manages communication between them. It takes care of communication between
 * players, updating timers, updating GUI, updating the game board by playing
 * players' moves, and saving the current state of the game. It runs asynchronously.
 */
public class Game extends Thread {
    public static final long DEFAULT_TIMER = 5400000;

    private StringProperty whiteClockStr;
    private StringProperty blackClockStr;
    private ChessClock whiteClock;
    private ChessClock blackClock;

    private final Player blackPlayer;
    private final Player whitePlayer;
    private final Board board;
    private final ChessSceneGenerator sceneGenerator;
    private boolean stop;
    private boolean whiteTurn;

    /**
     * Default constructor for the Game class.
     *
     * @param whitePlayer    white player
     * @param blackPlayer    black player
     * @param sceneGenerator ChessSceneGenerator functions of which are called when the board changes
     * @param b              Board, initial state of the chess board
     * @param whiteStarts    boolean which determines which player starts the game
     */
    public Game(Player whitePlayer, Player blackPlayer, ChessSceneGenerator sceneGenerator, Board b, boolean whiteStarts) {
        this.board = b;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.setClocks(new ChessClock(DEFAULT_TIMER), new ChessClock(DEFAULT_TIMER));
        this.sceneGenerator = sceneGenerator;
        this.whiteTurn = whiteStarts;
        this.stop = false;
    }

    /**
     * Starts the game - updating timers, listening for player Moves, and reacting to them.
     */
    @Override
    public void run() {
        long lastWhiteUpdate = whiteClock.getRemainingTime();
        long lastBlackUpdate = blackClock.getRemainingTime();

        if (!whiteTurn) {
            blackPlayer.play(null);
        }

        while (!stop) {
            if (whiteTurn) {
                if (whitePlayer.hasTurn()) {
                    this.playTurn(whitePlayer, blackPlayer, whiteClock, whiteClockStr);
                    //redraw board & inform other player
                    whiteTurn = false;
                } else {
                    lastWhiteUpdate = this.updateClock(whiteClockStr, whiteClock, lastWhiteUpdate);
                    if (whiteClock.getRemainingTime() <= 0) {
                        sceneGenerator.endGame(blackPlayer);
                        if (blackPlayer instanceof NetworkPlayer) {
                            ((NetworkPlayer) blackPlayer).endGameOnServer("lose");
                        }
                        this.stop = true;
                    }
                }
            } else {
                if (blackPlayer.hasTurn()) {
                    this.playTurn(blackPlayer, whitePlayer, blackClock, blackClockStr);
                    //redraw board & inform other player
                    whiteTurn = true;
                } else {
                    lastBlackUpdate = this.updateClock(blackClockStr, blackClock, lastBlackUpdate);
                    if (blackClock.getRemainingTime() <= 0) {
                        sceneGenerator.endGame(whitePlayer);
                        if (whitePlayer instanceof NetworkPlayer) {
                            ((NetworkPlayer) whitePlayer).endGameOnServer("lose");
                        }
                        this.stop = true;
                    }
                }
            }
        }
    }

    /* Plays the players turns, stops his ChessClock, and checks for end of the game, notifies
     * ChessSceneGenerator to update the board scene */
    private void playTurn(Player player, Player enemy, ChessClock clock, StringProperty clockStr) {
        Turn t = player.getTurn();
        Move m = t.getMove();
        if (m == null) {
            sceneGenerator.drawNetworkCrashed();
            return;
        } else if (m.getSpecialMove() != null && m.getSpecialMove().equals(SpecialMove.PROMOTION)
                && m.getPromotionIdentifier() == null) {
            sceneGenerator.choosePromotion((GUIPlayer) player);
            while (!stop && m.getPromotionIdentifier() == null) {
                this.waitForMillis(50);
                t = player.getTurn();
                m = t.getMove();
            }
        }
        board.playMove(m);
        //synchronize clock
        clock.synchronize(t.getClock());
        String clockId = whiteTurn ? "White: " : "Black: ";
        clockStr.setValue(clockId + clock.toString());
        //redraw changes in GUI
        sceneGenerator.redrawBoard(t.getMove());
        //detect end of game
        if (Ruleset.detectCheckMate(enemy.getColor(), board)) {
            if (player instanceof NetworkPlayer) {
                ((NetworkPlayer) player).endGameOnServer("lose");
            }
            sceneGenerator.endGame(player);
            this.stop = true;
        } else if (Ruleset.detectDraw(enemy.getColor(), board)) {
            if (player instanceof NetworkPlayer) {
                ((NetworkPlayer) player).endGameOnServer("draw");
            }
            sceneGenerator.endGame(null);
            this.stop = true;
        }
        //prompt the other player to play a turn
        enemy.play(t);
    }

    /* Wait for given number of milliseconds */
    private void waitForMillis(long millis) {
        long start = System.currentTimeMillis();
        long curr;
        while ((curr = System.currentTimeMillis()) < start + millis) ;
    }

    /**
     * Terminates the game thread
     */
    public void stopGame() {
        this.stop = true;
    }

    /* Updates the ChessClock and appropriate StringProperty by the amount of time that
     * has passed since last update */
    private long updateClock(StringProperty property, ChessClock clock, long lastUpdate) {
        clock.update();
        long updated = clock.getRemainingTime();
        if (lastUpdate - updated > 200) {
            String clockId = whiteTurn ? "White: " : "Black: ";
            property.setValue(clockId + clock.toString());
            return updated;
        }
        return lastUpdate;
    }


    /**
     * Saves the current state of the game, so that it can be later loaded, and continued.
     */
    public void saveGame() {
        SaveManager saveManager = new SaveManager();
        GameSave save;
        if (whiteTurn) {
            ChessClock whiteClock = new ChessClock();
            whiteClock.synchronize(this.whiteClock);
            whiteClock.pause();
            save = new GameSave(whiteClock, blackClock, this.board, "white");
        } else {
            ChessClock blackClock = new ChessClock();
            blackClock.synchronize(this.blackClock);
            blackClock.pause();
            save = new GameSave(whiteClock, blackClock, this.board, "black");
        }
        saveManager.saveGame(save);
    }

    /**
     * Sets both the StringProperties into which the remaining time of the two players is written.
     *
     * @param whiteClockStr StringProperty wherein the white player's remaining time is stored
     * @param blackClockStr StringProperty wherein the black player's remaining time is stored
     */
    public void setClockStringProperties(StringProperty whiteClockStr, StringProperty blackClockStr) {
        this.whiteClockStr = whiteClockStr;
        this.blackClockStr = blackClockStr;
        this.whiteClockStr.setValue("White: " + this.whiteClock.toString());
        this.blackClockStr.setValue("Black: " + this.blackClock.toString());
    }

    /**
     * Sets both ChessClocks to given ChessClocks, used when loading the game from a save.
     *
     * @param whiteClock white player's ChessClock
     * @param blackClock black player's ChessClock
     */
    public void setClocks(ChessClock whiteClock, ChessClock blackClock) {
        this.whiteClock = whiteClock;
        this.blackClock = blackClock;
        this.whitePlayer.setChessClock(this.whiteClock);
        this.blackPlayer.setChessClock(this.blackClock);
    }
}
