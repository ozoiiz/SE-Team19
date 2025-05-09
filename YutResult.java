package com.example.yutgame;

public enum YutResult {
    BACKDO(-1, false),  // 백도: 1칸 후퇴
    DO(1, false),       // 도: 1칸 전진
    GAE(2, false),      // 개: 2칸 전진
    GEOL(3, false),     // 걸: 3칸 전진
    YUT(4, true),       // 윷: 4칸 전진, 추가 던지기 있음
    MO(5, true);        // 모: 5칸 전진, 추가 던지기 있음

    private final int move;
    private final boolean extraThrow;

    YutResult(int move, boolean extraThrow) {
        this.move = move;
        this.extraThrow = extraThrow;
    }

    public int getMove() {
        return move;
    }

    public boolean hasExtraThrow() {
        return extraThrow;
    }
}
