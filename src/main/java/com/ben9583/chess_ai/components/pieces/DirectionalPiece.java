package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class DirectionalPiece extends Piece {
    public DirectionalPiece(@NotNull Player player, @NotNull Board board) { super(player, board); }

    protected abstract Vector2[] getRelativeSquares();

    public Vector2[] getMovableSquares() {
        List<Vector2> possibleSquares = new ArrayList<>();

        Vector2 position = super.getPosition();
        for(Vector2 direction : this.getRelativeSquares()) {
            Vector2 positionToCheck = position.add(direction);
            while(super.isValidTarget(positionToCheck)) {
                possibleSquares.add(positionToCheck);
                positionToCheck = positionToCheck.add(direction);
            }
        }

        return possibleSquares.toArray(new Vector2[0]);
    }
}
