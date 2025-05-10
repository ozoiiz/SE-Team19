package com.example.yutgame;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class GameController {
    private Game model;
    private GameView controllerView;
    // 이번 턴에 얻은 윷 던지기 결과들을 저장합니다.
    private List<YutResult> pendingResults;

    public GameController(Game model, GameView view) {
        this.model = model;
        this.controllerView = view;
        view.setController(this);
        pendingResults = new ArrayList<>();
    }

    public void startGame() {
        controllerView.showMessage("게임 시작! " + model.getCurrentPlayer().getName() + "의 턴입니다.");
        controllerView.updateBoard(model);
    }

    // 테스트 모드: 사용자가 버튼을 눌러 지정한 결과를 누적합니다.
    public void handleSpecifiedThrow(YutResult res) {
        pendingResults.add(res);
        controllerView.showMessage(model.getCurrentPlayer().getName() + "의 턴: "
                + res.name() + " 획득. 누적 결과: " + pendingResults);
        if (!res.hasExtraThrow()) {
            applyPendingResults();
        }
    }

    // 실제 모드: 랜덤 윷 던지기 버튼 클릭 시 호출됩니다.
    public void handleRandomThrow() {
        YutResult res = model.getYutThrower().throwYut();
        pendingResults.add(res);
        controllerView.showMessage(model.getCurrentPlayer().getName() + "의 랜덤 결과: " + res.name());
        if (!res.hasExtraThrow()) {
            applyPendingResults();
        }
    }

    // 결과 적용 및 턴 전환 로직
    public void applyPendingResults() {
        Player current = model.getCurrentPlayer();
        if (pendingResults.isEmpty()) {
            controllerView.showMessage("적용할 결과가 없습니다.");
            return;
        }
        // 실제 던진 결과 순서상의 마지막 값으로 추가 던지기 여부 결정
        boolean thrownExtra = pendingResults.get(pendingResults.size() - 1).hasExtraThrow();

        // 1) 적용 순서 입력 (Swing 다이얼로그 사용)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pendingResults.size(); i++) {
            sb.append(i + 1).append(":").append(pendingResults.get(i).name()).append("  ");
        }
        String orderStr = JOptionPane.showInputDialog(null,
                current.getName() + "님, 추가 던지기가 종료되었습니다.\n" +
                        "적용 순서를 쉼표로 구분하여 입력하세요.\n" + sb.toString(),
                "결과 적용 순서 선택", JOptionPane.QUESTION_MESSAGE);
        String[] parts = orderStr != null ? orderStr.split(",") : new String[0];
        List<YutResult> orderedResults = new ArrayList<>();
        for (String part : parts) {
            try {
                int idx = Integer.parseInt(part.trim()) - 1;
                if (idx >= 0 && idx < pendingResults.size()) {
                    orderedResults.add(pendingResults.get(idx));
                }
            } catch (NumberFormatException e) {
                // 무시
            }
        }
        if (orderedResults.size() != pendingResults.size()) {
            orderedResults = new ArrayList<>(pendingResults);
        }
        pendingResults.clear();

        // 2) 순서대로 결과 적용
        boolean captureOccurred = false;
        for (YutResult res : orderedResults) {
            int pieceIndex = promptForValidPiece(current, res);
            if (pieceIndex == -1) continue;
            current.movePiece(pieceIndex, res,
                    model.getBoard().getTotalCells(), model.getBoard());
            Piece movedPiece = current.getPieces().get(pieceIndex);
            if (model.processPieceLanding(movedPiece, current)) {
                captureOccurred = true;
            }
            controllerView.updateBoard(model);
            if (handleWinIfNeeded()) return;
        }

        // 3) 실제 던진 결과 마지막이 윷·모이거나 캡처가 발생했을 때만 추가 던지기 유지
        if (thrownExtra || captureOccurred) {
            controllerView.showMessage(current.getName() + "님의 추가 던지기 기회입니다.");
        } else {
            model.nextTurn();
            controllerView.showMessage("다음 턴: " + model.getCurrentPlayer().getName());
        }
    }

    /**
     * 승리 조건을 확인하고, 승리 시 재시작/종료 다이얼로그를 띄운 뒤 처리합니다.
     */
    private boolean handleWinIfNeeded() {
        if (!model.checkWinCondition()) return false;
        Player winner = model.getWinner();
        int choice = JOptionPane.showOptionDialog(
                null,
                winner.getName() + "님이 승리했습니다.\n게임을 다시 시작하시겠습니까?",
                "게임 종료",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"다시 시작", "종료"},
                "다시 시작"
        );
        if (choice == JOptionPane.YES_OPTION) {
            model.reset();
            controllerView.resetBoard();
            startGame();
        } else {
            System.exit(0);
        }
        return true;
    }

    /**
     * 사용자에게 결과를 적용할 말을 선택하도록 요청합니다.
     */
    private int promptForValidPiece(Player current, YutResult res) {
        while (true) {
            String input = JOptionPane.showInputDialog(null,
                    current.getName() + "님, 결과 " + res.name() + "을 적용할 말 번호를 선택하세요 (1-"
                            + current.getPieces().size() + " ): ");
            if (input == null) {
                controllerView.showMessage("입력이 취소되었습니다.");
                return -1;
            }
            int pieceIndex;
            try {
                pieceIndex = Integer.parseInt(input.trim()) - 1;
            } catch (NumberFormatException ex) {
                controllerView.showMessage("숫자를 입력하세요.");
                continue;
            }
            if (pieceIndex < 0 || pieceIndex >= current.getPieces().size()) {
                controllerView.showMessage("잘못된 말 번호입니다. 다시 선택하세요.");
                continue;
            }
            if (current.getPieces().get(pieceIndex).isFinished()) {
                controllerView.showMessage("해당 말은 이미 완주했습니다. 다른 말을 선택하세요.");
                continue;
            }
            return pieceIndex;
        }
    }
}
