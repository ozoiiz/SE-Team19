package com.example.javafxyut;

import com.example.javafxyut.fx.JavaFXBoardPanel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.awt.Color;

public class JavaFXGameView extends Application implements GameView {
    private Board board;
    private Game game;
    private boolean testMode;
    private GameController controller;
    private Label statusLabel;
    private JavaFXBoardPanel boardPanel;
    private HBox throwButtonBox;
    private Stage mainStage;

    @Override
    public void start(Stage primaryStage) {
        this.mainStage = primaryStage;

        // 임시 UI로 stage 먼저 띄움
        VBox placeholder = new VBox(new Label("Yutnori Game Loading..."));
        Scene scene = new Scene(placeholder, 700, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("윷놀이 게임 (JavaFX)");
        primaryStage.show();

        // 모든 입력 다이얼로그를 안전하게 띄우기 위해 사용
        Platform.runLater(() -> runSetupSequence(primaryStage));
    }

    private void runSetupSequence(Stage primaryStage) {
        // 1. 보드 형태 선택
        ChoiceDialog<String> boardDialog = new ChoiceDialog<>("SQUARE", "SQUARE", "PENTAGON", "HEXAGON");
        boardDialog.setTitle("보드 선택");
        boardDialog.setHeaderText("보드 형태를 선택하세요:");
        boardDialog.initOwner(mainStage);
        String boardChoice = boardDialog.showAndWait().orElse("SQUARE");
        BoardShape selectedShape = BoardShape.valueOf(boardChoice);

        // 2. 플레이어 수 입력
        int playerCount = 2;
        while (true) {
            TextInputDialog dialog = new TextInputDialog("2");
            dialog.setTitle("플레이어 수");
            dialog.setHeaderText("플레이어 수를 입력하세요 (2-4):");
            dialog.initOwner(mainStage);
            String input = dialog.showAndWait().orElse("2");
            try {
                playerCount = Integer.parseInt(input);
                playerCount = Math.max(2, Math.min(4, playerCount));
                break;
            } catch (NumberFormatException ignored) {}
        }

        // 3. 각 플레이어 말 개수 입력
        int pieceCount = 4;
        while (true) {
            TextInputDialog dialog = new TextInputDialog("4");
            dialog.setTitle("말 개수");
            dialog.setHeaderText("각 플레이어의 말 갯수를 입력하세요 (2-5):");
            dialog.initOwner(mainStage);
            String input = dialog.showAndWait().orElse("4");
            try {
                pieceCount = Integer.parseInt(input);
                pieceCount = Math.max(2, Math.min(5, pieceCount));
                break;
            } catch (NumberFormatException ignored) {}
        }

        // 4. 게임 모드 선택 (T/R)
        TextInputDialog modeDialog = new TextInputDialog("R");
        modeDialog.setTitle("게임 모드를 선택하세요");
        modeDialog.setHeaderText("테스트 모드(T) / 실제 모드(R) : ");
        modeDialog.initOwner(mainStage);
        String modeInput = modeDialog.showAndWait().orElse("R").trim();
        testMode = modeInput.equalsIgnoreCase("T");

        // 5. 모델 생성
        board = new Board(selectedShape);
        game  = new Game(board);

        // 6. 플레이어 색상
        Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE };

        // 7. 플레이어 이름 입력
        for (int i = 0; i < playerCount; i++) {
            TextInputDialog nameDialog = new TextInputDialog("플레이어" + (i + 1));
            nameDialog.setTitle("플레이어 이름 입력");
            nameDialog.setHeaderText("플레이어 " + (i + 1) + "의 이름을 입력하세요:");
            nameDialog.initOwner(mainStage);
            String playerName = nameDialog.showAndWait().orElse("플레이어" + (i + 1));
            if (playerName.isEmpty()) playerName = "플레이어" + (i + 1);
            game.addPlayer(new Player(playerName, pieceCount, colors[i]));
        }

        // 8. UI 세팅
        statusLabel   = new Label("게임 시작!");
        statusLabel.setStyle("-fx-font-size:20px; -fx-padding:8px;");
        boardPanel    = new JavaFXBoardPanel(board, game);
        throwButtonBox= new HBox(10); throwButtonBox.setAlignment(Pos.CENTER);
        controller    = new GameController(game, this);

        if (testMode) {
            for (YutResult res : YutResult.values()) {
                Button b = new Button(res.name());
                b.setOnAction(e -> controller.handleSpecifiedThrow(res));
                throwButtonBox.getChildren().add(b);
            }
        } else {
            Button rand = new Button("랜덤 윷 던지기");
            rand.setOnAction(e -> controller.handleRandomThrow());
            throwButtonBox.getChildren().add(rand);
        }

        Button apply = new Button("결과 적용");
        apply.setOnAction(e -> controller.applyPendingResults());

        VBox controls = new VBox(10, throwButtonBox, apply);
        controls.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, statusLabel, boardPanel, controls);
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-padding:20px;");

        // 메인 UI로 교체
        mainStage.getScene().setRoot(root);

        controller.startGame();
    }

    // -- GameView 인터페이스 구현 --

    @Override
    public void updateBoard(Game game) {
        Platform.runLater(boardPanel::redraw);
    }

    @Override
    public void showMessage(String message) {
        Platform.runLater(() -> statusLabel.setText(message));
    }

    @Override
    public void setController(GameController c) {
        this.controller = c;
    }

    @Override
    public void resetBoard() {
        Platform.runLater(() -> {
            statusLabel.setText("게임 시작!");
            boardPanel.redraw();
        });
    }

    @Override
    public String showInputDialog(String msg) {
        TextInputDialog d = new TextInputDialog();
        d.setHeaderText(msg);
        d.setTitle("입력");
        d.initOwner(mainStage);
        return d.showAndWait().orElse(null);
    }

    @Override
    public int showOptionDialog(String msg, String title, String[] opts, String def) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.initOwner(mainStage);
        ButtonType[] types = new ButtonType[opts.length];
        for (int i = 0; i < opts.length; i++) types[i] = new ButtonType(opts[i]);
        a.getButtonTypes().setAll(types);
        ButtonType res = a.showAndWait().orElse(null);
        for (int i = 0; i < types.length; i++)
            if (res == types[i]) return i;
        return -1;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
