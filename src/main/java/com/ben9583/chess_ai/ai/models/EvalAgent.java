package com.ben9583.chess_ai.ai.models;

import com.ben9583.chess_ai.ai.AIAgent;
import com.ben9583.chess_ai.ai.utils.Move;
import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.components.pieces.Piece;
import com.ben9583.chess_ai.utils.Vector2;

import java.util.concurrent.atomic.AtomicReference;

public abstract class EvalAgent extends AIAgent {
    public EvalAgent(Board board, Player player) { super(board, player); }

    public abstract float evaluatePosition();

    @Override
    public Move getNextMove() {
        if(this.shouldResign()) super.board.resign(super.player);

        Piece[] myPieces = super.getMyPieces();

        Move bestMove = null;
        float bestEval = -Float.MAX_VALUE;

        for(Piece p : myPieces) {
            Vector2[] possiblePositions = p.getMovableSquares(true);
            for(Vector2 v : possiblePositions) {
                Move m = new Move(p, v);
                AtomicReference<Float> eval = new AtomicReference<>(0.0f);

                super.board.runOnMove(p, v, () -> eval.set(this.evaluatePosition()));
                if(eval.get() > bestEval) {
                    bestEval = eval.get();
                    bestMove = m;
                }
            }
        }

        return bestMove;
    }
}
