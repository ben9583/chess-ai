package com.ben9583.chess_ai.gfx.components;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import com.ben9583.chess_ai.components.pieces.Piece;
import com.ben9583.chess_ai.utils.Vector2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Graphical component of the board. This class is only present
 * when ChessAI.graphicsEnabled is true, i.e., when the flag
 * '--graphics-enabled' is passed into the main method.
 *
 * Does not perform any of the functionality of the chess game
 * but instead an interface for the user to make moves and
 * play the game.
 */
public class Board2D extends JComponent {
    /* Number of columns the board has. */
    public final int COLUMNS;
    /* Number of rows the board has. */
    public final int ROWS;

    /* The chess board corresponding to this GUI. */
    private final Board board;
    /*
    * Map that connects a piece (given by its name) to an image.
    * Exists so that we only have to load each image once.
    */
    private final Map<String, BufferedImage> images;
    /* Whether to display the promotion UI. */
    private boolean promotionPrompt = false;

    /* Piece that the user last clicked. */
    private Piece clickedPiece = null;
    /* List of squares the clickedPiece can move. */
    private final java.util.List<Vector2> squarePositions = new ArrayList<>();
    /* List of coordinates where we should draw the possible move circles. */
    private final java.util.List<Vector2> circlePositions = new ArrayList<>();

    /* x-offset for the board. */
    public static final int X_OFFSET = 200;
    /* y-offset for the board */
    public static final int Y_OFFSET = 75;
    /* Width of the square. */
    public static final int SQUARE_WIDTH = 100;
    /* Height of the square. */
    public static final int SQUARE_HEIGHT = 100;

    /* Color corresponding to the dark squares. */
    public static final Color DARK_COLOR = new Color(119, 73, 44);
    /* Color corresponding to the light squares. */
    public static final Color LIGHT_COLOR = new Color(190, 157, 119);
    /* Color corresponding to the dark squares when clicked. */
    public static final Color DARK_RED_COLOR = new Color(119, 44, 44);
    /* Color corresponding to the light squares when clicked. */
    public static final Color LIGHT_RED_COLOR = new Color(190, 119, 119);
    /* Array of colors to make math easier (access the i % 2 color for each square). */
    private static final Color[] COLORS = { Board2D.DARK_COLOR, Board2D.LIGHT_COLOR, Board2D.DARK_RED_COLOR, Board2D.LIGHT_RED_COLOR };

    /* Color corresponding to the circle that shows a possible move. */
    private static final Color GRAY = new Color(75, 75, 75, 89);

    /**
     * Converts pixel coordinates to square positions on the board.
     * @param coords Pixel coordinates
     * @return Position on the board
     */
    public Vector2 screenCoordsToSquare(Vector2 coords) {
        return new Vector2((coords.getX() - X_OFFSET)/SQUARE_WIDTH, -((coords.getY() - Y_OFFSET)/SQUARE_HEIGHT + 1 - this.COLUMNS));
    }

    /**
     * Converts a square position on the board to the pixel coordinates where it is drawn.
     * @param square Position on the board
     * @return Pixel coordinates
     */
    public Vector2 squareToScreenCoords(Vector2 square) {
        return new Vector2((square.getX() * SQUARE_WIDTH) + X_OFFSET, ((this.COLUMNS - square.getY() - 1) * SQUARE_HEIGHT) + Y_OFFSET);
    }

    /**
     * Helper class that provides listening for mouse clicks.
     */
    public class ChessAIMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(board.isGameOver()) return;

            Vector2 coords = new Vector2(e.getX(), e.getY());
            Vector2 square = screenCoordsToSquare(coords);

            boolean moveThePiece = false;

            if(!promotionPrompt && clickedPiece != null && squarePositions.contains(square) && clickedPiece.getPlayer().equals(board.getWhoseTurn())) {
                board.setClicked(null);
                moveThePiece = true;
            } else if(promotionPrompt && square.getX() == COLUMNS + 1) {
                switch (square.getY()) {
                    case 7 -> {
                        board.promote("Knight");
                        promotionPrompt = false;
                    }
                    case 6 -> {
                        board.promote("Bishop");
                        promotionPrompt = false;
                    }
                    case 5 -> {
                        board.promote("Rook");
                        promotionPrompt = false;
                    }
                    case 4 -> {
                        board.promote("Queen");
                        promotionPrompt = false;
                    }
                    case 3 -> {
                        board.promote("Knook");
                        promotionPrompt = false;
                    }
                }
            } else if(!promotionPrompt) {
                board.setClicked(square);
            }

            if(board.awaitingPromotion()) {
                promotionPrompt = true;
            }

            squarePositions.clear();
            circlePositions.clear();

            if(moveThePiece) {
                clickedPiece.movePiece(square);
            }

            clickedPiece = null;

