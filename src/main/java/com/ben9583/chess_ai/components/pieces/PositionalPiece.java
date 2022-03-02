package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class PositionalPiece extends Piece {
    public PositionalPiece(@NotNull Player player, @NotNull Board board) { super(player, board); }

    protected abstract Vector2[] getRelativeSquares();

    public Vector2[] getMovableSquares() {
        return Arrays.stream(this.getRelativeSquares())
                .map(super.getPosition()::add)
                .filter(super::isValidTarget)
                .toArray(Vector2[]::new);
    }
}
