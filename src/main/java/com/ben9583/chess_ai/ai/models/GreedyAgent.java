package com.ben9583.chess_ai.ai.models;

import com.ben9583.chess_ai.ai.AIAgent;
import com.ben9583.chess_ai.ai.utils.Move;
import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.components.pieces.Piece;
import com.ben9583.chess_ai.utils.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Very simple AI Agent that picks the best move presented to it.
 * The 'best move' is the move that captures a piece with the highest value.
 * Does not consider strategically valuable moves like checkmate or promotions.
 * In the case of a tie, picks random.
 */
public class GreedyAgent extends AIAgent {
    private final Random randomGenerator;

    public GreedyAgent(Board board, Player player) {
        super(board, player);

        this.randomGenerator = new Random();
    }

    public GreedyAgent(Board board, Player player, int seed) {
        super(board, player);

        this.randomGenerator = new Random(seed);
    }

    @Override
    public Move getNextMove() {
        List<Move> possibleMoves = new ArrayList<>();
        Piece[] myPieces = super.getMyPieces();

        int highScore = 0;

        for(Piece p : myPieces) {
            Vector2[] possiblePositions = p.getMovableSquares(true);
            for(Vector2 v : possiblePositions) {
                Piece target = super.board.getPieceAtPosition(v);
                if(target == null) {
                    if(highScore == 0) possibleMoves.add(new Move(p, v));
                } else {
                    if(target.getValue() > highScore) {
                        possibleMoves.clear();
                        possibleMoves.add(new Move(p, v));
                        highScore = target.getValue();
                    }
                }
            }
        }

        return possibleMoves.get(randomGenerator.nextInt(possibleMoves.size()));
    }

    @Override
    public boolean shouldResign() {
        return false;
    }

    @Override
    public String promote() {
        return "Queen";
    }
}
