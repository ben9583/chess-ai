package com.ben9583.chess_ai.components.pieces;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.*;

public class King extends PositionalPiece {
    /* Functions that should be run when castling. For the purposes of checking if castling is legal. */
    private final Stream<Runnable> castleWhiteKingSideRunnables = Stream.of(() -> getBoard().castleWhiteKing(King.this));
    private final Stream<Runnable> castleWhiteQueenSideRunnables = Stream.of(() -> getBoard().castleWhiteQueen(King.this));
    private final Stream<Runnable> castleBlackKingSideRunnables = Stream.of(() -> getBoard().castleBlackKing(King.this));
    private final Stream<Runnable> castleBlackQueenSideRunnables = Stream.of(() -> getBoard().castleBlackQueen(King.this));
    private final Stream<Runnable> uncastleWhiteKingSideRunnables = Stream.of(() -> getBoard().uncastleWhiteKing(King.this));
    private final Stream<Runnable> uncastleWhiteQueenSideRunnables = Stream.of(() -> getBoard().uncastleWhiteQueen(King.this));
    private final Stream<Runnable> uncastleBlackKingSideRunnables = Stream.of(() -> getBoard().uncastleBlackKing(King.this));
    private final Stream<Runnable> uncastleBlackQueenSideRunnables = Stream.of(() -> getBoard().uncastleBlackQueen(King.this));

    /* Functions that should be run to undo the state changes when castling. */
    private final Stream<Runnable> disableCastleWhiteKingSideRunnables = Stream.of(() -> getBoard().disableCastleWhiteKing());
    private final Stream<Runnable> disableCastleWhiteQueenSideRunnables = Stream.of(() -> getBoard().disableCastleWhiteQueen());
    private final Stream<Runnable> disableCastleBlackKingSideRunnables = Stream.of(() -> getBoard().disableCastleBlackKing());
    private final Stream<Runnable> disableCastleBlackQueenSideRunnables = Stream.of(() -> getBoard().disableCastleBlackQueen());
    private final Stream<Runnable> enableCastleWhiteKingSideRunnables = Stream.of(() -> getBoard().enableCastleWhiteKing());
    private final Stream<Runnable> enableCastleWhiteQueenSideRunnables = Stream.of(() -> getBoard().enableCastleWhiteQueen());
    private final Stream<Runnable> enableCastleBlackKingSideRunnables = Stream.of(() -> getBoard().enableCastleBlackKing());
    private final Stream<Runnable> enableCastleBlackQueenSideRunnables = Stream.of(() -> getBoard().enableCastleBlackQueen());

    /* The king can move to any of its neighboring squares. */
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

    /* Relative position the king moves to when king-side castling. */
    private static final Vector2 kingSideCastleSquare = new Vector2(2, 0);
    /* Relative position the king moves to when queen-side castling. */
    private static final Vector2 queenSideCastleSquare = new Vector2(-2, 0);

    public King(@NotNull Player player, @NotNull Board board) {
        super(player, board);
    }

    @Override
    public int getValue() {
        return 99999;
    }

    @Override
    public char getFENSymbol() {
        return 'K';
    }

    @Override
    public String getIconPath() {
        if(this.getPlayer().equals(Player.WHITE))
            return "src/main/resources/images/piece_icons/wk.png";
        else
            return "src/main/resources/images/piece_icons/bk.png";
    }

    @Override
    protected Vector2[] getRelativeSquares() {

        List<Vector2> out = new ArrayList<>(Arrays.asList(King.relativeSquares));

        if(super.getPlayer().equals(Player.WHITE)) {
            if(super.getBoard().canCastleWhiteKing() && !super.getBoard().isInCheck(super.getPlayer()) && super.isValidTarget(super.getPosition().add(Vector2.EAST), false, disableCastleWhiteKingSideRunnables, enableCastleWhiteKingSideRunnables) && super.isValidTarget(super.getPosition().add(kingSideCastleSquare), false, castleWhiteKingSideRunnables, uncastleWhiteKingSideRunnables)) {
                out.add(kingSideCastleSquare);
            }
            if(super.getBoard().canCastleWhiteQueen() && !super.getBoard().isInCheck(super.getPlayer()) && super.isValidTarget(super.getPosition().add(Vector2.WEST), false, disableCastleWhiteQueenSideRunnables, enableCastleWhiteQueenSideRunnables) && super.isValidTarget(super.getPosition().add(queenSideCastleSquare), false, castleWhiteQueenSideRunnables, uncastleWhiteQueenSideRunnables)) {
                out.add(queenSideCastleSquare);
            }
        } else {
            if(super.getBoard().canCastleBlackKing() && !super.getBoard().isInCheck(super.getPlayer()) && super.isValidTarget(super.getPosition().add(Vector2.EAST), false, disableCastleBlackKingSideRunnables, enableCastleBlackKingSideRunnables) && super.isValidTarget(super.getPosition().add(kingSideCastleSquare), false, castleBlackKingSideRunnables, uncastleBlackKingSideRunnables)) {
                out.add(kingSideCastleSquare);
            }
            if(super.getBoard().canCastleBlackQueen() && !super.getBoard().isInCheck(super.getPlayer()) && super.isValidTarget(super.getPosition().add(Vector2.WEST), false, disableCastleBlackQueenSideRunnables, enableCastleBlackQueenSideRunnables) && super.isValidTarget(super.getPosition().add(queenSideCastleSquare), false, castleBlackQueenSideRunnables, uncastleBlackQueenSideRunnables)) {
                out.add(queenSideCastleSquare);

            }
        }

        return out.toArray(new Vector2[0]);
    }

    @Override
    protected void pieceMoved(Vector2 position) {

        int dx = position.sub(super.getPosition()).getX();
        if(super.getPlayer().equals(Player.WHITE)) {
            if(dx == 2) {
                castleWhiteKingSideRunnables.forEach(Runnable::run);
            } else if(dx == -2) {
                castleWhiteQueenSideRunnables.forEach(Runnable::run);
            }
            super.getBoard().disableCastleWhiteKing();
            super.getBoard().disableCastleWhiteQueen();
        } else {
            if(dx == 2) {
                castleBlackKingSideRunnables.forEach(Runnable::run);
            } else if(dx == -2) {
                castleBlackQueenSideRunnables.forEach(Runnable::run);
            }
            super.getBoard().disableCastleBlackKing();
            super.getBoard().disableCastleBlackQueen();
        }

    }
}
