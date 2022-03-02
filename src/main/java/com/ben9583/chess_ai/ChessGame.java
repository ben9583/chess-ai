package com.ben9583.chess_ai;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.gfx.ChessAIScene;
import com.ben9583.chess_ai.gfx.ChessAIWindow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChessGame {

    @Nullable
    private ChessAIWindow window = null;
    @Nullable
    private ChessAIScene scene = null;

    @NotNull
    private final Board board;

    public ChessGame(boolean graphicsEnabled) {
        if(graphicsEnabled) {
            this.window = new ChessAIWindow();
            this.scene = this.window.getScene();
        }

        this.board = new Board();
    }


}
