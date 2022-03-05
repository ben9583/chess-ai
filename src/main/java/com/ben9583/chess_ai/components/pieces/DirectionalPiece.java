package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class representing a directional piece.
 * A directional piece is a piece that can move in some
 * relative direction for an arbitrary length until it
 * reaches another piece, in which case it is 'blocked'.
 */
public abstract class DirectionalPiece extends Piece {
    public DirectionalPiece(@NotNull Player player, @NotNull Board board) { super(player, board); }

    /**
     * Returns an array of directions this piece can move to.
     * This piece will consider all squares in this direction until
     * it is 'blocked' by a piece.
     *
     * See also: Cardinal directions in Vector2.
     * @return An array of directions this piece can move to
     */
    protected abstract Vector2[] getRelativeSquares();

    @Override
    public Vector2[] getMovableSquares(boolean considerChecks) {
        List<Vector2> possibleSquares = new ArrayList<>();

        Vector2 position = super.getPosition();
        for(Vector2 direction : this.getRelativeSquares()) {
            Vector2 positionToCheck = position.add(direction);
            while(super.isValidTarget(positionToCheck, false)) {
                if(!considerChecks || super.isValidTarget(positionToCheck, true)) possibleSquares.add(positionToCheck);
                if(super.getBoard().getPieceAtPosition(positionToCheck) != null) break;
                positionToCheck = positionToCheck.add(direction);
            }
        }

        return possibleSquares.toArray(new Vector2[0]);
    }
}
