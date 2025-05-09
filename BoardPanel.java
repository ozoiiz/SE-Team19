package com.example.yutgame;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class BoardPanel extends JPanel {
    private Board board;
    private Game game;
    private Map<Integer, Point> cellPositions;

    public BoardPanel(Board board, Game game) {
        this.board = board;
        this.game = game;
        setPreferredSize(new Dimension(600, 600));
        cellPositions = new HashMap<>();
        initCellPositions();
    }

    private void initCellPositions() {
        if (board.getShape() == BoardShape.SQUARE) {
            initSquarePositions();
        } else if (board.getShape() == BoardShape.PENTAGON) {
            initPentagonPositions();
        } else if (board.getShape() == BoardShape.HEXAGON) {
            initHexagonPositions();
        }
    }

    // 사각형
    private void initSquarePositions() {
        Point v0 = new Point(550, 550);  // bottom-right
        Point v1 = new Point(550, 250);  // top-right
        Point v2 = new Point(250, 250);  // top-left
        Point v3 = new Point(250, 550);  // bottom-left

        int idx = 0;
        // 외곽 (20개)
        for (int i = 0; i < 5; i++) {
            double t = i / 5.0;
            cellPositions.put(idx++, new Point(
                    (int)(v0.x + t * (v1.x - v0.x)),
                    (int)(v0.y + t * (v1.y - v0.y))
            ));
        }
        cellPositions.put(idx++, new Point(v1));
        for (int i = 1; i < 5; i++) {
            double t = i / 5.0;
            cellPositions.put(idx++, new Point(
                    (int)(v1.x + t * (v2.x - v1.x)),
                    (int)(v1.y + t * (v2.y - v1.y))
            ));
        }
        cellPositions.put(idx++, new Point(v2));
        for (int i = 1; i < 5; i++) {
            double t = i / 5.0;
            cellPositions.put(idx++, new Point(
                    (int)(v2.x + t * (v3.x - v2.x)),
                    (int)(v2.y + t * (v3.y - v2.y))
            ));
        }
        cellPositions.put(idx++, new Point(v3));
        for (int i = 1; i < 5; i++) {
            double t = i / 5.0;
            cellPositions.put(idx++, new Point(
                    (int)(v3.x + t * (v0.x - v3.x)),
                    (int)(v3.y + t * (v0.y - v3.y))
            ));
        }

        // 지름길 (각 꼭짓점 → 1/3, 2/3 → 센터)
        Point center = new Point(400, 400);
        Point[] verts = { v0, v1, v2, v3 };
        double[] tv = { 1.0/3, 2.0/3 };
        for (Point v : verts) {
            for (double t : tv) {
                cellPositions.put(idx++, new Point(
                        (int)(v.x + t * (center.x - v.x)),
                        (int)(v.y + t * (center.y - v.y))
                ));
            }
        }
        // 센터
        cellPositions.put(idx, center);
    }

    // 오각형
    private void initPentagonPositions() {
        int n = 5;
        Point center = new Point(400, 400);
        double r = 250;
        double[] angles = {
                Math.toRadians(198), Math.toRadians(270),
                Math.toRadians(342), Math.toRadians(54),
                Math.toRadians(126)
        };
        Point[] verts = new Point[n];
        for (int i = 0; i < n; i++) {
            verts[i] = new Point(
                    center.x + (int)(r * Math.cos(angles[i])),
                    center.y + (int)(r * Math.sin(angles[i]))
            );
        }

        int idx = 0;
        // 외곽 (25개)
        for (int i = 0; i < n; i++) {
            Point s = verts[i];
            Point e = verts[(i + 1) % n];
            cellPositions.put(idx++, new Point(s));
            for (int j = 1; j <= 4; j++) {
                double t = j / 5.0;
                cellPositions.put(idx++, new Point(
                        (int)(s.x + t * (e.x - s.x)),
                        (int)(s.y + t * (e.y - s.y))
                ));
            }
        }

        // 분기 (10개)
        double[] tv = { 1.0/3, 2.0/3 };
        for (Point v : verts) {
            for (double t : tv) {
                cellPositions.put(idx++, new Point(
                        (int)(v.x + t * (center.x - v.x)),
                        (int)(v.y + t * (center.y - v.y))
                ));
            }
        }
        // 센터
        cellPositions.put(idx, center);
    }

    // 육각형
    private void initHexagonPositions() {
        int n = 6;
        Point center = new Point(400, 400);
        double r = 250;
        double[] angles = {
                Math.toRadians(180), Math.toRadians(240),
                Math.toRadians(300), Math.toRadians(0),
                Math.toRadians(60),  Math.toRadians(120)
        };
        Point[] verts = new Point[n];
        for (int i = 0; i < n; i++) {
            verts[i] = new Point(
                    center.x + (int)(r * Math.cos(angles[i])),
                    center.y + (int)(r * Math.sin(angles[i]))
            );
        }

        int idx = 0;
        // 외곽 (30개)
        for (int i = 0; i < n; i++) {
            Point s = verts[i];
            Point e = verts[(i + 1) % n];
            cellPositions.put(idx++, new Point(s));
            for (int j = 1; j <= 4; j++) {
                double t = j / 5.0;
                cellPositions.put(idx++, new Point(
                        (int)(s.x + t * (e.x - s.x)),
                        (int)(s.y + t * (e.y - s.y))
                ));
            }
        }

        // 분기 (12개)
        double[] tv = { 1.0/3, 2.0/3 };
        for (Point v : verts) {
            for (double t : tv) {
                cellPositions.put(idx++, new Point(
                        (int)(v.x + t * (center.x - v.x)),
                        (int)(v.y + t * (center.y - v.y))
                ));
            }
        }
        // 센터
        cellPositions.put(idx, center);
    }

    // ───── 내부 클래스: 말 표시 정보 ──────────────────────────────────
    private static class PieceDisplay {
        String label;
        Color color;
        public PieceDisplay(String label, Color color) {
            this.label = label;
            this.color = color;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int cellSize = 30;
        double cellRadius = cellSize / 2.0;
        double specialRadius = cellSize; // bigSize/2

        int outerCount;
        switch (board.getShape()) {
            case SQUARE: outerCount = 20; break;
            case PENTAGON: outerCount = 25; break;
            case HEXAGON: outerCount = 30; break;
            default: outerCount = 0;
        }
        int centerIndex = board.getTotalCells() - 1;

        java.util.List<Integer> vertexIndices = new ArrayList<>();
        if (board.getShape() == BoardShape.SQUARE) {
            vertexIndices.add(0); vertexIndices.add(5);
            vertexIndices.add(10); vertexIndices.add(15);
        } else if (board.getShape() == BoardShape.PENTAGON) {
            for (int i = 0; i < 5; i++) vertexIndices.add(i * 5);
        } else if (board.getShape() == BoardShape.HEXAGON) {
            for (int i = 0; i < 6; i++) vertexIndices.add(i * 5);
        }

        // 외곽 선 연결
        g2.setColor(Color.BLACK);
        for (int i = 0; i < outerCount; i++) {
            int next = (i + 1) % outerCount;
            Point p1 = cellPositions.get(i);
            Point p2 = cellPositions.get(next);
            if (p1 == null || p2 == null) continue;
            double r1 = vertexIndices.contains(i) ? specialRadius : cellRadius;
            double r2 = vertexIndices.contains(next) ? specialRadius : cellRadius;
            drawLineBetweenCircles(g2, p1, r1, p2, r2);
        }

        // 꼭짓점 ↔ 센터 선 연결
        for (int vIdx : vertexIndices) {
            Point vp = cellPositions.get(vIdx);
            Point cp = cellPositions.get(centerIndex);
            if (vp == null || cp == null) continue;
            drawLineBetweenCircles(g2, vp, specialRadius, cp, specialRadius);
        }

        // 셀 그리기
        for (int i = 0; i < board.getTotalCells(); i++) {
            Point p = cellPositions.get(i);
            if (p == null) continue;

            boolean isVertex = vertexIndices.contains(i);
            boolean isCenter = (i == centerIndex);
            boolean isSpecial = isVertex || isCenter;

            if (isSpecial) {
                int bigSize = cellSize * 2;
                g2.setColor(Color.WHITE);
                g2.fillOval(p.x - bigSize/2, p.y - bigSize/2, bigSize, bigSize);
                g2.setColor(Color.BLACK);
                g2.drawOval(p.x - bigSize/2, p.y - bigSize/2, bigSize, bigSize);

                int innerSize = cellSize * 3 / 2;
                g2.setColor(Color.WHITE);
                g2.fillOval(p.x - innerSize/2, p.y - innerSize/2, innerSize, innerSize);
                g2.setColor(Color.BLACK);
                g2.drawOval(p.x - innerSize/2, p.y - innerSize/2, innerSize, innerSize);
            } else {
                g2.setColor(Color.WHITE);
                g2.fillOval(p.x - cellSize/2, p.y - cellSize/2, cellSize, cellSize);
                g2.setColor(Color.BLACK);
                g2.drawOval(p.x - cellSize/2, p.y - cellSize/2, cellSize, cellSize);
            }
            g2.setColor(Color.BLACK);
            g2.drawString(String.valueOf(i), p.x - 5, p.y + 5);
        }

        // 말 그리기
        Map<Integer, java.util.List<PieceDisplay>> cellPieces = new HashMap<>();
        for (Player player : game.getPlayers()) {
            int num = 1;
            for (Piece piece : player.getPieces()) {
                if (piece.isFinished()) { num++; continue; }
                int pos = piece.getPosition();
                PieceDisplay pd = new PieceDisplay(String.valueOf(num), player.getColor());
                cellPieces.computeIfAbsent(pos, k -> new ArrayList<>()).add(pd);
                num++;
            }
        }
        for (Map.Entry<Integer, java.util.List<PieceDisplay>> entry : cellPieces.entrySet()) {
            Point p = cellPositions.get(entry.getKey());
            int j = 0;
            for (PieceDisplay pd : entry.getValue()) {
                int xOff = (j % 3) * 10;
                int yOff = (j / 3) * 10;
                g2.setColor(pd.color);
                g2.fillOval(p.x - 10 + xOff, p.y - 10 + yOff, 20, 20);
                g2.setColor(Color.BLACK);
                g2.drawOval(p.x - 10 + xOff, p.y - 10 + yOff, 20, 20);
                g2.drawString(pd.label, p.x - 5 + xOff, p.y + 5 + yOff);
                j++;
            }
        }
    }

    // 선 그을 때 원에 겹치지 않게
    private void drawLineBetweenCircles(Graphics2D g2, Point p1, double r1, Point p2, double r2) {
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        double dist = Math.hypot(dx, dy);
        if (dist <= 0) return;
        double ux = dx / dist;
        double uy = dy / dist;
        int x1 = (int)(p1.x + ux * r1);
        int y1 = (int)(p1.y + uy * r1);
        int x2 = (int)(p2.x - ux * r2);
        int y2 = (int)(p2.y - uy * r2);
        g2.drawLine(x1, y1, x2, y2);
    }

    public void refresh() {
        repaint();
    }

}
