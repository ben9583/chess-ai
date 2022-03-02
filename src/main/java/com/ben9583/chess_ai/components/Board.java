package com.ben9583.chess_ai.components;

import com.ben9583.chess_ai.components.pieces.*;
import org.jetbrains.annotations.Nullable;

public class Board {
    private static final Piece[][] DEFAULT_BOARD = {
            { new Rook(Player.WHITE), new Knight(Player.WHITE), new Bishop(Player.WHITE), new Queen(Player.WHITE), new King(Player.WHITE), new Bishop(Player.WHITE), new Knight(Player.WHITE), new Rook(Player.WHITE) },
            { new Pawn(Player.WHITE), new Pawn(Player.WHITE), new Pawn(Player.WHITE), new Pawn(Player.WHITE), new Pawn(Player.WHITE), new Pawn(Player.WHITE), new Pawn(Player.WHITE), new Pawn(Player.WHITE) },
            { null, null, null, null, null, null, null, null },
            { null, null, null, null, null, null, null, null },
            { null, null, null, null, null, null, null, null },
            { null, null, null, null, null, null, null, null },
            { new Pawn(Player.BLACK), new Pawn(Player.BLACK), new Pawn(Player.BLACK), new Pawn(Player.BLACK), new Pawn(Player.BLACK), new Pawn(Player.BLACK), new Pawn(Player.BLACK), new Pawn(Player.BLACK) },
            { new Rook(Player.BLACK), new Knight(Player.BLACK), new Bishop(Player.BLACK), new Queen(Player.BLACK), new King(Player.BLACK), new Bishop(Player.BLACK), new Knight(Player.BLACK), new Rook(Player.BLACK) }
    };

    private final Piece[][] board;

    public Board() {
        this.board = new Piece[][]{
                { new Rook(Player.WHITE), new Knight(Player.WHITE), new Bishop(Player.WHITE), new Queen(Player.WHITE), new King(Player.WHITE), new Bishop(Player.WHITE), new Knight(Player.WHITE), new Rook(Player.WHITE) },
                { new Pawn(Player.WHITE), new Pawn(Player.WHITE), new Pawn(Player.WHITE), new Pawn(Player.WHITE), new Pawn(Player.WHITE), new Pawn(Player.WHITE), new Pawn(Player.WHITE), new Pawn(Player.WHITE) },
                { null, null, null, null, null, null, null, null },
                { null, null, null, null, null, null, null, null },
                { null, null, null, null, null, null, null, null },
                { null, null, null, null, null, null, null, null },
                { new Pawn(Player.BLACK), new Pawn(Player.BLACK), new Pawn(Player.BLACK), new Pawn(Player.BLACK), new Pawn(Player.BLACK), new Pawn(Player.BLACK), new Pawn(Player.BLACK), new Pawn(Player.BLACK) },
                { new Rook(Player.BLACK), new Knight(Player.BLACK), new Bishop(Player.BLACK), new Queen(Player.BLACK), new King(Player.BLACK), new Bishop(Player.BLACK), new Knight(Player.BLACK), new Rook(Player.BLACK) }
        };
    }

    public Board(Piece[][] board) {
        this.board = board;
    }
}
