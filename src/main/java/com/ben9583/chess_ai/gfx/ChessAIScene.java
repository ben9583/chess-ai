package com.ben9583.chess_ai.gfx;

import com.ben9583.chess_ai.gfx.components.Board2D;

import javax.swing.*;
import java.awt.*;

public class ChessAIScene extends JComponent {
    public final Board2D board;
    public ChessAIScene() {
        super();

        this.board = new Board2D(8, 8);
        super.add(board);
        board.setBounds(0, 0, 1920, 1080);
    }

    @Override
    public void paintComponent(Graphics g) {
        if(!(g instanceof Graphics2D)) return;
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(new Color(10, 20, 50));
        g2.fillRect(0, 0, 1920, 1080);

        super.revalidate();
        super.repaint();
        super.setVisible(true);
    }
}
