package com.example.yutgame;

public interface GameView {
    void updateBoard(Game game);
    void showMessage(String message);
    void setController(GameController controller);
}
