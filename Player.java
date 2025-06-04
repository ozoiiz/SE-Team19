package com.example.javafxyut;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private Color color;
    private List<Piece> pieces;

    public Player(String name, int pieceCount, Color color) {
        this.name = name;
        this.color = color;
        pieces = new ArrayList<>();
        for (int i = 0; i < pieceCount; i++) {
            pieces.add(new Piece());
        }
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    // movePiece: 지정된 인덱스의 말에 YutResult의 이동값을 적용하여 이동시키는 메서드
    public void movePiece(int index, YutResult result, int boardSize, Board board) {
        if (index >= 0 && index < pieces.size()) {
            Piece piece = pieces.get(index);
            piece.move(result.getMove(), board);
        }
    }
}
