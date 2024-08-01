package cz.cvut.fel.pjv.chessclient.gui;

import cz.cvut.fel.pjv.chessclient.gamelogic.Board;
import cz.cvut.fel.pjv.chessclient.pieces.Color;
import cz.cvut.fel.pjv.chessclient.pieces.Piece;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *  BoardFiller fills 2D image array with pieces for GUI board according to
 *  board that is initialized manually (in simulation) or automatically for
 *  the game and load it to image views
 */

public class BoardFiller {
    private final Board board;

    public BoardFiller(Board board) {
        this.board = board;
    }

    /**
     * Filling 2D image view array with images of pieces or with empty image as an
     * empty square on board
     * @return created image view to chess scene
     */
    public ImageView[][]boardFill(){
        Image[][] images = new Image[8][8];
        for (int i = 0; i < 8; i++){ //y
            for (int j = 0; j < 8; j++){ //x
                Piece p = board.getPiece(i, j);
                if (p == null){
                    images[j][i] = new Image("/images/empty.png");
                }else{
                    if(p.toString().equals("pawn")){
                        if(p.getColor().equals(Color.BLACK)){
                            images[j][i] = new Image("/images/bpawn.png");
                        }else{
                            images[j][i] = new Image("/images/wpawn.png");
                        }
                    }else if(p.toString().equals("rook")){
                        if(p.getColor().equals(Color.BLACK)){
                            images[j][i] = new Image("/images/brook.png");
                        }else{
                            images[j][i] = new Image("/images/wrook.png");
                        }
                    }else if(p.toString().equals("knight")){
                        if(p.getColor().equals(Color.BLACK)){
                            images[j][i] = new Image("/images/bknight.png");
                        }else{
                            images[j][i] = new Image("/images/wknight.png");
                        }
                    }else if(p.toString().equals("bishop")){
                        if(p.getColor().equals(Color.BLACK)){
                            images[j][i] = new Image("/images/bbishop.png");
                        }else{
                            images[j][i] = new Image("/images/wbishop.png");
                        }
                    }else if(p.toString().equals("queen")){
                        if(p.getColor().equals(Color.BLACK)){
                            images[j][i] = new Image("/images/bqueen.png");
                        }else{
                            images[j][i] = new Image("/images/wqueen.png");
                        }
                    }else{
                        if(p.getColor().equals(Color.BLACK)){
                            images[j][i] = new Image("/images/bking.png");
                        }else{
                            images[j][i] = new Image("/images/wking.png");
                        }
                    }
                }

            }
        }
        ImageView[][] imageViews = new ImageView[8][8];

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                imageViews[x][y] = new ImageView();
            }
        }
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                imageViews[x][y].setImage(images[x][y]);
                imageViews[x][y].setFitWidth(47);
                imageViews[x][y].setFitHeight(47);
                imageViews[x][y].setPreserveRatio(true);
                imageViews[x][y].setSmooth(true);
                imageViews[x][y].setCache(true);
            }
        }

        return imageViews;
    }
}
