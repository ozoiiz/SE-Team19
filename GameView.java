package com.example.yutgame;

public interface GameView {
    /** 보드와 말 상태를 화면에 갱신합니다. */
    void updateBoard(Game game);

    /** 사용자에게 메시지를 표시합니다. */
    void showMessage(String message);

    /** Controller를 뷰에 설정합니다. */
    void setController(GameController controller);

    /** 뷰를 초기 상태로 리셋합니다. */
    void resetBoard();
}
