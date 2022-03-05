package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Abstract class representing a positional piece.
 * A positional piece is a piece whose movable squares
 * can be represented as a finite array of relative positions
 * so long as they are not occupied by a piece belonging to
 * the same player.
 */
public abstract class PositionalPiece extends Piece {
    public PositionalPiece(@NotNull Player player, @NotNull Board board) { super(player, board); }

    /**
     * Returns an array of squares relative to
     * the current position of the piece that it
     * can move to. This class automatically checks if
     * the moves are legal, so this should usually return
     * a constant array of relative positions.
     * @return An array of squares relative to this piece's position
     */
    protected abstract Vector2[] getRelativeSquares();

    @Override
    public Vector2[] getMovableSquares(boolean considerChecks) {
        return Arrays.stream(this.getRelativeSquares())
                .map(super.getPosition()::add)
                .filter((Vector2 pos) -> super.isValidTarget(pos, considerChecks))
                .toArray(Vector2[]::new);
    }
}
