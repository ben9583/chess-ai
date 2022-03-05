package com.ben9583.chess_ai.gfx;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.gfx.components.Board2D;

import javax.swing.*;
import java.awt.*;

/*
Main component class belonging to the window of this game.
Contains the GUI for the board.
 */
public class ChessAIScene extends JComponent {
    /* The graphical interface of the board. */
    public final Board2D board2d;

    public ChessAIScene(Board board) {
        super();

        this.board2d = new Board2D(8, 8, board);
        super.add(board2d);
        board2d.setBounds(0, 0, 1920, 1080);
        super.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        if(!(g instanceof Graphics2D g2)) return;

        g2.setColor(new Color(10, 20, 50));
        g2.fillRect(0, 0, 1920, 1080);

        super.revalidate();
        super.repaint();
    }
}
