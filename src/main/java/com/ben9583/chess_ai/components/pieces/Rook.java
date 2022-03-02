package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;

public class Rook extends DirectionalPiece {
    private static final Vector2[] relativeSquares = {
            Vector2.NORTH,
            Vector2.EAST,
            Vector2.SOUTH,
            Vector2.WEST
    };

    public Rook(@NotNull Player player, @NotNull Board board) {
        super(player, board);
    }

    @Override
    public int getValue() {
        return 5;
    }

    @Override
    protected Vector2[] getRelativeSquares() {
        return Rook.relativeSquares;
    }

    @Override
    protected void pieceMoved(Vector2 position) {}
}
