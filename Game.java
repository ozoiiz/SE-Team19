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
     * 이동한 말(movedPiece)이 위치한 칸을 기준
     * 같은 플레이어의 말이 같은 위치에 있으면 그룹핑(groupWith)을 적용
     * 다른 플레이어의 말이 같은 위치에 있으면 캡처(capture)를 적용
     */
    public void processPieceLanding(Piece movedPiece, Player currentPlayer) {
        for (Piece piece : currentPlayer.getPieces()) {
            if (piece != movedPiece && !piece.isFinished() && piece.getPosition() != 0) {
                if (piece.getPosition() == movedPiece.getPosition()) {
                    movedPiece.groupWith(piece);
                }
            }
        }
        int commonPos = movedPiece.getPosition();
        for (Piece piece : currentPlayer.getPieces()) {
            if (piece.isGrouped() && piece.getPosition() != commonPos) {
                piece.setPosition(commonPos);
            }
        }
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
        if (movedPiece.isFinished()) {
            int finishPos = movedPiece.getPosition();
            for (Piece p : currentPlayer.getPieces()) {
                if (p != movedPiece && p.isGrouped()) {
                    p.setPosition(finishPos);
                    p.finish();
                }
            }
        }
    }

    /**
     * 게임을 초기 상태로 되돌립니다.
     */
    public void reset() {
        // 모든 플레이어 말 초기화
        for (Player p : players) {
            for (Piece piece : p.getPieces()) {
                piece.reset();
            }
        }
        currentPlayerIndex = 0;
        // 던지기 결과 초기화
        yutThrower = new YutThrower();
    }
}
