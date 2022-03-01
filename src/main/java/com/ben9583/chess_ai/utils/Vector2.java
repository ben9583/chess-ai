package com.ben9583.chess_ai.utils;

public class Vector2 {
    private final int x;
    private final int y;

    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2(int xy) {
        this.x = xy;
        this.y = xy;
    }

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(Vector2 o) {
        return new Vector2(this.getX() + o.getX(), this.getY() + o.getY());
    }

    public Vector2 sub(Vector2 o) {
        return new Vector2(this.getX() - o.getX(), this.getY() - o.getY());
    }

    public Vector2 neg() {
        return new Vector2(-this.getX(), -this.getY());
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Vector2 v)) throw new ClassCastException("Object " + o + " is not of type Vector2.");
        return this.getX() == v.getX() && this.getY() == v.getY();
    }

    @Override
    public String toString() {
        return "(" + this.getX() + ", " + this.getY() + ")";
    }
}
