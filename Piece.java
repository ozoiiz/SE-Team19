package com.example.yutgame;

public class Piece {
    public enum MovementRoute {
        OUTER,   // 기본 외곽 경로
        CENTER,  // 분기 경로
        FINAL    // 마지막 경로
    }

    private int position;                // 현재 board의 셀 인덱스
    private boolean grouped;             // 그룹 상태
    private boolean finished;            // 완주 상태
    private MovementRoute route;         // 현재 이동 경로
    private int branchAccumulated;       // CENTER 경로 누적 이동 값
    private int finalBranchAccumulated;  // FINAL 경로 누적 이동 값
    private int branchStart;             // 분기 시작점(vertex) 인덱스

    public Piece() {
        reset();
    }

    /**
     * Piece를 초기 상태로 되돌립니다.
     */
    public void reset() {
        this.position = 0;
        this.grouped = false;
        this.finished = false;
        this.route = MovementRoute.OUTER;
        this.branchAccumulated = 0;
        this.finalBranchAccumulated = 0;
        this.branchStart = -1;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int pos) {
        this.position = pos;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isGrouped() {
        return grouped;
    }

    /**
     * 상대 말(opponent)을 잡아 출발점으로 보냅니다.
     */
    public void capture(Piece opponent) {
        opponent.reset();
    }

    /**
     * 같은 플레이어의 말을 그룹 상태로 만듭니다.
     */
    public void groupWith(Piece other) {
        this.grouped = true;
        other.grouped = true;
    }

    public void finish() {
        this.finished = true;
    }


    // ─────────────────────────────────────────────────
    // 헬퍼 메서드: 보드 모양 별 상수값
    private int getOuterRingLength(BoardShape shape) {
        switch (shape) {
            case SQUARE:   return 20;
            case PENTAGON: return 25;
            case HEXAGON:  return 30;
            default:       return 0;
        }
    }

    private int getCenterToOuter(BoardShape shape) {
        switch (shape) {
            case SQUARE:   return 9;
            case PENTAGON: return 14;
            case HEXAGON:  return 19;
            default:       return 0;
        }
    }

    private int getFinishCellIndex(BoardShape shape) {
        // 출발점과 동일하게 처리
        return 0;
    }

    private int getFinishVertexPrevious(BoardShape shape) {
        switch (shape) {
            case SQUARE:   return 15;
            case PENTAGON: return 20;
            case HEXAGON:  return 25;
            default:       return 0;
        }
    }

    private int getCenterCellIndex(BoardShape shape) {
        switch (shape) {
            case SQUARE:   return 28;
            case PENTAGON: return 35;
            case HEXAGON:  return 42;
            default:       return 0;
        }
    }

    private int getFirstBranchCell(BoardShape shape, int vertexIndex) {
        if (shape == BoardShape.SQUARE) {
            if (vertexIndex == 5) return 22;
            if (vertexIndex == 10) return 24;
            if (vertexIndex == 15) return 26;
        }
        if (shape == BoardShape.PENTAGON) {
            if (vertexIndex == 5) return 27;
            if (vertexIndex == 10) return 29;
            if (vertexIndex == 15) return 31;
        }
        if (shape == BoardShape.HEXAGON) {
            if (vertexIndex == 5) return 32;
            if (vertexIndex == 10) return 34;
            if (vertexIndex == 15) return 36;
            if (vertexIndex == 20) return 38;
        }
        return 0;
    }

    private int getSecondBranchCell(BoardShape shape, int vertexIndex) {
        if (shape == BoardShape.SQUARE) {
            if (vertexIndex == 5) return 23;
            if (vertexIndex == 10) return 25;
            if (vertexIndex == 15) return 27;
        }
        if (shape == BoardShape.PENTAGON) {
            if (vertexIndex == 5) return 28;
            if (vertexIndex == 10) return 30;
            if (vertexIndex == 15) return 32;
        }
        if (shape == BoardShape.HEXAGON) {
            if (vertexIndex == 5) return 33;
            if (vertexIndex == 10) return 35;
            if (vertexIndex == 15) return 37;
            if (vertexIndex == 20) return 39;
        }
        return 0;
    }


    //중심점 -> 종료 이전 꼭짓점 1(중심점에 멈추지 못했을 경우)
    private int getThirdBranchCell(BoardShape shape, int vertexIndex) {
        if (shape == BoardShape.SQUARE) {
            if (vertexIndex == 5) return 27;
            if (vertexIndex == 10) return 21; }
        if (shape == BoardShape.PENTAGON && vertexIndex == 5 || vertexIndex == 10 || vertexIndex == 15) {return 34;}
        if (shape == BoardShape.HEXAGON)
            if(vertexIndex == 5 || vertexIndex == 10 || vertexIndex == 20) {return 41;}
        if(vertexIndex == 15) {return 31;}
        return 0;
    }
    //중심점 -> 종료 이전 꼭짓점 2(중심점에 멈추지 못했을 경우)
    private int getForthBranchCell(BoardShape shape, int vertexIndex) {
        if (shape == BoardShape.SQUARE) {
            if (vertexIndex == 5) return 26;
            if (vertexIndex == 10) return 20;
        }
        if (shape == BoardShape.PENTAGON && vertexIndex == 5 || vertexIndex == 10 || vertexIndex == 15) {return 33;}
        if (shape == BoardShape.HEXAGON)
            if(vertexIndex == 5 || vertexIndex == 10 || vertexIndex == 20) {return 40;}
        if(vertexIndex == 15) {return 30;}
        return 0;
    }
    //중심점 -> 종료점 1(중심점에서 멈췄을 경우 or 지름길이 꼭짓점으로 바로 연결되어 있을 경우 사각형과 육각형)
    private int getFirstFinalBranchCell(BoardShape shape, int vertexIndex) {
        if (shape == BoardShape.SQUARE && vertexIndex == 5 || vertexIndex == 10) {return 21;}
        if (shape == BoardShape.PENTAGON && vertexIndex == 5 || vertexIndex == 10 || vertexIndex == 15) {return 26;}
        if (shape == BoardShape.HEXAGON && vertexIndex == 5 || vertexIndex == 10 || vertexIndex == 15 || vertexIndex == 20) {return 31;}
        return 0;
    }


    private int getSecondFinalBranchCell(BoardShape shape, int vertexIndex) {
        if (shape == BoardShape.SQUARE && vertexIndex == 5 || vertexIndex == 10) return 20;
        if (shape == BoardShape.PENTAGON && vertexIndex == 5 || vertexIndex == 10 || vertexIndex == 15) return 25;
        if (shape == BoardShape.HEXAGON && vertexIndex == 5 || vertexIndex == 10 || vertexIndex == 15 || vertexIndex == 20) {return 30;}
        return 0;
    }

    private boolean atVertex(Board board) {
        BoardShape shape = board.getShape();
        int pos = this.position;
        if (shape == BoardShape.SQUARE) {
            return pos % 5 == 0 && pos < 20;
        } else if (shape == BoardShape.PENTAGON) {
            return pos % 5 == 0 && pos < 25;
        } else if (shape == BoardShape.HEXAGON) {
            return pos % 5 == 0 && pos < 30;
        }
        return false;
    }

    public void move(int steps, Board board) {
        if (finished) return;

        BoardShape shape = board.getShape();
        int outerLength = getOuterRingLength(shape);
        int finishCell = getFinishCellIndex(shape);
        int finishPrev = getFinishVertexPrevious(shape);
        int centerToOuter = getCenterToOuter(shape);
        //중심점 -> 종료점으로 이동하는 루트
        if (route == MovementRoute.FINAL) {
            finalBranchAccumulated += steps;
            if (finalBranchAccumulated == 0) {
                position = getCenterCellIndex(shape);
            } else if (finalBranchAccumulated == 1) {
                position = getFirstFinalBranchCell(shape, branchStart);
            } else if (finalBranchAccumulated == 2) {
                position = getSecondFinalBranchCell(shape, branchStart);
            } else if (finalBranchAccumulated >= 3) {
                position = finishCell;
                route = MovementRoute.OUTER;
                branchAccumulated = 0;
                finished = true;
                return;
            }
        }
        //기본 외곽 경로
        if (route == MovementRoute.OUTER) {
            // 시작점 처리
            if (position == 0) {
                int newPos = position + steps;
                if (newPos >= outerLength) {
                    position = finishCell;
                    finished = true;
                } else {
                    position = newPos;
                }
            } else {
                int newPos = position + steps;
                if (newPos >= outerLength) {
                    position = finishCell;
                    finished = true;
                } else {
                    position = newPos;
                }

                //이동 완료 후 꼭짓점이면 지름길로 이동
                if (!finished
                        && atVertex(board)
                        && position != finishPrev //끝이랑 끝 직전 꼭짓점에서는 분기하지 않고 아우터 경로로
                        && position != 0) {
                    route = MovementRoute.CENTER;
                    branchAccumulated = 0;
                    branchStart = position;
                    return;
                }
            }
        }

        if (route == MovementRoute.CENTER) {
            branchAccumulated += steps;
            if (branchAccumulated <= 1) {
                position = getFirstBranchCell(shape, branchStart);
            } else if (branchAccumulated == 2) {
                position = getSecondBranchCell(shape, branchStart);
            } else if (branchAccumulated == 3) {
                position = getCenterCellIndex(shape);
                route = MovementRoute.FINAL;
                branchAccumulated = 0;
            } else if (branchAccumulated == 4) {
                position = getThirdBranchCell(shape, branchStart);
            } else if (branchAccumulated == 5) {
                position = getForthBranchCell(shape, branchStart);
            } else {
                route = MovementRoute.OUTER;
                position = branchAccumulated + centerToOuter;
                branchAccumulated = 0;
            }
        }
    }
}

