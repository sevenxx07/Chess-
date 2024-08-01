package chessclient.gui;

import chessclient.players.GUIPlayer;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.InputStream;

/**
 * Choose promotion stage provides the opportunity for player to choose
 * how he would like to promote the pawn
 */

public class ChoosePromotionStage {

    private final GUIPlayer player;

    public ChoosePromotionStage(GUIPlayer Player) {
        this.player = Player;
    }

    /**
     * Generating whole scene for choosing the promotion, after choice the player
     * will be send back to chess scene
     * Without the choice, stage can't be closed
     */
    public void choosePromotion() {
        GridPane figures = new GridPane();
        figures.setPadding(new Insets(10));
        figures.setHgap(5);
        Image queenIm, knightIm, bishopIm, rookIm;

        if (player.getColor() == cz.cvut.fel.pjv.chessclient.pieces.Color.BLACK) {
            queenIm = new Image("/images/bqueen.png");
            knightIm = new Image("/images/bknight.png");
            bishopIm = new Image("/images/bbishop.png");
            rookIm = new Image("/images/brook.png");
        } else {
            queenIm = new Image("/images/wqueen.png");
            knightIm = new Image("/images/wknight.png");
            bishopIm = new Image("/images/wbishop.png");
            rookIm = new Image("/images/wrook.png");
        }

        ImageView queen = new ImageView(queenIm);
        ImageView knight = new ImageView(knightIm);
        ImageView bishop = new ImageView(bishopIm);
        ImageView rook = new ImageView(rookIm);

        figures.add(queen, 0, 0);
        figures.add(knight, 1, 0);
        figures.add(bishop, 2, 0);
        figures.add(rook, 3, 0);

        Stage choosePromotion = new Stage();

        choosePromotion.setOnCloseRequest(Event::consume);
        queen.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            Stage choosePromotion1 = (Stage) queen.getScene().getWindow();
            choosePromotion1.close();
            player.setPromotionIdentifier("queen");
        });

        queen.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> queen.setOpacity(0.5));
        queen.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> queen.setOpacity(1));

        knight.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            Stage choosePromotion12 = (Stage) knight.getScene().getWindow();
            choosePromotion12.close();
            player.setPromotionIdentifier("knight");
        });
        knight.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> knight.setOpacity(0.5));
        knight.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> knight.setOpacity(1));

        bishop.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            Stage choosePromotion13 = (Stage) bishop.getScene().getWindow();
            choosePromotion13.close();
            player.setPromotionIdentifier("bishop");
        });
        bishop.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> bishop.setOpacity(0.5));
        bishop.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> bishop.setOpacity(1));

        rook.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            Stage choosePromotion14 = (Stage) rook.getScene().getWindow();
            choosePromotion14.close();
            player.setPromotionIdentifier("rook");
        });
        rook.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> rook.setOpacity(0.5));
        rook.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> rook.setOpacity(1));

        Scene promotion = new Scene(figures, 275, 80);

        InputStream imageInputStream = ChessApp.class.getResourceAsStream("/images/chessicon.png");
        Image image = new Image(imageInputStream);
        choosePromotion.getIcons().add(image);
        choosePromotion.setTitle("Choose promotion");
        choosePromotion.setScene(promotion);
        choosePromotion.show();
    }
}
