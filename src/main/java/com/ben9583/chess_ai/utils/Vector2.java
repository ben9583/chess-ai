package com.ben9583.chess_ai.utils;

/*
Class that defines 2D integers using an ordered pair.
Also provides mathematical operations that can be performed on these objects.
 */
public class Vector2 {
    /* The cardinal directions expressed in Vector2 form. */
    public static final Vector2 NORTH = new Vector2(0, 1);
    public static final Vector2 SOUTH = new Vector2(0, -1);
    public static final Vector2 EAST = new Vector2(1, 0);
    public static final Vector2 WEST = new Vector2(-1, 0);

    /* The diagonal directions expressed in Vector2 form. */
    public static final Vector2 NORTHEAST = new Vector2(1, 1);
    public static final Vector2 SOUTHEAST = new Vector2(1, -1);
    public static final Vector2 SOUTHWEST = new Vector2(-1, -1);
    public static final Vector2 NORTHWEST = new Vector2(-1, 1);

    /* x-coordinate (first number) of this Vector2. */
    private final int x;
    /* y-coordinate (second number) of this Vector2. */
    private final int y;

    /**
     * Creates a Vector2 representing the origin, (0, 0).
     */
    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Creates a Vector2 that has equal x and y values.
     * @param xy The value representing both x and y
     */
    public Vector2(int xy) {
        this.x = xy;
        this.y = xy;
    }

    /**
     * Creates a Vector2 representing (x, y).
     * @param x The x-coordinate of this Vector2
     * @param y The y-coordinate of this Vector2
     */
    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Performs addition between this Vector2 and o, returning a new Vector2 as the answer.
     * @param o Another Vector2
     * @return The sum of this Vector2 and o
     */
    public Vector2 add(Vector2 o) {
        return new Vector2(this.getX() + o.getX(), this.getY() + o.getY());
    }

    /**
     * Performs subtraction between this Vector2 and o, returning a new Vector2 as the answer.
     * @param o Another Vector2
     * @return The difference of this Vector2 and o
     */
    public Vector2 sub(Vector2 o) {
        return new Vector2(this.getX() - o.getX(), this.getY() - o.getY());
    }

    /**
     * Performs negation on this vector, returning a new Vector2 as the answer.
     * @return A new Vector2 representing (-x, -y) of this Vector2
     */
    public Vector2 neg() {
        return new Vector2(-this.getX(), -this.getY());
    }

    /**
     * Returns the x-coordinate of this Vector2.
     * @return The x-coordinate of this Vector2
     */
    public int getX() {
        return this.x;
    }

    /**
     * Returns the y-coordinate of this Vector2.
     * @return The y-coordinate of this Vector2
     */
    public int getY() {
        return this.y;
    }

    /**
     * Returns whether this Vector2 and o are equal, that is, if they have the same x and y values.
     * @param o Another Vector2
     * @return Whether this Vector2 and o are equal
     */
    @Override
    public boolean equals(Object o) {
        if(o == null) return false;
        if(!(o instanceof Vector2 v)) throw new ClassCastException("Object " + o + " is not of type Vector2.");
        return this.getX() == v.getX() && this.getY() == v.getY();
    }

    @Override
    public String toString() {
        return "(" + this.getX() + ", " + this.getY() + ")";
    }
}
