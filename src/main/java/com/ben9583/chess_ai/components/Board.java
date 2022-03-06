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

    /* 2D array representing all the pieces belonging to this board at (x, y). */
    private final Piece[][] board;
    /* Lookup table to make finding pieces easier. */
    @NotNull
    private final Map<Piece, Vector2> pieces;

    /* Whose turn it is in the game. */
    @NotNull
    private Player whoseTurn;

    /* Whether castling is possible for each player and each side. */
    private boolean castleWhiteKing;
    private boolean castleWhiteQueen;
    private boolean castleBlackKing;
    private boolean castleBlackQueen;

    /* The Vector2 corresponding to the pawn awaiting a decision on promotion. Null if no piece to promote. */
    @Nullable
    private Vector2 awaitPromotion = null;

    /* The Vector2 corresponding to the position at which en passant can take place, following FEN notation. */
    @Nullable
    private Vector2 enPassantPosition = null;

    /* Number of half-moves since a capture or pawn move. Draw at 50. */
    private int halfMoveClock = 0;
    /* Number of full-moves occurred since this game started. */
    private int fullMoveNumber = 0;

    /* Number of times a given position has been reached. Two positions are equal by rules of threefold repetition. */
    private final Map<String, Integer> reachedPositions = new HashMap<>();

    /* Whether the game is over because of checkmate or stalemate. */
    private boolean gameOver = false;
    /* Message for why the game ended. */
    private String gameOverReason = null;

    /* Whether the next move should increment the turn. Used for validating checks. */
    private boolean disableMovementThisTurn = false;

    /* The piece the user clicked. See also: Board2D */
    private Vector2 clicked = null;

    /**
     * Creates a standard chess board.
     */
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

    /**
     * Returns a string hash of this board.
     * Two hashes are equal if they would cause a threefold repetition.
     * @return A hash of this board
     */
    @NotNull
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

    /**
     * Returns whether there is a square at location on this board.
     * @param location A potential square this board exists at
     * @return Whether location is a square on this board
     */
    public boolean boardExistsAt(@NotNull Vector2 location) {
        return location.getY() >= 0 && location.getX() >= 0 && location.getY() < this.board.length && location.getX() < this.board[location.getY()].length;
    }

    /**
     * Returns the piece at location or null if none there.
     * @param location A square on the board
     * @return The piece at location or null if none there
     */
    @Nullable
    public Piece getPieceAtPosition(Vector2 location) {
        return this.board[location.getY()][location.getX()];
    }

    /**
     * Sets the position of piece to location on this board and returns the piece displaced by this action.
     * @param piece A piece belonging to this board
     * @param location The square to move this piece to
     * @return The piece replaced from this position. Position is null, but still belongs to this board
     */
    @Nullable
    private Piece setPosition(@NotNull Piece piece, @NotNull Vector2 location) {
        Vector2 start = this.getPosition(piece);
        Piece pieceAtLocation = this.getPieceAtPosition(location);

        assert piece != pieceAtLocation;

        this.board[location.getY()][location.getX()] = piece;
        this.board[start.getY()][start.getX()] = null;

        this.pieces.put(piece, location);
        this.pieces.remove(pieceAtLocation);

        return pieceAtLocation;
    }

    /**
     * Gets the location of piece on this board.
     * Throws an exception if the piece is not on this board.
     * @param piece A piece on this board
     * @return the position of piece
     */
    @NotNull
    public Vector2 getPosition(@NotNull Piece piece) {
        Vector2 location = this.pieces.getOrDefault(piece, null);
        if(location == null) throw new IllegalArgumentException("Piece " + piece + " was not found on the board.");

        return location;
    }

    /**
     * Function that changes whose turn it is.
     * Also checks for game-ending conditions like checkmate or stalemate.
     */
    private void nextTurn() {
        if(this.whoseTurn.equals(Player.WHITE)) {
            this.whoseTurn = Player.BLACK;
        } else {
            this.whoseTurn = Player.WHITE;
            this.fullMoveNumber++;
        }
        this.halfMoveClock++;

        if(this.isCheckmate(this.whoseTurn)) {
            System.out.println("Checkmate! " + (this.whoseTurn.equals(Player.WHITE) ? "Black" : "White") + " wins.");
            this.gameOver = true;
            this.gameOverReason = "Checkmate! " + (this.whoseTurn.equals(Player.WHITE) ? "Black" : "White") + " wins.";
            return;
        }
        if(this.halfMoveClock == 50) {
            System.out.println("Draw by 50-move rule.");
            this.gameOver = true;
            this.gameOverReason = "Draw by 50-move rule.";
        }
    }

    /**
     * Moves piece to end and increments the turn if necessary.
     * Also checks for threefold reptition draws.
     * @param piece Piece to move
     * @param end Position to move piece to
     */
    public void movePiece(@NotNull Piece piece, @NotNull Vector2 end) {
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
        if(reachedTimes == 3) {
            System.out.println("Draw by threefold repetition.");
            this.gameOver = true;
            this.gameOverReason = "Draw by threefold repetition.";
        }

        if(this.awaitPromotion == null) this.nextTurn();
    }

    /**
     * Removes the piece at position.
     * Position will be null, but board will still be this.
     * @param position Square to remove the piece from
     * @return The piece at position that was just removed
     */
    @NotNull
    public Piece removePiece(@NotNull Vector2 position) {
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

    /**
     * Removes target from its square.
     * Position will be null, but board will still be this.
     * @param target Piece to remove from its position
     * @return The location this piece was previously at
     */
    @NotNull
    public Vector2 removePiece(Piece target) {
        Vector2 position = this.getPosition(target);

        this.pieces.remove(target);
        this.board[position.getY()][position.getX()] = null;
        return position;
    }

    /**
     * Returns whether target is currently in check.
     * @param target Player to be checked for check
     * @return Whether target is in check
     */
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

    /**
     * Returns whether moving movingPiece to end puts the respective player in check.
     * @param movingPiece Piece to move
     * @param end Square to move movingPiece to
     * @return Whether the player that owns movingPiece is in check after moving it to end
     */
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

    /**
     * Returns whether player is checkmated.
     * @param player Player to check if checkmated
     * @return Whether player is checkmated
     */
    public boolean isCheckmate(Player player) {
        for(Piece p : this.pieces.keySet().toArray(new Piece[0])) {
            if(p.getPlayer().equals(player)) {
                Vector2[] movableSquares = p.getMovableSquares(true);
                if(movableSquares.length > 0) return false;
            }
        }

        return true;
    }

    /**
     * Returns whose turn it is.
     * @return The player whose turn it is
     */
    @NotNull
    public Player getWhoseTurn() {
        return this.whoseTurn;
    }

    /**
     * Returns whether the White player can castle king-side.
     * @return Whether the White player can castle king-side
     */
    public boolean canCastleWhiteKing() {
        return this.castleWhiteKing;
    }

    /**
     * Returns whether the White player can castle queen-side.
     * @return Whether the White player can castle queen-side
     */
    public boolean canCastleWhiteQueen() {
        return this.castleWhiteQueen;
    }

    /**
     * Returns whether the Black player can castle king-side.
     * @return Whether the Black player can castle king-side
     */
    public boolean canCastleBlackKing() {
        return this.castleBlackKing;
    }

    /**
     * Returns whether the Black player can castle queen-side.
     * @return Whether the Black player can castle queen-side
     */
    public boolean canCastleBlackQueen() {
        return this.castleBlackQueen;
    }

    /**
     * Prevents the White player from castling king-side if possible.
     */
    public void disableCastleWhiteKing() { this.castleWhiteKing = false; }

    /**
     * Prevents the White player from castling queen-side if possible.
     */
    public void disableCastleWhiteQueen() { this.castleWhiteQueen = false; }

    /**
     * Prevents the Black player from castling king-side if possible.
     */
    public void disableCastleBlackKing() { this.castleBlackKing = false; }

    /**
     * Prevents the Black player from castling queen-side if possible.
     */
    public void disableCastleBlackQueen() { this.castleBlackQueen = false; }

    /**
     * Allows the White player to castle king-side if possible. Exists to allow undoing castling.
     */
    public void enableCastleWhiteKing() { this.castleWhiteKing = true; }

    /**
     * Allows the White player to castle queen-side if possible. Exists to allow undoing castling.
     */
    public void enableCastleWhiteQueen() { this.castleWhiteQueen = true; }

    /**
     * Allows the Black player to castle king-side if possible. Exists to allow undoing castling.
     */
    public void enableCastleBlackKing() { this.castleBlackKing = true; }

    /**
     * Allows the Black player to castle queen-side if possible. Exists to allow undoing castling.
     */
    public void enableCastleBlackQueen() { this.castleBlackQueen = true; }

    /**
     * Castles the White Player king-side by moving the king and rook.
     * @param king The king to castle
     */
    public void castleWhiteKing(King king) {
        this.castle(king, king.getPosition().add(new Vector2(2, 0)), Objects.requireNonNull(this.getPieceAtPosition(king.getPosition().add(new Vector2(3, 0)))), king.getPosition().add(Vector2.EAST));
        this.castleWhiteKing = false;
    }

    /**
     * Castles the White Player queen-side by moving the king and rook.
     * @param king The king to castle
     */
    public void castleWhiteQueen(King king) {
        this.castle(king, king.getPosition().add(new Vector2(-2, 0)), Objects.requireNonNull(this.getPieceAtPosition(king.getPosition().add(new Vector2(-4, 0)))), king.getPosition().add(Vector2.WEST));
        this.castleWhiteQueen = false;
    }

    /**
     * Castles the Black Player king-side by moving the king and rook.
     * @param king The king to castle
     */
    public void castleBlackKing(King king) {
        this.castle(king, king.getPosition().add(new Vector2(2, 0)), Objects.requireNonNull(this.getPieceAtPosition(king.getPosition().add(new Vector2(3, 0)))), king.getPosition().add(Vector2.EAST));
        this.castleBlackKing = false;
    }

    /**
     * Castles the Black Player queen-side by moving the king and rook.
     * @param king The king to castle
     */
    public void castleBlackQueen(King king) {
        this.castle(king, king.getPosition().add(new Vector2(-2, 0)), Objects.requireNonNull(this.getPieceAtPosition(king.getPosition().add(new Vector2(-4, 0)))), king.getPosition().add(Vector2.WEST));
        this.castleBlackQueen = false;
    }

    /**
     * Un-castles the White Player king-side by moving the king and rook. Exists to allow undoing castling.
     * @param king The king to un-castle
     */
    public void uncastleWhiteKing(King king) {
        this.castle(king, king.getPosition().add(new Vector2(-2, 0)), Objects.requireNonNull(this.getPieceAtPosition(king.getPosition().add(new Vector2(-2, 0)))), king.getPosition().add(Vector2.EAST));
        this.castleWhiteKing = true;
    }

    /**
     * Un-castles the White Player queen-side by moving the king and rook. Exists to allow undoing castling.
     * @param king The king to un-castle
     */
    public void uncastleWhiteQueen(King king) {
        this.castle(king, king.getPosition().add(new Vector2(2, 0)), Objects.requireNonNull(this.getPieceAtPosition(king.getPosition().add(Vector2.EAST))), king.getPosition().add(new Vector2(-2, 0)));
        this.castleWhiteQueen = true;
    }

    /**
     * Un-castles the Black Player king-side by moving the king and rook. Exists to allow undoing castling.
     * @param king The king to un-castle
     */
    public void uncastleBlackKing(King king) {
        this.castle(king, king.getPosition().add(new Vector2(-2, 0)), Objects.requireNonNull(this.getPieceAtPosition(king.getPosition().add(new Vector2(-2, 0)))), king.getPosition().add(Vector2.EAST));
        this.castleBlackKing = true;
    }

    /**
     * Un-castles the Black Player queen-side by moving the king and rook. Exists to allow undoing castling.
     * @param king The king to un-castle
     */
    public void uncastleBlackQueen(King king) {
        this.castle(king, king.getPosition().add(new Vector2(2, 0)), Objects.requireNonNull(this.getPieceAtPosition(king.getPosition().add(Vector2.EAST))), king.getPosition().add(new Vector2(-2, 0)));
        this.castleBlackQueen = true;
    }

    /**
     * Castles by moving king to kingToWhere and rook to rookToWhere.
     * This is also used for un-castling.
     * @param king King to move
     * @param kingToWhere Location to move the king
     * @param rook Rook to move
     * @param rookToWhere Location to move the rook
     */
    private void castle(Piece king, Vector2 kingToWhere, Piece rook, Vector2 rookToWhere) {
        assert king instanceof King;
        assert rook instanceof Rook;

        this.removePiece(king);
        this.removePiece(rook);

        this.placePiece(king, kingToWhere);
        this.placePiece(rook, rookToWhere);

        this.disableMovementThisTurn = true;
    }

    /**
     * Marks the game as 'awaiting promotion' by disabling movement
     * until the player whose turn it is promotes.
     *
     * See also: promote
     * @param square Square of the promoting pawn
     */
    public void awaitPromotion(Vector2 square) {
        this.awaitPromotion = square;
    }

    /**
     * Returns whether the game is currently awaiting
     * the user to decide what piece to promote to.
     * @return Whether the game is awaiting promotion
     */
    public boolean awaitingPromotion() {
        return this.awaitPromotion != null;
    }

    /**
     * Promotes the pawn at promotion square to piece,
     * a String that corresponds to the name of a piece that
     * will be created to replace the pawn.
     * @param piece Name of piece to promote to
     */
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

    /**
     * Returns the square corresponding to FEN en passant.
     * Is null if no pawn was moved two squares the previous turn.
     * @return The position where an opponent pawn can attack as en passant
     */
    @Nullable
    public Vector2 getEnPassantPosition() {
        return this.enPassantPosition;
    }

    /**
     * Sets the FEN position where en passant can take place.
     * If a pawn have moved two squares, it should be the square it skipped.
     * This should be null in any other case.
     * @param position FEN position where en passant can take place
     */
    public void setEnPassantPosition(@Nullable Vector2 position) {
        this.enPassantPosition = position;
    }

    /**
     * Resets the half move clock counter.
     * Runs when a pawn is moved or a piece is captured.
     * When it reaches 50 half-moves, the game is drawn.
     */
    public void resetHalfMoveClock() {
        this.halfMoveClock = 0;
    }

    /**
     * Returns whether the game has been stopped because
     * either checkmate or stalemate has been reached.
     * @return Whether the game is over
     */
    public boolean isGameOver() {
        return this.gameOver;
    }

    /**
     * Gets the square clicked in a graphical interface
     * using this board.
     * @return Square the user clicked
     */
    public Vector2 getClicked() {
        return this.clicked;
    }

    /**
     * Gets the reason the game is over.
     * Throws an exception if the game is not over.
     * @return The reason the game is over
     */
    @NotNull
    public String getGameOverReason() {
        if(this.gameOverReason == null) throw new IllegalStateException("Cannot get the reason the game is over because the game is not over.");
        return this.gameOverReason;
    }

    /**
     * Sets the square clicked in a graphical interface.
     * This should when the user clicks a square
     * @param square Square the user clicked
     */
    public void setClicked(Vector2 square) {
        this.clicked = square;
    }
}
