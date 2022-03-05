package com.ben9583.chess_ai.gfx.components;

import com.ben9583.chess_ai.components.Board;
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

public class Board2D extends JComponent {
    public final int COLUMNS;
    public final int ROWS;

    private final Board board;
    private final Map<Piece, BufferedImage> images;

    private Piece clickedPiece = null;
    private final java.util.List<Vector2> squarePositions = new ArrayList<Vector2>();
    private final java.util.List<Vector2> circlePositions = new ArrayList<Vector2>();

    public static final int X_OFFSET = 200;
    public static final int Y_OFFSET = 75;
    public static final int SQUARE_WIDTH = 100;
    public static final int SQUARE_HEIGHT = 100;

    public static final Color DARK_COLOR = new Color(119, 73, 44);
    public static final Color LIGHT_COLOR = new Color(190, 157, 119);
    public static final Color DARK_RED_COLOR = new Color(119, 44, 44);
    public static final Color LIGHT_RED_COLOR = new Color(190, 119, 119);
    private static final Color[] COLORS = { Board2D.DARK_COLOR, Board2D.LIGHT_COLOR, Board2D.DARK_RED_COLOR, Board2D.LIGHT_RED_COLOR };

    private static final Color GRAY = new Color(75, 75, 75, 89);

    public Vector2 screenCoordsToSquare(Vector2 coords) {
        return new Vector2((coords.getX() - X_OFFSET)/SQUARE_WIDTH, -((coords.getY() - Y_OFFSET)/SQUARE_HEIGHT + 1 - this.COLUMNS));
    }

    public Vector2 squareToScreenCoords(Vector2 square) {
        return new Vector2((square.getX() * SQUARE_WIDTH) + X_OFFSET, ((this.COLUMNS - square.getY() - 1) * SQUARE_HEIGHT) + Y_OFFSET);
    }

    public class ChessAIMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            Vector2 coords = new Vector2(e.getX(), e.getY());
            Vector2 square = screenCoordsToSquare(coords);

            if(clickedPiece != null && squarePositions.contains(square) && clickedPiece.getPlayer().equals(board.getWhoseTurn())) {
                clickedPiece.movePiece(square);
                board.setClicked(null);
            } else {
                board.setClicked(square);
            }
            clickedPiece = null;
            squarePositions.clear();
            circlePositions.clear();
            repaint();
        }
    }

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
                        this.images.put(p, ImageIO.read(new File(p.getIconPath())));
                    } catch(IOException e) {
                        this.images.put(p, null);
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
                    BufferedImage img = this.images.get(piece);
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
    }
}
