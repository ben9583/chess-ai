package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.utils.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Knook extends Piece {
    /* The knook can move anywhere that is two squares away in one dimension and one square away in another. */
    private static final Vector2[] relativePositions = {
            new Vector2(1, 2),
            new Vector2(2, 1),
            new Vector2(2, -1),
            new Vector2(1, -2),
            new Vector2(-1, -2),
            new Vector2(-2, -1),
            new Vector2(-2, 1),
            new Vector2(-1, 2)
    };

    /* The knook can move vertically or horizontally. */
    private static final Vector2[] relativeDirections = {
            Vector2.NORTH,
            Vector2.EAST,
            Vector2.SOUTH,
            Vector2.WEST
    };

    public Knook(Player player, Board board) { super(player, board); }

    @Override
    public String getIconPath() {
        if(this.getPlayer().equals(Player.WHITE))
            return "src/main/resources/images/piece_icons/wo.png";
        else
            return "src/main/resources/images/piece_icons/bo.png";
    }

    @Override
    public int getValue() {
        return 161660;
    }

    @Override
    public char getFENSymbol() {
        return 'O';
    }

    @Override
    public Vector2[] getMovableSquares(boolean considerChecks) {
        List<Vector2> possibleSquares = new ArrayList<>();

        Vector2 position = super.getPosition();
        for(Vector2 direction : relativeDirections) {
            Vector2 positionToCheck = position.add(direction);
            while(super.isValidTarget(positionToCheck, false)) {
                if(!considerChecks || super.isValidTarget(positionToCheck, true)) possibleSquares.add(positionToCheck);
                if(super.getBoard().getPieceAtPosition(positionToCheck) != null) break;
                positionToCheck = positionToCheck.add(direction);
            }
        }

        for(Vector2 pos : relativePositions) {
            Vector2 positionToCheck = position.add(pos);
            if(super.isValidTarget(positionToCheck, considerChecks))
                possibleSquares.add(positionToCheck);
        }

        return possibleSquares.toArray(new Vector2[0]);
    }

    @Override
    protected void pieceMoved(Vector2 position) {}
}
