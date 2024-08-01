package chessclient.gui;

import chessclient.network.ChessClient;
import javafx.beans.property.StringProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * QueueUpdater class updates periodically the timer which shows to a Player waiting in
 * queue for a chess match, it also checks for when the match starts, and alerts the
 * QueueSceneGenerator.
 */
public class QueueUpdater extends Thread {

    private static final Logger logger = Logger.getLogger(QueueUpdater.class.getName());
    private final StringProperty timeInQueue;
    private final ChessClient connection;
    private final QueueSceneGenerator sceneGenerator;
    private long startTime;
    private boolean stopUpdater;

    /**
     * Default constructor for QueueUpdater.
     *
     * @param connection     ChessClient connecting the user to server
     * @param timeInQueue    StringProperty into which the waiting time is saved
     * @param sceneGenerator QueueSceneGenerator which has to redraw the scene once game is found
     */
    public QueueUpdater(ChessClient connection, StringProperty timeInQueue, QueueSceneGenerator sceneGenerator) {
        this.timeInQueue = timeInQueue;
        this.connection = connection;
        this.sceneGenerator = sceneGenerator;
        this.stopUpdater = false;
    }

    /**
     * Starts a new thread which periodically updates the timer shown to user waiting in queue
     * for a chess match.
     */
    @Override
    public void run() {
        this.startTime = System.currentTimeMillis();
        long lastUpdate = startTime;
        //Non blocking read of ObjectInputStream
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        ExecutorService service = Executors.newCachedThreadPool();
        service.submit(() -> {
            completableFuture.complete(connection.receiveString());
        });

        while (!completableFuture.isDone() && !stopUpdater) {
            lastUpdate = this.updateQueueTime(lastUpdate);
        }

        completableFuture.complete("null");

        try {
            if (!stopUpdater) {
                sceneGenerator.startGame(completableFuture.get());
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Couldn't load player's color", e);
        }
    }

    /* Updates the Text which shows the timer by the time that has passed */
    private long updateQueueTime(long lastUpdate) {
        long currTime = System.currentTimeMillis();
        if (currTime - lastUpdate > 200) {
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            timeInQueue.setValue("Time spent in queue: " + formatter.format(currTime - this.startTime));
            return currTime;
        }
        return lastUpdate;
    }

    /**
     * Stops the thread.
     */
    public void stopUpdater() {
        this.stopUpdater = true;
    }

}
