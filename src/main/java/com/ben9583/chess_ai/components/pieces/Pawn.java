package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(@NotNull Player player, @NotNull Board board) {
        super(player, board);
    }

    @Override
    public int getValue() {
        return 1;
    }

    @Override
    public Vector2[] getMovableSquares() {
        Vector2 moveToConsider;
        Vector2 firstMoveToConsider;
        Vector2[] attacksToConsider;
        if(super.getPlayer().equals(Player.WHITE)) {
            moveToConsider = Vector2.NORTH;
            firstMoveToConsider = new Vector2(0, 2);
            attacksToConsider = new Vector2[]{ Vector2.NORTHWEST, Vector2.NORTHEAST };
        } else {
            moveToConsider = Vector2.SOUTH;
            firstMoveToConsider = new Vector2(0, -2);
            attacksToConsider = new Vector2[]{ Vector2.SOUTHWEST, Vector2.SOUTHEAST };
        }

        List<Vector2> positionsToConsider = new ArrayList<>();

        if(super.isValidTarget(moveToConsider)) positionsToConsider.add(moveToConsider);
        if(super.isValidTarget(firstMoveToConsider) && ((super.getPlayer().equals(Player.WHITE) && super.getPosition().getY() == 1) || (super.getPlayer().equals(Player.BLACK) && super.getPosition().getY() == 6)))
            positionsToConsider.add(firstMoveToConsider);

        for(Vector2 attack : attacksToConsider) {
            if(super.isValidTarget(attack)) {
                Piece target = super.getBoard().getPieceAtPosition(attack);
                Vector2 enPassantPosition = super.getBoard().getEnPassantPosition();
                if ((enPassantPosition != null && enPassantPosition.equals(attack)) || (target != null && target.getPlayer() != super.getPlayer()))
                    positionsToConsider.add(attack);
            }
        }

        return positionsToConsider.toArray(new Vector2[0]);
    }

    @Override
    protected void pieceMoved(Vector2 position) {
        Vector2 enPassantPosition = super.getBoard().getEnPassantPosition();
        if(enPassantPosition != null && enPassantPosition.equals(position)) {
            if(super.getPlayer().equals(Player.WHITE)) {
                super.getBoard().removePiece(position.add(Vector2.SOUTH));
            } else {
                super.getBoard().removePiece(position.add(Vector2.NORTH));
            }
        }

        if(Math.abs(super.getPosition().getY() - position.getY()) == 2) {
            if(super.getPlayer().equals(Player.WHITE)) {
                super.getBoard().setEnPassantPosition(position.add(Vector2.SOUTH));
            } else {
                super.getBoard().setEnPassantPosition(position.add(Vector2.NORTH));
            }
        } else {
            super.getBoard().setEnPassantPosition(null);
        }

        super.getBoard().resetHalfMoveClock();
    }
}
