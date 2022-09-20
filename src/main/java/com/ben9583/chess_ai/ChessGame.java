package com.ben9583.chess_ai;

import com.ben9583.chess_ai.ai.AIAgent;
import com.ben9583.chess_ai.ai.models.NeuralAgent;
import com.ben9583.chess_ai.ai.utils.Move;
import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.gfx.ChessAIWindow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ChessGame {
    @Nullable
    private ChessAIWindow window = null;

    @NotNull
    private Board board;

    @NotNull
    private Map<Player, AIAgent> aiPlayers;

    public ChessGame(boolean graphicsEnabled) {
        this.board = new Board();
        this.aiPlayers = new HashMap<>();

        NeuralAgent blackAgent = new NeuralAgent(this.board, Player.BLACK, 95);
        blackAgent.constructNeuralNetwork();

        this.aiPlayers.put(Player.BLACK, blackAgent);

        if(graphicsEnabled) {
            this.window = new ChessAIWindow(this.board);
            this.board.bindNextTurnEvent(this::onNextTurn);
        } else {
            NeuralAgent whiteAgent = new NeuralAgent(this.board, Player.WHITE, 83);
            whiteAgent.constructNeuralNetwork();

            this.aiPlayers.put(Player.WHITE, whiteAgent);

            blackAgent.loadDataFromFile("output.dat");
            whiteAgent.loadDataFromFile("output.dat");

            System.out.println("Begin training...");

            int epochs = 10000;

            for(int i = 0; i < epochs; i++) {
                while (!this.board.isGameOver()) this.onNextTurn();

                if(i * 100 % epochs == 0) {
                    System.out.println(i * 100 / epochs + "%");
                    System.out.println(this.board.getPGN());
                }

                if(!this.board.getGameOverReason().contains("Black"))
                    blackAgent.mutate();

                if(!this.board.getGameOverReason().contains("White"))
                    whiteAgent.mutate();

                this.board.resetGame();
            }

            blackAgent.saveDataToFile("output.dat");
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

            // System.out.println(this.board.toFEN());

            if(this.board.awaitingPromotion()) this.board.promote(whoShouldPlay.promote());
        }
    }
}
