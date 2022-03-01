package com.ben9583.chess_ai.gfx;

import javax.swing.*;

public class ChessAIWindow {
    private final JFrame frame;
    private final ChessAIScene scene;

    public ChessAIWindow() {
        this.frame = new JFrame("Chess AI");
        this.scene = new ChessAIScene();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().add(this.scene);

        scene.setBounds(0, 0, 1920, 1080);

        frame.setSize(1920, 1080);

        frame.revalidate();
        frame.repaint();
        frame.setVisible(true);
    }
}
