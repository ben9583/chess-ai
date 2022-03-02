package com.ben9583.chess_ai.components;

import com.ben9583.chess_ai.components.pieces.*;

public class Board {
    private final Piece[][] board;

    public Board() {
        this.board = new Piece[][]{
                { new Rook(Player.WHITE, this), new Knight(Player.WHITE, this), new Bishop(Player.WHITE, this), new Queen(Player.WHITE, this), new King(Player.WHITE, this), new Bishop(Player.WHITE, this), new Knight(Player.WHITE, this), new Rook(Player.WHITE, this) },
                { new Pawn(Player.WHITE, this), new Pawn(Player.WHITE, this), new Pawn(Player.WHITE, this), new Pawn(Player.WHITE, this), new Pawn(Player.WHITE, this), new Pawn(Player.WHITE, this), new Pawn(Player.WHITE, this), new Pawn(Player.WHITE, this) },
                { null, null, null, null, null, null, null, null },
                { null, null, null, null, null, null, null, null },
                { null, null, null, null, null, null, null, null },
                { null, null, null, null, null, null, null, null },
                { new Pawn(Player.BLACK, this), new Pawn(Player.BLACK, this), new Pawn(Player.BLACK, this), new Pawn(Player.BLACK, this), new Pawn(Player.BLACK, this), new Pawn(Player.BLACK, this), new Pawn(Player.BLACK, this), new Pawn(Player.BLACK, this) },
                { new Rook(Player.BLACK, this), new Knight(Player.BLACK, this), new Bishop(Player.BLACK, this), new Queen(Player.BLACK, this), new King(Player.BLACK, this), new Bishop(Player.BLACK, this), new Knight(Player.BLACK, this), new Rook(Player.BLACK, this) }
        };
    }

    public Board(Piece[][] board) {
        this.board = board;
    }
}
