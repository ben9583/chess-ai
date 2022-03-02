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

    public abstract int getValue();

    public abstract Vector2[] getMovableSquares();
}
