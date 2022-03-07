package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * Abstract class representing a chess piece.
 */
public abstract class Piece {
    /* The player this piece belongs to. */
    @NotNull
    private final Player player;
    /* The board this piece belongs to. */
    @NotNull
    private final Board board;

    /**
     * Abstract constructor for a piece belonging to player and board.
     * @param player Who this piece belongs to
     * @param board What board this piece belongs to
     */
    public Piece(@NotNull Player player, @NotNull Board board) {
        this.player = player;
        this.board = board;
    }

    @Override
    public String toString() {
        return (this.player.equals(Player.WHITE) ? "White" : "Black") + this.getClass().getSimpleName();
    }

    /**
     * The standard value for any chess piece.
     * @return The value of this piece
     */
    public abstract int getValue();

    /**
     * The symbol that represents this piece in Forsyth-Edwards Notation (FEN).
     * @return Character representing this piece in FEN
     */
    public abstract char getFENSymbol();

    /**
     * The filepath to the image that should be used to represent this piece graphically.
     * @return Filepath to an image
     */
    public abstract String getIconPath();

    /**
     * Returns an array of squares this piece can legally move to.
     * Each piece has its own rules for this and should consider
     * the board and its position when evaluating.
     *
     * See also: isValidTarget
     * @param considerChecks Whether this piece should include moves that put the player's king in check
     * @return Array of moves that this piece can legally move to
     */
    public abstract Vector2[] getMovableSquares(boolean considerChecks);

    /**
     * Returns whether position is a valid square for this piece to move to.
     * @param position Square to check
     * @param considerChecks Whether to consider moves that put the player's king in check to not be valid
     * @return Whether position is a valid square
     */
    protected boolean isValidTarget(Vector2 position, boolean considerChecks) {
        return this.isValidTarget(position, considerChecks, Stream.empty(), Stream.empty());
    }

    /**
     * Returns whether position is a valid square for this piece to move to.
     * Runs everything in onMoves before moving and onBacks after moving to
     * restore the original state before moving.
     *
     * This method is useful for moves that alter the state of the board in
     * ways beyond just the position of the pieces, like castling.
     * @param position Square to check
     * @param considerChecks Whether to consider moves that put the player's king in check to not be valid
     * @param onMoves Stream of Runnables to run before moving
     * @param onBacks Stream of Runnables that undo the Runnables in onMoves
     * @return
     */
    protected boolean isValidTarget(Vector2 position, boolean considerChecks, Stream<Runnable> onMoves, Stream<Runnable> onBacks) {
        if(!this.board.boardExistsAt(position)) return false;

        Piece target = this.board.getPieceAtPosition(position);
        if(target != null && target.getPlayer().equals(this.player)) return false;
        if(!considerChecks) return true;

        onMoves.forEach(Runnable::run);
        boolean result = !this.board.doesThisMovePutMeInCheck(this, position);
        onBacks.forEach(Runnable::run);

        return result;
    }

    /**
     * Moves this piece to some position.
     * @param position Square to move this piece to
     */
    public void movePiece(Vector2 position) {
        this.pieceMoved(position);
        if(!(this instanceof Pawn)) {
            this.board.setEnPassantPosition(null);
        }

        this.board.movePiece(this, position);
    }

    /**
     * Fired right before a piece is about to be moved to position.
     * This method should be overridden by pieces that change the state
     * of the board other than position when moved.
     * @param position Square this piece is about to move to
     */
    protected abstract void pieceMoved(Vector2 position);

    /**
     * Returns the position this piece is at.
     * @return The position this piece is at
     */
    @NotNull
    public Vector2 getPosition() {
        return this.board.getPosition(this);
    }

    /**
     * Returns the player this piece belongs to.
     * @return The player this piece belongs to
     */
    @NotNull
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Returns the board this piece belongs to.
     * @return The board this piece belongs to
     */
    @NotNull
    public Board getBoard() {
        return this.board;
    }
}
