package battle;

/**
 * Represent a ship and its position
 */
public class Ship implements Cloneable {
    /**
     * The maximum size of a ship
     */
    public static final int MAX_SIZE = 5;
    /**
     * The minimum size of a ship
     */
    public static final int MIN_SIZE = 1;
    /**
     * The direction of the ship
     */
    private final Direction direction;
    /**
     * The name of the ship
     */
    private final String name;
    /**
     * The size of the ship
     */
    private final int size;
    /**
     * The number of hits received by the ship
     */
    private int hitNumber;
    /**
     * The xOrigin of the ship (vertical axis)
     */
    private int xOrigin;
    /**
     * The yOrigin of the ship (horizontal axis)
     */
    private int yOrigin;

    /**
     * The constructor of the class (initialize name and size)
     *
     * @param name the name of the ship
     * @param size the size of the ship
     */
    public Ship(String name, int size) {
        // Check parameters
        if (name == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        } else if (size < Ship.MIN_SIZE || size > Ship.MAX_SIZE) {
            throw new IllegalArgumentException("The size parameter should be a number between " + Ship.MIN_SIZE + " and " + Ship.MAX_SIZE + ".");
        }

        this.name = name;
        this.size = size;
        this.direction = Direction.HORIZONTAL;
    }

    /**
     * This method allows to check if the ship is on a particular square
     *
     * @param x the x of the square to check
     * @param y the y of the square to check
     * @return is the ship on this particular square
     */
    public boolean contains(int x, int y) {
        if (x < 0) {
            throw new IllegalArgumentException("The x parameter should be a number bigger than 0.");
        } else if (y < 0) {
            throw new IllegalArgumentException("The y parameter should be a number bigger than 0.");
        }

        boolean ret = false;

        if (this.direction == Direction.HORIZONTAL) {
            if (x >= this.xOrigin && x < this.xOrigin + this.size && y == this.yOrigin) {
                ret = true;
            }
        } else if (this.direction == Direction.VERTICAL) {
            if (y >= this.yOrigin && y < this.yOrigin + this.size && x == this.xOrigin) {
                ret = true;
            }
        }

        return ret;
    }

    /**
     * Increment the hitNumber of the ship
     */
    public void addHit() {
        this.hitNumber++;
    }

    @Override
    public Ship clone() throws CloneNotSupportedException {
        return (Ship) super.clone();
    }

    /**
     * Return each field of the ship
     *
     * @return the formatted string
     */
    @Override
    public String toString() {
        return "Ship{" +
                "name='" + this.name + '\'' +
                ", size=" + this.size +
                ", hitNumber=" + this.hitNumber +
                ", xOrigin=" + this.xOrigin +
                ", yOrigin=" + this.yOrigin +
                ", direction=" + this.direction +
                "}";
    }

    /**
     * If the ship has been hit the same number of times as its size, it's considered sunk
     *
     * @return is the ship sunk
     */
    public boolean isSunk() {
        return this.size == this.hitNumber;
    }
}
