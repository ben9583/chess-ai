package com.ben9583.chess_ai.components;

import com.ben9583.chess_ai.components.pieces.*;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Board {
    private final Piece[][] board;
    private final Map<Piece, Vector2> pieces;
    private Player whoseTurn;
    private boolean castleWhiteKing;
    private boolean castleWhiteQueen;
    private boolean castleBlackKing;
    private boolean castleBlackQueen;
    private Vector2 enPassantPosition = null;
    private int halfMoveClock = 0;
    private int fullMoveNumber = 0;

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

        this.pieces = new HashMap<>();

        for(int i = 0; i < this.board.length; i++) {
            for(int j = 0; j < this.board[i].length; j++) {
                if(this.board[i][j] != null) {
                    this.pieces.put(this.board[i][j], new Vector2(j, i));
                }
            }
        }

        this.whoseTurn = Player.WHITE;
        this.castleWhiteKing = true;
        this.castleWhiteQueen = true;
        this.castleBlackKing = true;
        this.castleBlackQueen = true;
    }

    public Board(Piece[][] board) {
        this.board = board;
        this.pieces = new HashMap<>();

        for(int i = 0; i < this.board.length; i++) {
            for(int j = 0; j < this.board[i].length; j++) {
                if(this.board[i][j] != null) {
                    this.pieces.put(this.board[i][j], new Vector2(j, i));
                }
            }
        }

        this.whoseTurn = Player.WHITE;
        this.castleWhiteKing = true;
        this.castleWhiteQueen = true;
        this.castleBlackKing = true;
        this.castleBlackQueen = true;
    }

    public boolean boardExistsAt(Vector2 location) {
        return location.getY() < this.board.length && location.getX() < this.board[location.getY()].length;
    }

    @Nullable
    public Piece getPieceAtPosition(Vector2 location) {
        return this.board[location.getY()][location.getX()];
    }

    @Nullable
    private Piece setPosition(Piece piece, Vector2 location) {
        Vector2 start = this.getPosition(piece);
        Piece pieceAtLocation = this.getPieceAtPosition(location);

        this.board[location.getY()][location.getX()] = piece;
        this.board[start.getY()][start.getX()] = null;

        this.pieces.put(piece, location);

        return pieceAtLocation;
    }

    @NotNull
    public Vector2 getPosition(Piece piece) {
        Vector2 location = this.pieces.getOrDefault(piece, null);
        if(location == null) throw new IllegalArgumentException("Piece " + piece + " was not found in this board.");

        return location;
    }

    private void nextTurn() {
        if(this.whoseTurn.equals(Player.WHITE)) {
            this.whoseTurn = Player.BLACK;
        } else {
            this.whoseTurn = Player.WHITE;
            this.fullMoveNumber++;
        }
        this.halfMoveClock++;
    }

    public void movePiece(Piece piece, Vector2 end) {
        if(!piece.getPlayer().equals(this.whoseTurn)) throw new IllegalArgumentException("It's Player " + this.whoseTurn + "'s turn, but a piece that tried to move belongs to player " + piece.getPlayer() + ".");

        Piece removedPiece = this.setPosition(piece, end);
        this.nextTurn();
    }

    @NotNull
    public Piece removePiece(Vector2 position) {
        Piece target = this.getPieceAtPosition(position);
        if(target == null) throw new IllegalArgumentException("Tried to remove piece at " + position + ", but nothing was there.");

        this.pieces.remove(target);
        this.board[position.getY()][position.getX()] = null;
        return target;
    }

    @NotNull
    public Vector2 removePiece(Piece target) {
        Vector2 position = this.getPosition(target);
        if(target == null) throw new IllegalArgumentException("Tried to remove piece at " + position + ", but nothing was there.");

        this.pieces.remove(target);
        this.board[position.getY()][position.getX()] = null;
        return position;
    }

    public boolean isCastleWhiteKing() {
        return this.castleWhiteKing;
    }

    public boolean isCastleWhiteQueen() {
        return this.castleWhiteQueen;
    }

    public boolean isCastleBlackKing() {
        return this.castleBlackKing;
    }

    public boolean isCastleBlackQueen() {
        return this.castleBlackQueen;
    }

    @Nullable
    public Vector2 getEnPassantPosition() {
        return this.enPassantPosition;
    }

    public void setEnPassantPosition(@Nullable Vector2 position) {
        this.enPassantPosition = position;
    }
}
