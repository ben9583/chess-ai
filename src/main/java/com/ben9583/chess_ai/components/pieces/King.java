package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class King extends PositionalPiece {
    private static final Vector2[] relativeSquares = {
            Vector2.NORTH,
            Vector2.NORTHEAST,
            Vector2.EAST,
            Vector2.SOUTHEAST,
            Vector2.SOUTH,
            Vector2.SOUTHWEST,
            Vector2.WEST,
            Vector2.NORTHWEST
    };

    private static final Vector2 kingSideCastleSquare = new Vector2(2, 0);
    private static final Vector2 queenSideCastleSquare = new Vector2(-2, 0);

    public King(@NotNull Player player, @NotNull Board board) {
        super(player, board);
    }

    @Override
    public int getValue() {
        return 99999;
    }

    @Override
    protected Vector2[] getRelativeSquares() {
        List<Vector2> out = new ArrayList<>(Arrays.asList(King.relativeSquares));

        if(super.getPlayer().equals(Player.WHITE)) {
            if(super.getBoard().isCastleWhiteKing() && out.contains(Vector2.EAST) && super.isValidTarget(new Vector2(2, 0))) {
                out.add(new Vector2(2, 0));
            }
            if(super.getBoard().isCastleWhiteQueen() && out.contains(Vector2.WEST) && super.isValidTarget(new Vector2(-2, 0))) {
                out.add(new Vector2(-2, 0));
            }
        } else {
            if(super.getBoard().isCastleBlackKing() && out.contains(Vector2.EAST) && super.isValidTarget(new Vector2(2, 0))) {
                out.add(new Vector2(2, 0));
            }
            if(super.getBoard().isCastleBlackQueen() && out.contains(Vector2.WEST) && super.isValidTarget(new Vector2(-2, 0))) {
                out.add(new Vector2(-2, 0));
            }
        }

        return King.relativeSquares;
    }

    @Override
    protected void pieceMoved(Vector2 position) {
        if(Math.abs(super.getPosition().getX() - position.getX()) == 2) {
            if(super.getPlayer().equals(Player.WHITE)) {
                super.getBoard().disableCastleWhiteKing();
                super.getBoard().disableCastleWhiteQueen();
            } else {
                super.getBoard().disableCastleBlackKing();
                super.getBoard().disableCastleBlackQueen();
            }
        }
    }
}
