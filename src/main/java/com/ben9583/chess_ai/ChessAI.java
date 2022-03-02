package com.ben9583.chess_ai;

public class ChessAI {
    public static ChessGame game;

    private static boolean graphicsEnabled = false;

    public static void main(String[] args) {
        for(String arg : args) {
            if (arg.equals("--graphics")) {
                ChessAI.graphicsEnabled = true;
                break;
            }
        }

        ChessAI.game = new ChessGame(ChessAI.graphicsEnabled);
    }
}
