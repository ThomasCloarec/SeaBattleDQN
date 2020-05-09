package battle;

/**
 * The game board is composed of square objects that are defined by the present class.
 */
public class Square {
    /**
     * the x position of the square on the board
     */
    private final int x;
    /**
     * the x position of the square on the board
     */
    private final int y;
    /**
     * is the square free
     */
    private boolean free;
    /**
     * has the square been shot
     */
    private boolean hit;

    /**
     * The constructor of the class Square, initialize free as true and hit as false
     *
     * @param x the x position of the square on the board
     * @param y the x position of the square on the board
     */
    public Square(int x, int y) {
        if (x < 0) {
            throw new IllegalArgumentException("The x parameter should be a number greater than 0.");
        } else if (y < 0) {
            throw new IllegalArgumentException("The y parameter should be a number greater than 0.");
        }

        this.x = x;
        this.y = y;
        this.free = true;
        this.hit = false;
    }

    /**
     * Set free as false
     */
    public void setBusy() {
        this.free = false;
    }

    /**
     * Set hit as true
     */
    public void setHit() {
        this.hit = true;
    }

    /**
     * Return each field of the square
     *
     * @return the formatted string
     */
    @Override
    public String toString() {
        return "Square{" +
                "free=" + this.free +
                ", hit=" + this.hit +
                ", x=" + this.x +
                ", y=" + this.y +
                '}';
    }

    /**
     * Gets free.
     *
     * @return Value of free.
     */
    public boolean isFree() {
        return this.free;
    }

    /**
     * Gets hit.
     *
     * @return Value of hit.
     */
    public boolean isHit() {
        return this.hit;
    }
}
