package cz.cvut.fel.pjv.chessclient.gamelogic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Keeps track of the time the player has left until he loses the game.
 */
public class ChessClock {

    private long remainingTime;     //remaining time in milliseconds
    private long lastUpdate;     //timestamp of when the clock was last updated

    /**
     * Constructor to be used by the Jackson library. You shouldn't initialize
     * ChessClock manually via this constructor.
     */
    public ChessClock() {

    }

    /**
     * Creates a new ChessClock and sets the remaining time of the player to the
     * given number of milliseconds.
     *
     * @param startTime remaining time of the player in milliseconds
     */
    public ChessClock(long startTime) {
        this.remainingTime = startTime;
        this.lastUpdate = -1;
    }

    /**
     * Sets this ChessClock's time and lastUpdate attributes to another ChessClock's
     * attributes.
     *
     * @param clock ChessClock which attributes should be copied into this ChessClock
     */
    public void synchronize(ChessClock clock) {
        this.remainingTime = clock.remainingTime;
        this.lastUpdate = clock.lastUpdate;
    }

    /**
     * Unpause the ChessClock if it's been paused, or subtract the remaining time
     * by the amount of time which has passed since the last update.
     */
    public void update() {
        long now = System.currentTimeMillis();
        if (lastUpdate != -1) {
            this.remainingTime = Math.max(this.remainingTime - (now - lastUpdate), 0);
        }

        this.lastUpdate = now;
    }

    /**
     * Sets the lastUpdate attribute of the ChessClock to an invalid value, thus
     * stopping the ChessClock.
     */
    public void pause() {
        this.update();
        this.lastUpdate = -1;
    }

    /**
     * Formats the remaining time in milliseconds into a HH:mm:ss String.
     *
     * @return String with the formatted remaining time
     */
    public String toString() {
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(remainingTime);
    }

    /**
     * Returns remaining time of the player in milliseconds.
     *
     * @return remaining time in milliseconds
     */
    public long getRemainingTime() {
        return remainingTime;
    }
}
