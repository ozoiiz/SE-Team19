package com.example.yutgame;

import java.util.Random;

public class YutThrower {
    private Random rand;

    public YutThrower() {
        rand = new Random();
    }

    public YutResult throwYut() {
        int r = rand.nextInt(16); // 0부터 15까지
        if (r == 0) return YutResult.BACKDO;
        else if (r < 4) return YutResult.DO;
        else if (r < 10) return YutResult.GAE;
        else if (r < 14) return YutResult.GEOL;
        else if (r == 14) return YutResult.YUT;
        else return YutResult.MO;
    }
}
