package com.ben9583.chess_ai.gfx.components;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.pieces.Piece;
import com.ben9583.chess_ai.utils.Vector2;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Board2D extends JComponent {
    public final int COLUMNS;
    public final int ROWS;

    private final Board board;
    private final Map<Piece, BufferedImage> images;

    public static final int X_OFFSET = 200;
    public static final int Y_OFFSET = 75;
    public static final int SQUARE_WIDTH = 100;
    public static final int SQUARE_HEIGHT = 100;

    public static final Color DARK_COLOR = new Color(119, 73, 44);
    public static final Color LIGHT_COLOR = new Color(190, 157, 119);
    private static final Color[] COLORS = { Board2D.DARK_COLOR, Board2D.LIGHT_COLOR };

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
    }

    @Override
    public void paintComponent(Graphics g) {
        if(!(g instanceof Graphics2D g2)) return;

        for(int i = 0; i < this.ROWS; i++) {
            for(int j = 0; j < this.COLUMNS; j++) {
                Piece piece = this.board.getPieceAtPosition(new Vector2(j, i));
                g2.setColor(COLORS[(i + j) % 2]);
                g2.fillRect(X_OFFSET + (j * SQUARE_WIDTH), Y_OFFSET + ((this.COLUMNS - i - 1) * SQUARE_HEIGHT), SQUARE_WIDTH, SQUARE_HEIGHT);

                if(piece != null) {
                    BufferedImage img = this.images.get(piece);
                    g2.drawImage(img, X_OFFSET + (j * SQUARE_WIDTH), Y_OFFSET + ((this.COLUMNS - i - 1) * SQUARE_HEIGHT), null);
                }
            }
        }
    }
}
