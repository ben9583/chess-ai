package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;

public class Pawn extends Piece {
    public Pawn(@NotNull Player player, @NotNull Board board) {
        super(player, board);
    }

    @Override
    public int getValue() {
        return 1;
    }

    @Override
    public Vector2[] getMovableSquares() {
        throw new UnsupportedOperationException();
    }
}
