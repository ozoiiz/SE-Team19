package com.example.yutgame;

import javax.swing.*;
import java.awt.Color;

public class Main {
    public static void main(String[] args) {
        // 보드 형태 선택
        String[] boardOptions = {"SQUARE", "PENTAGON", "HEXAGON"};
        String boardChoice = (String) JOptionPane.showInputDialog(null, "보드 형태를 선택하세요:",
                "보드 선택", JOptionPane.QUESTION_MESSAGE, null, boardOptions, boardOptions[0]);
        BoardShape selectedShape = BoardShape.SQUARE;
        if (boardChoice != null) {
            selectedShape = BoardShape.valueOf(boardChoice);
        }

        // 플레이어 수 입력 (2~5)
        String playerCountStr = JOptionPane.showInputDialog("플레이어 수를 입력하세요 (2-5):");
        int playerCount = Integer.parseInt(playerCountStr);
        if (playerCount < 2) playerCount = 2;
        if (playerCount > 5) playerCount = 5;

        // 각 플레이어의 말 갯수 입력 (2-5)
        String pieceCountStr = JOptionPane.showInputDialog("각 플레이어의 말 갯수를 입력하세요 (2-5):");
        int pieceCount = Integer.parseInt(pieceCountStr);
        if (pieceCount < 2) pieceCount = 2;
        if (pieceCount > 5) pieceCount = 5;

        // 게임 모드 선택: 테스트 모드(T) / 실제 모드(R)
        String modeInput = JOptionPane.showInputDialog("게임 모드를 선택하세요:\n테스트 모드(T) / 실제 모드(R):");
        final boolean testMode = (modeInput != null && modeInput.equalsIgnoreCase("T"));

        // 보드와 게임 생성 (final로 선언하여 inner class에서 사용)
        final Board board = new Board(selectedShape);
        final Game game = new Game(board);

        // 플레이어별 색상: 1번: 빨강, 2번: 주황, 3번: 노랑, 4번: 초록, 5번: 파랑
        Color[] colors = {
                Color.RED,
                new Color(255, 165, 0),
                Color.YELLOW,
                Color.GREEN,
                Color.BLUE
        };

        for (int i = 0; i < playerCount; i++) {
            String playerName = JOptionPane.showInputDialog("플레이어 " + (i+1) + "의 이름을 입력하세요:");
            if (playerName == null || playerName.isEmpty()) {
                playerName = "Player" + (i+1);
            }
            game.addPlayer(new Player(playerName, pieceCount, colors[i]));
        }

        SwingUtilities.invokeLater(() -> {
            SwingGameView view = new SwingGameView(board, game, testMode);
            GameController controller = new GameController(game, view);
            view.setVisible(true);
            controller.startGame();
        });

    }
}
