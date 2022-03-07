package com.ben9583.chess_ai;

import com.ben9583.chess_ai.ai.AIAgent;
import com.ben9583.chess_ai.ai.models.GreedyAgent;
import com.ben9583.chess_ai.ai.models.RandomAgent;
import com.ben9583.chess_ai.ai.utils.Move;
import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.gfx.ChessAIScene;
import com.ben9583.chess_ai.gfx.ChessAIWindow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ChessGame {
    @Nullable
    private ChessAIWindow window = null;

    @NotNull
    private final Board board;

    @NotNull
    private final Map<Player, AIAgent> aiPlayers;

    public ChessGame(boolean graphicsEnabled) {
        this.board = new Board();
        if(graphicsEnabled) {
            this.window = new ChessAIWindow(this.board);
        }

        this.aiPlayers = new HashMap<>();
        this.aiPlayers.put(Player.BLACK, new GreedyAgent(this.board, Player.BLACK, 9583));

        this.board.bindNextTurnEvent(this::onNextTurn);
    }

    private void onNextTurn() {
        AIAgent whoShouldPlay = this.aiPlayers.get(this.board.getWhoseTurn());

        if(whoShouldPlay != null) {
            Move nextMove = whoShouldPlay.getNextMove();
            this.board.movePiece(nextMove.piece(), nextMove.position());
        }
    }
}
