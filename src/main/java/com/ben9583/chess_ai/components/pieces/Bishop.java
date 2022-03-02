package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;

public class Bishop extends DirectionalPiece {
    private static final Vector2[] relativeSquares = {
            Vector2.NORTHEAST,
            Vector2.SOUTHEAST,
            Vector2.SOUTHWEST,
            Vector2.NORTHWEST
    };

    public Bishop(@NotNull Player player, @NotNull Board board) {
        super(player, board);
    }

    @Override
    public int getValue() {
        return 3;
    }

    @Override
    protected Vector2[] getRelativeSquares() {
        return Bishop.relativeSquares;
    }
}
