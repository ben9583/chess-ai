package com.ben9583.chess_ai.ai;

import com.ben9583.chess_ai.ai.utils.Move;
import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.components.pieces.Piece;

/**
 * Abstract base class for any AI agent to play chess.
 * Defines certain methods that the Board will expect
 * to be able to call in order to get a decision.
 */
public abstract class AIAgent {
    /* Board this AIAgent will observe. */
    protected final Board board;
    /* Player this AIAgent will play as. */
    protected final Player player;

    public AIAgent(Board board, Player player) {
        this.board = board;
        this.player = player;
    }

    /**
     * Uses information from the Board to get the next move.
     * @return The next move this player should make
     */
    public abstract Move getNextMove();

    protected Piece[] getMyPieces() {
        return this.board.getPlayerPieces(this.player);
    }
}
