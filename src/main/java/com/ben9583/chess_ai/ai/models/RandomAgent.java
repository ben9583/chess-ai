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
 * AI Agent that makes random moves.
 */
public class RandomAgent extends AIAgent {
    private final Random randomGenerator;

    public RandomAgent(Board board, Player player) {
        super(board, player);

        this.randomGenerator = new Random();
    }

    public RandomAgent(Board board, Player player, int seed) {
        super(board, player);

        this.randomGenerator = new Random(seed);
    }

    @Override
    public Move getNextMove() {
        List<Move> possibleMoves = new ArrayList<>();
        Piece[] myPieces = super.getMyPieces();

        for(Piece p : myPieces) {
            Vector2[] possiblePositions = p.getMovableSquares(true);
            for(Vector2 v : possiblePositions) {
                possibleMoves.add(new Move(p, v));
            }
        }

        return possibleMoves.get(randomGenerator.nextInt(possibleMoves.size()));
    }

    @Override
    public String promote() {
        return "Queen";
    }

    @Override
    public boolean shouldResign() {
        return false;
    }
}
