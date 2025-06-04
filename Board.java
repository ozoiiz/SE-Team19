package com.example.javafxyut;

public class Board {
    private BoardShape shape;
    private int totalCells;

    public Board(BoardShape shape) {
        this.shape = shape;
        if(shape == BoardShape.SQUARE) {
            totalCells = 29;
        } else if(shape == BoardShape.PENTAGON) {
            totalCells = 36;
        } else if(shape == BoardShape.HEXAGON) {
            totalCells = 43;
        }
    }
    public BoardShape getShape() {
        return shape;
    }
    public int getTotalCells() {
        return totalCells;
    }
}
