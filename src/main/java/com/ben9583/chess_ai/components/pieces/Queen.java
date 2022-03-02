package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;

public class Queen extends DirectionalPiece {
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

    public Queen(@NotNull Player player, @NotNull Board board) {
        super(player, board);
    }

    @Override
    public int getValue() {
        return 9;
    }

    @Override
    public String getIconPath() {
        if(this.getPlayer().equals(Player.WHITE))
            return "src/main/resources/images/piece_icons/wq.png";
        else
            return "src/main/resources/images/piece_icons/bq.png";
    }

    @Override
    protected Vector2[] getRelativeSquares() {
        return Queen.relativeSquares;
    }

    @Override
    protected void pieceMoved(Vector2 position) {}
}
