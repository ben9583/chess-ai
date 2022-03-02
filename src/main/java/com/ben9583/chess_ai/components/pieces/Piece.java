package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Piece {
    private final Player player;

    public Piece(@NotNull Player player) {
        this.player = player;
    }
}
