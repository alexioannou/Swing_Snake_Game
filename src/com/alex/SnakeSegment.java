package com.alex;

public class SnakeSegment {
    boolean isHead = false;
    int x;
    int y;

    public boolean isHead() {
        return isHead;
    }

    public void setHead(boolean head) {
        isHead = head;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public SnakeSegment(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
