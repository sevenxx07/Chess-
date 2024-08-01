package chessclient.gamelogic;

import chessclient.pieces.Color;
import chessclient.pieces.King;
import chessclient.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

/**
 * The Ruleset class contains rules concerning the king and whether he is in danger
 * and how this situation affects possible moves of other pieces. At the same time,
 * it detects the end of the game - checkmate or stalemate.
 */
public class Ruleset {

    /**
     * Checks if the player of the given color lost the game due to a checkmate.
     *
     * @param kingsColor color of the player whose turn it currently is
     * @param b          current game board
     * @return true if the player has no valid Moves, and his King has been
     * checkmated, else false
     */
    public static boolean detectCheckMate(Color kingsColor, Board b) {
        List<Move> validMoves = getAllValidMoves(kingsColor, b);
        if (validMoves.isEmpty() && isKingChecked(kingsColor, b)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if the players' match ended in draw.
     *
     * @param kingsColor color of the player whose turn it currently is
     * @param b          current game board
     * @return true if the player can't play any Move, but hasn't lost, else false
     */
    public static boolean detectDraw(Color kingsColor, Board b) {
        List<Move> validMoves = getAllValidMoves(kingsColor, b);
        if (validMoves.isEmpty() && !isKingChecked(kingsColor, b)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if king of the given color is checked on the current board.
     *
     * @param kingsColor king's color
     * @param b          the current game board
     * @return true if the king of the given color is checked, else false
     */
    public static boolean isKingChecked(Color kingsColor, Board b) {
        Color enemyColor = kingsColor.equals(Color.BLACK) ? Color.WHITE : Color.BLACK;
        List<Move> enemyMoves = getAllPossibleCaptures(enemyColor, b);
        Piece[][] board = b.getCurrentBoard();

        for (Move m : enemyMoves) {
            int[] to = m.getTo();
            if (board[to[0]][to[1]] != null && board[to[0]][to[1]].getClass().equals(King.class)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if the move doesn't endanger the king, thus whether the Move is valid
     *
     * @param b current game board
     * @param m Move the validity of which is to be decided
     * @return true if the Move doesn't expose the king of the same color, else false
     */
    public static boolean isValidMove(Board b, Move m) {
        Piece[][] oldBoard = b.getCurrentBoard();
        int[] from = m.getFrom();
        Color movedColor = oldBoard[from[0]][from[1]].getColor();
        Board hypothetical = playHypothetical(b, m);

        return !isKingChecked(movedColor, hypothetical);
    }


    /**
     * Generates a List containing all Moves of the player of the given color, which may
     * lead up to checkmate or draw.
     *
     * @param color Color of the player whose Moves should be retrieved
     * @param b     current Board
     * @return List containing all Moves of the player of the given color
     */
    public static List<Move> getAllPossibleCaptures(Color color, Board b) {
        List<Piece> pieces = color.equals(Color.WHITE) ? b.getWhPieces() : b.getBlPieces();
        List<Move> moves = new ArrayList<>();

        //For each enemy piece get possible captures
        for (Piece p : pieces) {
            //ignore king because it can't directly checkmate the other king
            if (p.getClass().equals(King.class)) {
                moves.addAll(((King) p).getPossibleNormalMoves(b));
            } else {
                moves.addAll(p.getPossibleMoves(b));
            }
        }

        return moves;
    }

    /**
     * Returns a List of all Moves player of given color can make which are valid with
     * regards to the individual Pieces' movement logic, and with regards of the Moves
     * not exposing the player's King.
     *
     * @param color color of the Player
     * @param b     the current game board
     * @return List of Moves the player of given color can make.
     */
    public static List<Move> getAllValidMoves(Color color, Board b) {
        List<Piece> pieces = color.equals(Color.WHITE) ? b.getWhPieces() : b.getBlPieces();
        List<Move> moves = new ArrayList<>();

        //For each enemy piece get valid moves
        for (Piece p : pieces) {
            moves.addAll(p.getValidMoves(b));
        }

        return moves;
    }

    /**
     * Plays given Move on a copy of the given Board. The original Board
     * is not affected by this Move.
     *
     * @param b Board on which the move is to be played
     * @param m Move to be played
     * @return Board on which the given Move has been played
     */
    private static Board playHypothetical(Board b, Move m) {
        Board copy = new Board(b);
        if (m.getSpecialMove() != null && m.getSpecialMove().equals(SpecialMove.PROMOTION)) {
            m.setPromotionIdentifier("queen");
            copy.playMove(m);
            m.setPromotionIdentifier(null);
        } else {
            copy.playMove(m);
        }

        return copy;
    }
}
