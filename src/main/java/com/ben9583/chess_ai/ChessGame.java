package com.ben9583.chess_ai;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.gfx.ChessAIScene;
import com.ben9583.chess_ai.gfx.ChessAIWindow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChessGame {

    @Nullable
    private ChessAIWindow window = null;

    @NotNull
    private final Board board;

    public ChessGame(boolean graphicsEnabled) {
        this.board = new Board();
        if(graphicsEnabled) {
            this.window = new ChessAIWindow(this.board);
        }
    }
}
