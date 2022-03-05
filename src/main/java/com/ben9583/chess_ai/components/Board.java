package com.ben9583.chess_ai.components;

import com.ben9583.chess_ai.components.pieces.*;
import com.ben9583.chess_ai.utils.Vector2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Board {

    private final Piece[][] board;
    private final Map<Piece, Vector2> pieces;

    private Player whoseTurn;

    private boolean castleWhiteKing;
    private boolean castleWhiteQueen;
    private boolean castleBlackKing;
    private boolean castleBlackQueen;

    private Vector2 awaitPromotion = null;

    private Vector2 enPassantPosition = null;

    private int halfMoveClock = 0;

    private final Map<String, Integer> reachedPositions = new HashMap<>();
    private int fullMoveNumber = 0;

    private boolean disableMovementThisTurn = false;

    private Vector2 clicked = null;

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

    private String getBoardHash() {
        /*
        From Wikipedia:

        Two positions are by definition "the same" if the same types of pieces occupy the same squares,
        the same player has the move, the remaining castling rights are the same and
        the possibility to capture en passant is the same.
        */

        return (
            Arrays.deepHashCode(this.board) +
            this.whoseTurn.toString() +
            this.castleWhiteKing +
            this.castleWhiteQueen +
            this.castleBlackKing +
            this.castleBlackQueen +
            this.enPassantPosition
        );
    }

    public boolean boardExistsAt(Vector2 location) {
        return location.getY() >= 0 && location.getX() >= 0 && location.getY() < this.board.length && location.getX() < this.board[location.getY()].length;
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
        this.pieces.remove(pieceAtLocation);

        return pieceAtLocation;
    }

    @NotNull
    public Vector2 getPosition(Piece piece) {
        Vector2 location = this.pieces.getOrDefault(piece, null);
        if(location == null) throw new IllegalArgumentException("Piece " + piece + " was not found on the board.");

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

        if(this.isCheckmate(this.whoseTurn)) System.out.println("Checkmate! " + (this.whoseTurn.equals(Player.WHITE) ? "Black" : "White") + " wins.");
        if(this.halfMoveClock == 50) System.out.println("Draw by 50-move rule.");
    }

    public void movePiece(Piece piece, Vector2 end) {
        if(!piece.getPlayer().equals(this.whoseTurn)) throw new IllegalArgumentException("It's Player " + this.whoseTurn + "'s turn, but a piece that tried to move belongs to player " + piece.getPlayer() + ".");

        if(!this.disableMovementThisTurn) {
            Piece removedPiece = this.setPosition(piece, end);
            if(removedPiece != null) this.resetHalfMoveClock();
        } else {
            this.disableMovementThisTurn = false;
        }

        String boardHash = this.getBoardHash();
        int reachedTimes = this.reachedPositions.getOrDefault(boardHash, 0) + 1;
        this.reachedPositions.put(boardHash, reachedTimes);
        if(reachedTimes == 3) System.out.println("Draw by threefold repetition.");

        if(this.awaitPromotion == null) this.nextTurn();
    }

    @NotNull
    public Piece removePiece(Vector2 position) {
        Piece target = this.getPieceAtPosition(position);
        if(target == null) throw new IllegalArgumentException("Tried to remove piece at " + position + ", but nothing was there.");

        this.pieces.remove(target);
        this.board[position.getY()][position.getX()] = null;
        return target;
    }

    public void placePiece(@NotNull Piece piece, Vector2 position) {
        if(this.getPieceAtPosition(position) != null) throw new IllegalArgumentException("Tried to insert " + piece + " at " + position + ", but " + this.getPieceAtPosition(position) + " was already there.");
        this.board[position.getY()][position.getX()] = piece;
        this.pieces.put(piece, position);
    }

    @NotNull
    public Vector2 removePiece(Piece target) {
        Vector2 position = this.getPosition(target);

        this.pieces.remove(target);
        this.board[position.getY()][position.getX()] = null;
        return position;
    }

    public boolean isInCheck(Player target) {
        Player attacker;
        if(target.equals(Player.WHITE))
            attacker = Player.BLACK;
        else
            attacker = Player.WHITE;

        for(Piece p : this.pieces.keySet().toArray(new Piece[0])) {
            if(p.getPlayer().equals(attacker) && !(p instanceof King)) {
                Vector2[] attackingSquares = p.getMovableSquares(false);
                for(Vector2 square : attackingSquares) {
                    Piece attackedPiece = this.getPieceAtPosition(square);
                    if(attackedPiece instanceof King && attackedPiece.getPlayer().equals(target)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean doesThisMovePutMeInCheck(Piece movingPiece, Vector2 end) {
        Vector2 start = this.getPosition(movingPiece);

        assert this.getPieceAtPosition(end) == null || !this.getPieceAtPosition(end).getPlayer().equals(movingPiece.getPlayer());
        Piece attackedPiece = this.setPosition(movingPiece, end);

        boolean inCheck = this.isInCheck(this.whoseTurn);

        Piece thereBetterBeNothingHere = this.setPosition(movingPiece, start);
        assert thereBetterBeNothingHere == null;

        if(attackedPiece != null) this.placePiece(attackedPiece, end);

        return inCheck;
    }

    public boolean isCheckmate(Player player) {
        for(Piece p : this.pieces.keySet().toArray(new Piece[0])) {
            if(p.getPlayer().equals(player)) {
                Vector2[] movableSquares = p.getMovableSquares(true);
                if(movableSquares.length > 0) return false;
            }
        }

        return true;
    }

    public Player getWhoseTurn() {
        return this.whoseTurn;
    }

    public boolean canCastleWhiteKing() {
        return this.castleWhiteKing;
    }

    public boolean canCastleWhiteQueen() {
        return this.castleWhiteQueen;
    }

    public boolean canCastleBlackKing() {
        return this.castleBlackKing;
    }

    public boolean canCastleBlackQueen() {
        return this.castleBlackQueen;
    }

    public void disableCastleWhiteKing() { this.castleWhiteKing = false; }

    public void disableCastleWhiteQueen() { this.castleWhiteQueen = false; }

    public void disableCastleBlackKing() { this.castleBlackKing = false; }

    public void disableCastleBlackQueen() { this.castleBlackQueen = false; }

    public void enableCastleWhiteKing() { this.castleWhiteKing = true; }

    public void enableCastleWhiteQueen() { this.castleWhiteQueen = true; }

    public void enableCastleBlackKing() { this.castleBlackKing = true; }

    public void enableCastleBlackQueen() { this.castleBlackQueen = true; }

    public void castleWhiteKing(King king) {
        this.castle(king, king.getPosition().add(new Vector2(2, 0)), Objects.requireNonNull(this.getPieceAtPosition(king.getPosition().add(new Vector2(3, 0)))), king.getPosition().add(Vector2.EAST));
        this.castleWhiteKing = false;
    }

    public void castleWhiteQueen(King king) {
        this.castle(king, king.getPosition().add(new Vector2(-2, 0)), Objects.requireNonNull(this.getPieceAtPosition(king.getPosition().add(new Vector2(-4, 0)))), king.getPosition().add(Vector2.WEST));
        this.castleWhiteQueen = false;
    }

    public void castleBlackKing(King king) {
        this.castle(king, king.getPosition().add(new Vector2(2, 0)), Objects.requireNonNull(this.getPieceAtPosition(king.getPosition().add(new Vector2(3, 0)))), king.getPosition().add(Vector2.EAST));
        this.castleBlackKing = false;
    }

    public void castleBlackQueen(King king) {
        this.castle(king, king.getPosition().add(new Vector2(-2, 0)), Objects.requireNonNull(this.getPieceAtPosition(king.getPosition().add(new Vector2(-4, 0)))), king.getPosition().add(Vector2.WEST));
        this.castleBlackQueen = false;
    }

    public void uncastleWhiteKing(King king) {
        this.castle(king, king.getPosition().add(new Vector2(-2, 0)), Objects.requireNonNull(this.getPieceAtPosition(king.getPosition().add(new Vector2(-2, 0)))), king.getPosition().add(Vector2.EAST));
        this.castleWhiteKing = true;
    }

    public void uncastleWhiteQueen(King king) {
        this.castle(king, king.getPosition().add(new Vector2(2, 0)), Objects.requireNonNull(this.getPieceAtPosition(king.getPosition().add(Vector2.EAST))), king.getPosition().add(new Vector2(-2, 0)));
        this.castleWhiteQueen = true;
    }

    public void uncastleBlackKing(King king) {
        this.castle(king, king.getPosition().add(new Vector2(-2, 0)), Objects.requireNonNull(this.getPieceAtPosition(king.getPosition().add(new Vector2(-2, 0)))), king.getPosition().add(Vector2.EAST));
        this.castleBlackKing = true;
    }

    public void uncastleBlackQueen(King king) {
        this.castle(king, king.getPosition().add(new Vector2(2, 0)), Objects.requireNonNull(this.getPieceAtPosition(king.getPosition().add(Vector2.EAST))), king.getPosition().add(new Vector2(-2, 0)));
        this.castleBlackQueen = true;
    }

    private void castle(Piece king, Vector2 kingToWhere, Piece rook, Vector2 rookToWhere) {
        assert king instanceof King;
        assert rook instanceof Rook;

        this.removePiece(king);
        this.removePiece(rook);

        this.placePiece(king, kingToWhere);
        this.placePiece(rook, rookToWhere);

        this.disableMovementThisTurn = true;
    }

    public void awaitPromotion(Vector2 square) {
        this.awaitPromotion = square;
    }

    public boolean awaitingPromotion() {
        return this.awaitPromotion != null;
    }

    public void promote(String piece) {
        if(this.awaitPromotion == null) throw new IllegalStateException("Tried to promote to a " + piece + ", but there's nothing to promote.");

        this.removePiece(this.getPieceAtPosition(this.awaitPromotion));

        Piece p;
        switch(piece) {
            case "Knight" -> p = new Knight(this.whoseTurn, this);
            case "Bishop" -> p = new Bishop(this.whoseTurn, this);
            case "Rook" -> p = new Rook(this.whoseTurn, this);
            case "Queen" -> p = new Queen(this.whoseTurn, this);
            default -> throw new IllegalArgumentException("There's no piece called '" + piece + "'.");
        }

        this.placePiece(p, this.awaitPromotion);
        this.awaitPromotion = null;

        this.nextTurn();
    }

    @Nullable
    public Vector2 getEnPassantPosition() {
        return this.enPassantPosition;
    }

    public void setEnPassantPosition(@Nullable Vector2 position) {
        this.enPassantPosition = position;
    }

    public void resetHalfMoveClock() {
        this.halfMoveClock = 0;
    }

    public Vector2 getClicked() {
        return this.clicked;
    }

    public void setClicked(Vector2 square) {
        this.clicked = square;
    }
}
