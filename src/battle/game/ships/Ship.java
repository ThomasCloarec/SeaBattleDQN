package battle.game.ships;

import battle.text.AppText;

/**
 * Represent a ship and its position
 */
public class Ship implements Cloneable {
    /**
     * The maximum size of a ship
     */
    public static final int MAX_SIZE = 10;
    /**
     * The minimum size of a ship
     */
    public static final int MIN_SIZE = 1;
    /**
     * The name of the ship
     */
    private final String name;
    /**
     * The size of the ship
     */
    private final int size;
    /**
     * The column origin of the ship (horizontal axis)
     */
    private int columnOrigin;
    /**
     * The direction of the ship
     */
    private Direction direction;
    /**
     * The number of hits received by the ship
     */
    private int hitNumber;
    /**
     * The line origin of the ship (vertical axis)
     */
    private int lineOrigin;

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
    }

    /**
     * Reset the hit number
     */
    public void resetHitNumber() {
        this.hitNumber = 0;
    }

    /**
     * This method allows to check if the ship is on a particular square
     *
     * @param line   the line of the square to check
     * @param column the column of the square to check
     * @return is the ship on this particular square
     */
    public boolean contains(int line, int column) {
        if (line < 0) {
            throw new IllegalArgumentException("The line parameter should be a number bigger than 0.");
        } else if (column < 0) {
            throw new IllegalArgumentException("The column parameter should be a number bigger than 0.");
        }

        boolean ret = false;

        if (this.direction == Direction.HORIZONTAL) {
            if (column >= this.columnOrigin && column < this.columnOrigin + this.size && line == this.lineOrigin) {
                ret = true;
            }
        } else if (this.direction == Direction.VERTICAL) {
            if (line >= this.lineOrigin && line < this.lineOrigin + this.size && column == this.columnOrigin) {
                ret = true;
            }
        }

        return ret;
    }

    /**
     * Increment The number of hits received by the ship.
     */
    public void addHit() {
        this.hitNumber++;
    }

    /**
     * Clone ship.
     *
     * @return the ship
     * @throws CloneNotSupportedException the clone not supported exception
     */
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
        return AppText.getTextFor("ship") + "{" +
                AppText.getTextFor("name") + "='" + this.name + "', " +
                AppText.getTextFor("size") + "=" + this.size + ", " +
                AppText.getTextFor("hit_number") + "=" + this.hitNumber +
                ", xOrigin=" + this.lineOrigin +
                ", yOrigin=" + this.columnOrigin +
                ", direction=" + this.direction +
                "}";
    }

    /**
     * Sets new The column origin of the ship horizontal axis.
     *
     * @param columnOrigin New value of The column origin of the ship horizontal axis.
     */
    public void setColumnOrigin(int columnOrigin) {
        this.columnOrigin = columnOrigin;
    }

    /**
     * Sets new The direction of the ship.
     *
     * @param direction New value of The direction of the ship.
     */
    public void setDirection(Direction direction) {
        // Check parameters
        if (direction == null) {
            throw new IllegalArgumentException("One or more parameter is null. See the concerned method.");
        }
        this.direction = direction;
    }

    /**
     * Sets new The line origin of the ship vertical axis.
     *
     * @param lineOrigin New value of The line origin of the ship vertical axis.
     */
    public void setLineOrigin(int lineOrigin) {
        this.lineOrigin = lineOrigin;
    }

    /**
     * Get the number of hits the ship received
     *
     * @return the hit number
     */
    public int getHitNumber() {
        return this.hitNumber;
    }

    /**
     * Gets The name of the ship.
     *
     * @return Value of The name of the ship.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets The size of the ship.
     *
     * @return Value of The size of the ship.
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Test if the direction is set (used to test if the ship has been initialized by the player)
     *
     * @return is the direction set
     */
    public boolean isDirectionSet() {
        return this.direction != null;
    }

    /**
     * If the ship has been hit the same number of times as its size, it's considered sunk
     *
     * @return is the ship sunk
     */
    public boolean isSunk() {
        return this.size <= this.hitNumber;
    }
}
