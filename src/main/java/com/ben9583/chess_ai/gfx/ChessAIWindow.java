package com.ben9583.chess_ai.gfx;

import com.ben9583.chess_ai.components.Board;

import javax.swing.*;

/*
Class used to display the game should graphics be enabled.
 */
public class ChessAIWindow {
    /* The graphical window holding the game. */
    private final JFrame frame;
    /* The principal component of the window. */
    private final ChessAIScene scene;

    public ChessAIWindow(Board board) {
        this.frame = new JFrame("Chess AI");
        this.scene = new ChessAIScene(board);

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.getContentPane().setLayout(null);
        this.frame.getContentPane().add(this.scene);

        this.scene.setBounds(0, 0, 1920, 1080);

        this.frame.setSize(1920, 1080);

        this.frame.revalidate();
        this.frame.repaint();
        this.frame.setVisible(true);
    }
}
