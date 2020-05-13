package battle.game.players;

/**
 * The game board is composed of square objects that are defined by the present class.
 */
public class Square {
    /**
     * the line position of the square on the board
     */
    private final int line;
    /**
     * the column position of the square on the board
     */
    private final int column;
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
     * @param line the x position of the square on the board
     * @param column the x position of the square on the board
     */
    public Square(int line, int column) {
        if (line < 0) {
            throw new IllegalArgumentException("The x parameter should be a number greater than 0.");
        } else if (column < 0) {
            throw new IllegalArgumentException("The y parameter should be a number greater than 0.");
        }

        this.line = line;
        this.column = column;
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
                ", x=" + this.line +
                ", y=" + this.column +
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

    /**
     * Gets the column position of the square on the board.
     *
     * @return Value of the column position of the square on the board.
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * Gets the line position of the square on the board.
     *
     * @return Value of the line position of the square on the board.
     */
    public int getLine() {
        return this.line;
    }
}
