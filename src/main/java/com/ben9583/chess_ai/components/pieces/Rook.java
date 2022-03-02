package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;

public class Rook extends DirectionalPiece {
    private static final Vector2[] relativeSquares = {
            Vector2.NORTH,
            Vector2.EAST,
            Vector2.SOUTH,
            Vector2.WEST
    };

    private boolean hasMoved = false;

    public Rook(@NotNull Player player, @NotNull Board board) {
        super(player, board);
    }

    @Override
    public int getValue() {
        return 5;
    }

    @Override
    public String getIconPath() {
        if(this.getPlayer().equals(Player.WHITE))
            return "src/main/resources/images/piece_icons/wr.png";
        else
            return "src/main/resources/images/piece_icons/br.png";
    }

    @Override
    protected Vector2[] getRelativeSquares() {
        return Rook.relativeSquares;
    }

    @Override
    protected void pieceMoved(Vector2 position) {
        if(!this.hasMoved) {
            this.hasMoved = true;
            if(super.getPosition().getX() > 0) {
                if(super.getPlayer().equals(Player.WHITE)) {
                    super.getBoard().disableCastleWhiteKing();
                } else {
                    super.getBoard().disableCastleBlackKing();
                }
            } else {
                if(super.getPlayer().equals(Player.WHITE)) {
                    super.getBoard().disableCastleWhiteQueen();
                } else {
                    super.getBoard().disableCastleBlackQueen();
                }
            }

        }
    }
}
