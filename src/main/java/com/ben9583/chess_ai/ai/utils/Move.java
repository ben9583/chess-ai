package com.ben9583.chess_ai.ai.utils;

import com.ben9583.chess_ai.components.pieces.Piece;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;

public record Move(@NotNull Piece piece, @NotNull Vector2 position) {
    public Move(@NotNull Piece piece, Vector2 position) {
        if(position.getX() < 0 || position.getY() < 0) throw new IllegalArgumentException("Cannot construct Move: position has negative values: " + position);

        this.piece = piece;
        this.position = position;
    }
}
