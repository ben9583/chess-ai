package com.ben9583.chess_ai.gfx.components;

import javax.swing.*;
import java.awt.*;

public class Board2D extends JComponent {
    public final int COLUMNS;
    public final int ROWS;

    public static final int X_OFFSET = 200;
    public static final int Y_OFFSET = 75;
    public static final int SQUARE_WIDTH = 100;
    public static final int SQUARE_HEIGHT = 100;

    public static final Color DARK_COLOR = new Color(119, 73, 44);
    public static final Color LIGHT_COLOR = new Color(190, 157, 119);
    private static final Color[] COLORS = { Board2D.DARK_COLOR, Board2D.LIGHT_COLOR };

    public Board2D(int columns, int rows) {
        super();

        this.COLUMNS = columns;
        this.ROWS = rows;
    }

    @Override
    public void paintComponent(Graphics g) {
        if(!(g instanceof Graphics2D)) return;
        Graphics2D g2 = (Graphics2D)g;

        for(int i = 0; i < this.ROWS; i++) {
            for(int j = 0; j < this.COLUMNS; j++) {
                g2.setColor(COLORS[(i + j) % 2]);
                g2.fillRect(X_OFFSET + (j * SQUARE_WIDTH), Y_OFFSET + (i * SQUARE_HEIGHT), SQUARE_WIDTH, SQUARE_HEIGHT);
            }
        }
    }
}
