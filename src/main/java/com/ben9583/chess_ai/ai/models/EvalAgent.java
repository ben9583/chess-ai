package com.ben9583.chess_ai.ai.models;

import com.ben9583.chess_ai.ai.AIAgent;
import com.ben9583.chess_ai.ai.utils.Move;
import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.components.pieces.Piece;
import com.ben9583.chess_ai.utils.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * AI Agent that relies on an evaluation of the board to pick the move
 * that has the highest evaluation. Higher values are preferred.
 */
public abstract class EvalAgent extends AIAgent {
    public EvalAgent(Board board, Player player) { super(board, player); }

    /**
     * Evaluates the array of boards,
     * with a higher value indicating a more favorable position
     * for this agent.
     * @param boards Array of boards to evaluate
     * @return Array of evaluations for each respective board in boards
     */
    public abstract float[] evaluatePositions(float[][][][] boards);

    @Override
    public Move getNextMove() {
        if(this.shouldResign()) super.board.resign(super.player);

        Piece[] myPieces = super.getMyPieces();

        List<float[][][]> boards = new ArrayList<>();
        List<Move> moves = new ArrayList<>();
        for(Piece p : myPieces) {
            Vector2[] possiblePositions = p.getMovableSquares(true);
            for(Vector2 v : possiblePositions) {
                Move m = new Move(p, v);
                moves.add(m);

                super.board.runOnMove(p, v, () -> boards.add(super.board.get3DBoard()));
            }
        }

        float[] evals = this.evaluatePositions(boards.toArray(new float[boards.size()][][][]));

        Move bestMove = null;
        float bestEval = -Float.MAX_VALUE;

        for(int i = 0; i < evals.length; i++) {
            if(evals[i] > bestEval) {
                bestEval = evals[i];
                bestMove = moves.get(i);
            }
        }

        return bestMove;
    }
}
