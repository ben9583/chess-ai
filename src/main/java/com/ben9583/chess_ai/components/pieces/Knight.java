package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class Knight extends PositionalPiece {
    private static final Vector2[] relativeSquares = {
            new Vector2(1, 2),
            new Vector2(2, 1),
            new Vector2(2, -1),
            new Vector2(1, -2),
            new Vector2(-1, -2),
            new Vector2(-2, -1),
            new Vector2(-2, 1),
            new Vector2(-1, 2)
    };

    public Knight(@NotNull Player player, @NotNull Board board) {
        super(player, board);
    }

    @Override
    public int getValue() {
        return 3;
    }

    @Override
    protected Vector2[] getRelativeSquares() {
        return Knight.relativeSquares;
    }

    @Override
    protected void pieceMoved(Vector2 position) {}
}
