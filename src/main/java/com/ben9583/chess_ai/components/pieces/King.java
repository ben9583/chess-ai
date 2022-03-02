package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;

public class King extends PositionalPiece {
    private static final Vector2[] relativeSquares = {
            Vector2.NORTH,
            Vector2.NORTHEAST,
            Vector2.EAST,
            Vector2.SOUTHEAST,
            Vector2.SOUTH,
            Vector2.SOUTHWEST,
            Vector2.WEST,
            Vector2.NORTHWEST
    };

    public King(@NotNull Player player, @NotNull Board board) {
        super(player, board);
    }

    @Override
    public int getValue() {
        return 99999;
    }

    @Override
    protected Vector2[] getRelativeSquares() {
        return King.relativeSquares;
    }
}
