package com.example.yutgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SwingGameView extends JFrame implements GameView {
    private GameController controller;
    private BoardPanel boardPanel;
    private JPanel controlPanel;
    private JLabel statusLabel;
    private JPanel throwButtonPanel;
    private boolean testMode;

    // testMode: true이면 테스트 모드, false이면 실제 모드
    public SwingGameView(Board board, Game game, boolean testMode) {
        this.testMode = testMode;
        setTitle("윷놀이 게임");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents(board, game);
    }

    private void initComponents(Board board, Game game) {
        boardPanel = new BoardPanel(board, game);
        statusLabel = new JLabel("게임 시작!");
        controlPanel = new JPanel(new BorderLayout());

        // 모드에 따라 던지기 버튼 구성이 달라집니다.
        if (testMode) {
            // 테스트 모드: 각 윷 결과 버튼 생성 (백도, 도, 개, 걸, 윷, 모)
            throwButtonPanel = new JPanel();
            for (YutResult res : YutResult.values()) {
                JButton btn = new JButton(res.name());
                btn.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (controller != null) {
                            controller.handleSpecifiedThrow(res);
                        }
                    }
                });
                throwButtonPanel.add(btn);
            }
            controlPanel.add(throwButtonPanel, BorderLayout.CENTER);
        } else {
            // 실제 모드: 랜덤 윷 던지기 버튼 생성
            JButton randomThrowButton = new JButton("랜덤 윷 던지기");
            randomThrowButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (controller != null) {
                        controller.handleRandomThrow();
                    }
                }
            });
            controlPanel.add(randomThrowButton, BorderLayout.CENTER);
        }

        // 두 모드 모두에 'Apply Moves' 버튼 추가
        JButton applyMovesButton = new JButton("Apply Moves");
        applyMovesButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if (controller != null) {
                    controller.applyPendingResults();
                }
            }
        });
        controlPanel.add(applyMovesButton, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(statusLabel, BorderLayout.NORTH);
        getContentPane().add(boardPanel, BorderLayout.CENTER);
        getContentPane().add(controlPanel, BorderLayout.SOUTH);
    }

    @Override
    public void updateBoard(Game game) {
        boardPanel.refresh();
    }

    @Override
    public void showMessage(String message) {
        statusLabel.setText(message);
    }

    @Override
    public void setController(GameController controller) {
        this.controller = controller;
    }
}