            repaint();
        }
    }

    /**
     * Constructs a graphical interface from a chess board.
     * @param columns Number of columns to draw
     * @param rows Number of rows to draw
     * @param board The chess board this UI element will interface with.
     */
    public Board2D(int columns, int rows, Board board) {
        super();

        this.COLUMNS = columns;
        this.ROWS = rows;

        this.board = board;
        this.images = new HashMap<>();

        for(int i = 0; i < this.ROWS; i++) {
            for(int j = 0; j < this.COLUMNS; j++) {
                Piece p = this.board.getPieceAtPosition(new Vector2(j, i));
                if(p != null) {
                    try {
                        this.images.put(p.toString(), ImageIO.read(new File(p.getIconPath())));
                    } catch(IOException e) {
                        this.images.put(p.toString(), null);
                    }
                }
            }
        }

        this.addMouseListener(new ChessAIMouseListener());
    }

    @Override
    public void paintComponent(Graphics g) {
        if(!(g instanceof Graphics2D g2)) return;

        for(int i = 0; i < this.ROWS; i++) {
            for(int j = 0; j < this.COLUMNS; j++) {
                Vector2 pos = new Vector2(i, j);
                Piece piece = this.board.getPieceAtPosition(pos);

                Vector2 screenPos = this.squareToScreenCoords(new Vector2(i, j));

                boolean thisPieceClicked;
                if(this.board.getClicked() != null && this.board.getClicked().equals(pos)) {
                    g2.setColor(COLORS[(i + j) % 2 + 2]);
                    thisPieceClicked = true;
                } else {
                    g2.setColor(COLORS[(i + j) % 2]);
                    thisPieceClicked = false;
                }

                g2.fillRect(screenPos.getX(), screenPos.getY(), SQUARE_WIDTH, SQUARE_HEIGHT);

                if(piece != null) {
                    BufferedImage img = this.images.get(piece.toString());
                    g2.drawImage(img, screenPos.getX(), screenPos.getY(), null);
                    if(thisPieceClicked && !piece.equals(this.clickedPiece) && piece.getPlayer().equals(this.board.getWhoseTurn())) {
                        this.clickedPiece = piece;
                        this.circlePositions.clear();
                        this.squarePositions.clear();
                        Vector2[] movableSquares = piece.getMovableSquares(true);
                        for(Vector2 square : movableSquares) {
                            Vector2 coords = this.squareToScreenCoords(square);
                            this.squarePositions.add(square);
                            this.circlePositions.add(coords);
                        }
                    }
                }
            }
        }

        if(this.clickedPiece != null) {
            g2.setColor(Board2D.GRAY);
            for(Vector2 coords : this.circlePositions) {
                g2.fillOval(coords.getX() + Board2D.SQUARE_WIDTH / 2 - 15, coords.getY() + Board2D.SQUARE_HEIGHT / 2 - 15, 30, 30);
            }
        }

        if(promotionPrompt) {
            String whoseTurn = this.board.getWhoseTurn().equals(Player.WHITE) ? "White" : "Black";
            int x = X_OFFSET + SQUARE_WIDTH * (this.COLUMNS + 1);
            int y = Y_OFFSET;
            g2.setColor(Board2D.DARK_COLOR);
            g2.fillRect(x, y, SQUARE_WIDTH, SQUARE_HEIGHT);
            g2.drawImage(images.get(whoseTurn + "Knight"), x, y, null);

            y += SQUARE_HEIGHT;
            g2.setColor(Board2D.LIGHT_COLOR);
            g2.fillRect(x, y, SQUARE_WIDTH, SQUARE_HEIGHT);
            g2.drawImage(images.get(whoseTurn + "Bishop"), x, y, null);

            y += SQUARE_HEIGHT;
            g2.setColor(Board2D.DARK_COLOR);
            g2.fillRect(x, y, SQUARE_WIDTH, SQUARE_HEIGHT);
            g2.drawImage(images.get(whoseTurn + "Rook"), x, y, null);

            y += SQUARE_HEIGHT;
            g2.setColor(Board2D.LIGHT_COLOR);
            g2.fillRect(x, y, SQUARE_WIDTH, SQUARE_HEIGHT);
            g2.drawImage(images.get(whoseTurn + "Queen"), x, y, null);

            y += SQUARE_HEIGHT;
            g2.setColor(Board2D.DARK_COLOR);
            g2.fillRect(x, y, SQUARE_WIDTH, SQUARE_HEIGHT);
            g2.drawImage(images.get(whoseTurn + "Knook"), x, y, null);
        }

        if(this.board.isGameOver()) {
            g2.setColor(Color.WHITE);
            g2.drawString(this.board.getGameOverReason(), X_OFFSET + SQUARE_WIDTH * (this.COLUMNS + 1), Y_OFFSET + SQUARE_HEIGHT * this.ROWS);
        }
    }
}
