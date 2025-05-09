// Game.java
package com.example.yutgame;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private List<Player> players;
    private Board board;
    private YutThrower yutThrower;
    private int currentPlayerIndex;

    public Game(Board board) {
        players = new ArrayList<>();
        this.board = board;
        yutThrower = new YutThrower();
        currentPlayerIndex = 0;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Board getBoard() {
        return board;
    }

    public YutThrower getYutThrower() {
        return yutThrower;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void nextTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public boolean checkWinCondition() {
        for (Player p : players) {
            boolean allFinished = true;
            for (Piece piece : p.getPieces()) {
                if (!piece.isFinished()) {
                    allFinished = false;
                    break;
                }
            }
            if (allFinished)
                return true;
        }
        return false;
    }

    public Player getWinner() {
        for (Player p : players) {
            boolean allFinished = true;
            for (Piece piece : p.getPieces()) {
                if (!piece.isFinished()) {
                    allFinished = false;
                    break;
                }
            }
            if (allFinished)
                return p;
        }
        return null;
    }

    /**
    이동한 말(movedPiece)이 위치한 칸을 기준
    같은 플레이어의 말이 같은 위치에 있으면 그룹핑(groupWith)을 적용
    다른 플레이어의 말이 같은 위치에 있으면 캡처(capture)를 적용, 그룹핑 해제 및 상대 말을 시작점(0)으로 돌림 이때 루트도 아우터로 다시 설정
    이 메서드는 말의 이동 후 GameController에서 호출
     */
    public void processPieceLanding(Piece movedPiece, Player currentPlayer) {
        for (Piece piece : currentPlayer.getPieces()) {
            if (piece != movedPiece && !piece.isFinished() && piece.getPosition() != 0) {
                if (piece.getPosition() == movedPiece.getPosition()) {
                        movedPiece.groupWith(piece);
                }
            }
        }

        // 그룹된 말들은 모두 동일한 위치로 동기화합니다.
        int commonPos = movedPiece.getPosition();
        for (Piece piece : currentPlayer.getPieces()) {
            if (piece.isGrouped() && piece.getPosition() != commonPos) {
                piece.setPosition(commonPos); // setPosition() 메서드를 추가해야 합니다.
            }
        }

        // 다른 플레이어의 말들에 대해 캡처 처리
        for (Player opponent : players) {
            if (!opponent.equals(currentPlayer)) {
                for (Piece op : opponent.getPieces()) {
                    if (!op.isFinished() && op.getPosition() != 0 &&
                            op.getPosition() == movedPiece.getPosition()) {
                        movedPiece.capture(op);
                    }
                }
            }
        }
    }
}
