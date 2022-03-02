package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;

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
        return (this.player.equals(Player.WHITE) ? "White" : "Black") + super.toString();
    }

    public abstract int getValue();

    public abstract Vector2[] getMovableSquares();

    protected boolean isValidTarget(Vector2 position) {
        if(!this.board.boardExistsAt(position)) return false;

        Piece target = this.board.getPieceAtPosition(position);
        return (target == null || !target.getPlayer().equals(this.player)) && !this.board.movePlacesInCheck(this, position);
    }

    public void movePiece(Vector2 position) {
        this.pieceMoved(position);

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
