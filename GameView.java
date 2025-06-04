package com.example.javafxyut;

public interface GameView {

    void updateBoard(Game game);
    void showMessage(String message);
    void setController(GameController controller);
    void resetBoard();

    String showInputDialog(String message);

    int showOptionDialog(String message, String title, String[] options, String defaultOption);
}
