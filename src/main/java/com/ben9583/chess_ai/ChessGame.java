package com.ben9583.chess_ai;

import com.ben9583.chess_ai.ai.AIAgent;
import com.ben9583.chess_ai.ai.models.GreedyAgent;
import com.ben9583.chess_ai.ai.models.NeuralAgent;
import com.ben9583.chess_ai.ai.models.RandomAgent;
import com.ben9583.chess_ai.ai.utils.Move;
import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
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
        this.aiPlayers = new HashMap<>();

        this.aiPlayers.put(Player.BLACK, new NeuralAgent(this.board, Player.BLACK, 7));

        if(graphicsEnabled) {
            this.window = new ChessAIWindow(this.board);
            this.board.bindNextTurnEvent(this::onNextTurn);
        } else {
            this.aiPlayers.put(Player.WHITE, new RandomAgent(this.board, Player.WHITE, 7));
            while(!this.board.isGameOver()) this.onNextTurn();

            System.out.println(this.board.getGameOverReason());
            System.out.println(this.board.getPGN());
        }
    }

    private void onNextTurn() {
        AIAgent whoShouldPlay = this.aiPlayers.get(this.board.getWhoseTurn());

        if(whoShouldPlay != null) {
            if(whoShouldPlay.shouldResign()) {
                this.board.resign(this.board.getWhoseTurn());
                return;
            }
            Move nextMove = whoShouldPlay.getNextMove();
            nextMove.piece().movePiece(nextMove.position());

            System.out.println(this.board.toFEN());

            if(this.board.awaitingPromotion()) this.board.promote(whoShouldPlay.promote());
        }
    }
}
