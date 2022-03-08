package com.ben9583.chess_ai.ai;

import com.ben9583.chess_ai.ai.utils.Move;
import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.components.pieces.Piece;

/**
 * Abstract base class for any AI agent to play chess.
 * Defines certain methods that the ChessGame will expect
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

    /**
     * Fired when player makes a move prompting a promotion.
     * Should decide which piece to promote to.
     * @return The piece that the pawn will promote to.
     */
    public abstract String promote();

    /**
     * Returns a decision by the AI if they should resign.
     * Ends the game with this AI losing.
     * @return Whether the AI wants to resign
     */
    public abstract boolean shouldResign();

    protected Piece[] getMyPieces() {
        return this.board.getPlayerPieces(this.player);
    }
}
