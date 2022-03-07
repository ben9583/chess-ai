package com.ben9583.chess_ai.ai.models;

import com.ben9583.chess_ai.ai.AIAgent;
import com.ben9583.chess_ai.ai.utils.Move;
import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.components.pieces.Piece;
import com.ben9583.chess_ai.utils.Vector2;

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
        Piece[] myPieces = super.getMyPieces();

        int rand = randomGenerator.nextInt(myPieces.length);
        Piece selectedPiece = myPieces[rand];

        Vector2[] movableSquares = selectedPiece.getMovableSquares(true);
        rand = randomGenerator.nextInt(movableSquares.length);
        Vector2 square = movableSquares[rand];

        return new Move(selectedPiece, square);
    }
}
