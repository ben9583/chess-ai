package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public abstract class Piece {
    @NotNull
    private final Player player;
    @NotNull
    private final Board board;

    public Piece(@NotNull Player player, @NotNull Board board) {
        this.player = player;
        this.board = board;
    }

    @Override
    public String toString() {
        return (this.player.equals(Player.WHITE) ? "White" : "Black") + this.getClass().getSimpleName();
    }

    public abstract int getValue();

    public abstract String getIconPath();

    public abstract Vector2[] getMovableSquares(boolean considerChecks);

    protected boolean isValidTarget(Vector2 position, boolean considerChecks) {
        return this.isValidTarget(position, considerChecks, Stream.empty(), Stream.empty());
    }

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

    public void movePiece(Vector2 position) {
        this.pieceMoved(position);
        if(!(this instanceof Pawn)) {
            this.board.setEnPassantPosition(null);
        }

        this.board.movePiece(this, position);
    }

    protected abstract void pieceMoved(Vector2 position);

    @NotNull
    public Vector2 getPosition() {
        return this.board.getPosition(this);
    }

    @NotNull
    public Player getPlayer() {
        return this.player;
    }

    @NotNull
    public Board getBoard() {
        return this.board;
    }
}
